package org.itson.arquitectura.battleshipservidor.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import org.itson.arquitectura.battleshipeventos.eventos.Evento;
import org.itson.arquitectura.battleshipservidor.dominio.Jugador;
import org.itson.arquitectura.battleshipservidor.dominio.Partida;
import org.itson.arquitectura.battleshipservidor.dominio.enums.Color;
import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoPartida;

/**
 *
 * @author victo
 */
public class PartidaBO {

    private Map<String, Jugador> jugadoresTemp = new HashMap<>();

    public EventoDTO crearPartida() {
        try {
            Partida partida = Partida.getInstance();
            if (partida == null) {
                throw new IllegalStateException("No se pudo inicializar la partida");
            }
            String codigoSala = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            partida.setCodigoSala(codigoSala);
            partida.setEstado(EstadoPartida.ESPERANDO);
            partida.setJugadores(new ArrayList<>());

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

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);
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
            jugador.setColor(Color.valueOf(colorBarco));

            partida.getJugadores().add(jugador);

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);

            if (partida.getJugadores().size() > 1) {
                datosRespuesta.put("oponenteNombre", partida.getJugadores().get(0).getNombre());
            }

            EventoDTO respuesta = new EventoDTO(Evento.CONFIGURAR_JUGADOR, datosRespuesta);

            return respuesta;

        } catch (Exception e) {
            System.out.println("Error al configurar jugador: " + e.getMessage());
            return null;
        }
    }
}
