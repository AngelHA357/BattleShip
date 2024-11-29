package org.itson.arquitectura.battleshipservidor.comunicacion;

import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipservidor.controlador.ControladorEventos;
import org.itson.arquitectura.battleshipservidor.negocio.ColocarNavesBO;
import org.itson.arquitectura.battleshipservidor.negocio.DisparoBO;
import org.itson.arquitectura.battleshipservidor.negocio.PartidaBO;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

/**
 *
 * @author PC
 */
public class ManejadorEventos {

    private static ManejadorEventos instance;
    private PartidaBO partidaBO;
    private ColocarNavesBO colocarNavesBO;
    private DisparoBO disparoBO;

    private ManejadorEventos() {
        this.partidaBO = new PartidaBO();
        this.colocarNavesBO = ColocarNavesBO.getInstance();
        this.disparoBO = DisparoBO.getInstance();
    }

    public static synchronized ManejadorEventos getInstance() {
        if (instance == null) {
            instance = new ManejadorEventos();
        }
        return instance;
    }

    public EventoDTO manejarEvento(EventoDTO evento) {
        Map<String, Object> datos = evento.getDatos();

        switch (evento.getEvento()) {
            case SESSION_INIT:
                Map<String, Object> respuesta = new HashMap<>();
                respuesta.put("exitoso", true);
                return new EventoDTO(Evento.SESSION_INIT, respuesta);

            case CREAR_PARTIDA:
                return partidaBO.crearPartida(evento.getIdJugador());

            case UNIRSE_PARTIDA:
                String codigoSala = (String) datos.get("codigoSala");
                return partidaBO.unirsePartida(codigoSala, evento.getIdJugador());

            case CONFIGURAR_JUGADOR:
                String nombreJugador = (String) datos.get("nombreJugador");
                String colorBarco = (String) datos.get("colorBarco");
                return partidaBO.configurarJugador(evento.getIdJugador(), nombreJugador, colorBarco);

            case CREAR_TABLERO:
                System.out.println("Procesando inicialización de tablero para jugador: " + evento.getIdJugador());
                try {
                    int[][] casillas = (int[][]) datos.get("tablero");
                    EventoDTO respuestaTablero = colocarNavesBO.crearTableroCompleto(evento.getIdJugador(), casillas);
                    System.out.println("Tablero inicializado correctamente");
                    return respuestaTablero;
                } catch (Exception e) {
                    System.out.println("Error al inicializar tablero: " + e.getMessage());
                    Map<String, Object> datosError = new HashMap<>();
                    datosError.put("exitoso", false);
                    datosError.put("error", e.getMessage());
                    return new EventoDTO(Evento.INICIALIZAR_TABLERO, datosError);
                }
            case DISPARAR:
                int filaDisparo = (int) datos.get("coordenadaX");
                int columnaDisparo = (int) datos.get("coordenadaY");
                return disparoBO.procesarDisparo(evento.getIdJugador(), filaDisparo, columnaDisparo);

            case JUGADOR_LISTO:
                return partidaBO.jugadorListo(evento.getIdJugador());

            default:
                throw new IllegalArgumentException("Evento no reconocido: " + evento.getEvento());
        }
    }
}

//            case COLOCAR_NAVES:
//                @SuppressWarnings("unchecked") Map<String, Object> posiciones = (Map<String, Object>) datos.get("posiciones");
//                return partidaBO.colocarNaves(evento.getIdJugador(), posiciones);
//            case JUGADOR_LISTO:
//                return partidaBO.jugadorListo(evento.getIdJugador());
