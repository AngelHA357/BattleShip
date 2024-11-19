package org.itson.arquitectura.battleshipservidor.comunicacion;

import java.util.Map;
import org.itson.arquitectura.battleshipservidor.controlador.ControladorEventos;
import org.itson.arquitectura.battleshipservidor.negocio.PartidaBO;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;

/**
 *
 * @author PC
 */
public class ManejadorEventos {

    private static ManejadorEventos instance;
    private PartidaBO partidaBO;

    private ManejadorEventos() {
        this.partidaBO = new PartidaBO();
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
            case CREAR_PARTIDA:
                return partidaBO.crearPartida();

            case UNIRSE_PARTIDA:
                String codigoSala = (String) datos.get("codigoSala");
                return partidaBO.unirsePartida(codigoSala, evento.getIdJugador());

            case CONFIGURAR_JUGADOR:
                String nombreJugador = (String) datos.get("nombreJugador");
                String colorBarco = (String) datos.get("colorBarco");
                return partidaBO.configurarJugador(evento.getIdJugador(), nombreJugador, colorBarco);

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