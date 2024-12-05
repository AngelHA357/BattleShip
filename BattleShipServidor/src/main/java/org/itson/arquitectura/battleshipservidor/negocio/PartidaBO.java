package org.itson.arquitectura.battleshipservidor.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.itson.arquitectura.battleshipservidor.comunicacion.ClienteHandler;
import org.itson.arquitectura.battleshipservidor.dominio.Jugador;
import org.itson.arquitectura.battleshipservidor.dominio.Partida;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.enums.Color;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoPartida;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

/**
 *
 * @author victo
 */
public class PartidaBO {

    private static final Map<String, Jugador> jugadoresTemp = new ConcurrentHashMap<>();

    public EventoDTO crearPartida(String idJugador) {
        try {
            Partida partida = Partida.getInstance();
            if (partida == null) {
                throw new IllegalStateException("No se pudo inicializar la partida");
            }
            String codigoSala = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            partida.setCodigoSala(codigoSala);
            partida.setEstado(EstadoPartida.ESPERANDO);
            partida.setJugadores(new ArrayList<>());

            Jugador jugadorTemp = new Jugador();
            jugadoresTemp.put(idJugador, jugadorTemp);
            partida.agregarJugador(jugadorTemp);

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("codigoSala", codigoSala);
            datosRespuesta.put("exitoso", true);
            return new EventoDTO(Evento.CREAR_PARTIDA, datosRespuesta);

        } catch (Exception e) {
            System.out.println("Error al crear partida: " + e.getMessage());
            return null;
        }
    }

    public EventoDTO unirsePartida(String codigoSala, String idJugador) {
        try {
            Partida partida = Partida.getInstance();

            if (!partida.getCodigoSala().equals(codigoSala)) {
                throw new IllegalArgumentException("Código de sala inválido");
            }

            if (partida.getJugadores().size() >= 2) {
                throw new IllegalArgumentException("La partida está llena");
            }

            Jugador jugadorTemp = new Jugador();
            jugadoresTemp.put(idJugador, jugadorTemp);

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);
            datosRespuesta.put("cantidadJugadores", jugadoresTemp.size());
            EventoDTO respuesta = new EventoDTO(Evento.UNIRSE_PARTIDA, datosRespuesta);

            //Solo quiero probar
            System.out.println("si se unió" + idJugador);

            return respuesta;
        } catch (Exception e) {
            System.out.println("Error al unirse a partida: " + e.getMessage());
            return null;
        }
    }

    public EventoDTO configurarJugador(String idJugador, String nombre, String colorBarco) {
        try {
            Partida partida = Partida.getInstance();
            Jugador jugador = jugadoresTemp.get(idJugador);

            if (jugador == null) {
                jugador = new Jugador();
                jugadoresTemp.put(idJugador, jugador);
            }

            jugador.setNombre(nombre);
            jugador.setColor(colorBarco.equalsIgnoreCase("azul") ? Color.AZUL : Color.ROJO);
            jugador.setId(idJugador);

            // Asegurarnos de que el jugador está en la lista de la partida
            if (!partida.getJugadores().contains(jugador)) {
                partida.agregarJugador(jugador);
            }

            String nombreRival = "";
            String idRival = "";
            for (Jugador otroJugador : partida.getJugadores()) {
                if (!otroJugador.getId().equals(idJugador)) {
                    nombreRival = otroJugador.getNombre();
                    idRival = otroJugador.getId();

                    if (idRival != null && !idRival.isEmpty()) {
                        // Notificar al jugador rival sobre el nuevo jugador
                        Map<String, Object> datosActualizacion = new HashMap<>();
                        datosActualizacion.put("actualizarRival", true);
                        datosActualizacion.put("nombreRival", nombre);
                        EventoDTO eventoActualizacion = new EventoDTO(Evento.CONFIGURAR_JUGADOR, datosActualizacion);
                        eventoActualizacion.setIdJugador(idRival);

                        System.out.println("Enviando actualización de rival - ID: " + idRival + ", Nombre: " + nombre);
                        ClienteHandler.enviarEventoAJugador(Integer.parseInt(idRival), eventoActualizacion);
                    }
                    break;
                }
            }

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);
            datosRespuesta.put("idJugador", idJugador);
            datosRespuesta.put("nombre", jugador.getNombre());
            datosRespuesta.put("color", jugador.getColor().name());
            datosRespuesta.put("nombreRival", nombreRival);

            EventoDTO respuesta = new EventoDTO(Evento.CONFIGURAR_JUGADOR, datosRespuesta);
            respuesta.setIdJugador(idJugador);
            return respuesta;

        } catch (Exception e) {
            Map<String, Object> datosError = new HashMap<>();
            datosError.put("exitoso", false);
            datosError.put("error", e.getMessage());
            datosError.put("idJugador", idJugador);
            return new EventoDTO(Evento.CONFIGURAR_JUGADOR, datosError);
        }
    }

    public EventoDTO jugadorListo(String idJugador) {
        try {
            Partida partida = Partida.getInstance();
            Jugador jugador = jugadoresTemp.get(idJugador);

            if (jugador == null) {
                throw new IllegalStateException("Jugador no encontrado");
            }

            jugador.setListo(true);

            boolean todosListos = partida.getJugadores().stream()
                    .allMatch(j -> j.isListo());

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);

            if (todosListos) {
                inicializarPrimerTurno();
                partida.setEstado(EstadoPartida.EN_PROGRESO);
                datosRespuesta.put("partidaIniciada", true);
                datosRespuesta.put("jugadorEnTurno", partida.getJugadorEnTurno().getId());
                datosRespuesta.put("estadoPartida", partida.getEstado());
            }

            List<EventoDTO> eventosJugadores = new ArrayList<>();

            for (Jugador jugadorActual : partida.getJugadores()) {
                Map<String, Object> datosJugador = new HashMap<>(datosRespuesta);

                String nombreRival = "";
                for (Jugador otroJugador : partida.getJugadores()) {
                    if (!otroJugador.getId().equals(jugadorActual.getId())) {
                        nombreRival = otroJugador.getNombre();
                        break;
                    }
                }

                datosJugador.put("jugadorRival", nombreRival);

                EventoDTO eventoJugador = new EventoDTO(Evento.JUGADOR_LISTO, datosJugador);
                eventoJugador.setIdJugador(jugadorActual.getId());

                ClienteHandler.enviarEventoAJugador(
                        Integer.parseInt(jugadorActual.getId()),
                        eventoJugador
                );
            }

            Map<String, Object> datosJugadorActual = new HashMap<>(datosRespuesta);
            String nombreRivalActual = "";
            for (Jugador otroJugador : partida.getJugadores()) {
                if (!otroJugador.getId().equals(idJugador)) {
                    nombreRivalActual = otroJugador.getNombre();
                    break;
                }
            }
            datosJugadorActual.put("jugadorRival", nombreRivalActual);
            return new EventoDTO(Evento.JUGADOR_LISTO, datosJugadorActual);

        } catch (Exception e) {
            Map<String, Object> datosError = new HashMap<>();
            datosError.put("exitoso", false);
            datosError.put("error", e.getMessage());
            return new EventoDTO(Evento.JUGADOR_LISTO, datosError);
        }
    }

    private void inicializarPrimerTurno() {
        Partida partida = Partida.getInstance();
        List<Jugador> jugadores = partida.getJugadores();

        if (!jugadores.isEmpty()) {
            Random random = new Random();
            int indiceInicial = random.nextInt(jugadores.size());
            Jugador primerJugador = jugadores.get(indiceInicial);
            partida.setJugadorEnTurno(primerJugador);
        }
    }

    public EventoDTO abandonarPartida(String idJugador) {
        try {
            Partida partida = Partida.getInstance();

            Jugador jugador = partida.getJugadores().stream()
                    .filter(j -> j.getId().equals(idJugador))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Jugador no encontrado en la partida"));

            List<Jugador> jugadoresActuales = new ArrayList<>(partida.getJugadores());

            partida.removerJugador(idJugador);
            partida.setEstado(EstadoPartida.FINALIZADA);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("jugadorAbandonoId", idJugador);
            respuesta.put("exitoso", true);
            respuesta.put("estadoPartida", partida.getEstado());

            EventoDTO eventoAbandono = new EventoDTO(Evento.ABANDONAR_PARTIDA, respuesta);

            System.out.println("Jugador " + idJugador + " ha abandonado la partida");

            return eventoAbandono;

        } catch (Exception e) {
            System.out.println("Error al procesar abandono de partida: " + e.getMessage());
            Map<String, Object> respuestaError = new HashMap<>();
            respuestaError.put("exitoso", false);
            respuestaError.put("error", "Error al abandonar partida: " + e.getMessage());
            return new EventoDTO(Evento.ABANDONAR_PARTIDA, respuestaError);
        }
    }
}
