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
                throw new IllegalStateException("Jugador no encontrado");
            }

            jugador.setNombre(nombre);
            jugador.setColor(colorBarco.equalsIgnoreCase("azul") ? Color.AZUL : Color.ROJO);
            jugador.setId(idJugador);
            partida.agregarJugador(jugador);

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);
            datosRespuesta.put("idJugador", idJugador);
            datosRespuesta.put("nombre", jugador.getNombre());
            datosRespuesta.put("color", jugador.getColor().name());
           
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
                
                String nombreRival = "";
                
                for (Map.Entry<String, Jugador> entry : jugadoresTemp.entrySet()) {
                    if (!entry.getKey().equals(idJugador)) {
                        nombreRival = entry.getValue().getNombre();
                        break;
                    }
                }
    
                datosRespuesta.put("jugadorRival", nombreRival);
            }

            return new EventoDTO(Evento.JUGADOR_LISTO, datosRespuesta);
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
