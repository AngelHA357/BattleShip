package org.itson.arquitectura.battleshipservidor.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
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

    private static final Map<String, Jugador> jugadoresTemp = new HashMap<>();

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
//            partida.agregarJugador(jugadorTemp);

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("codigoSala", codigoSala);
            EventoDTO respuesta = new EventoDTO(Evento.CREAR_PARTIDA, datosRespuesta);

            return respuesta;
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
//            partida.agregarJugador(jugadorTemp);

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

            return new EventoDTO(Evento.CONFIGURAR_JUGADOR, datosRespuesta);

        } catch (Exception e) {
            Map<String, Object> datosError = new HashMap<>();
            datosError.put("exitoso", false);
            datosError.put("error", e.getMessage());
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

            // Marcar al jugador como listo
            jugador.setListo(true);

            // Verificar si todos los jugadores están listos
            boolean todosListos = partida.getJugadores().stream()
                    .allMatch(j -> j.isListo());

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);

            // Si todos están listos, iniciar la partida
            if (todosListos) {
                inicializarPrimerTurno();
                partida.setEstado(EstadoPartida.EN_PROGRESO);
                datosRespuesta.put("partidaIniciada", true);
                datosRespuesta.put("jugadorEnTurno", partida.getJugadorEnTurno().getId());
                datosRespuesta.put("estadoPartida", partida.getEstado());
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
}
