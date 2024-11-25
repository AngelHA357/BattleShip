package org.itson.arquitectura.battleshipservidor.negocio;

import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipservidor.dominio.Coordenada;
import org.itson.arquitectura.battleshipservidor.dominio.Disparo;
import org.itson.arquitectura.battleshipservidor.dominio.Jugador;
import org.itson.arquitectura.battleshipservidor.dominio.Partida;
import org.itson.arquitectura.battleshipservidor.dominio.Tablero.Tablero;
import org.itson.arquitectura.battleshipservidor.dominio.UbicacionNave;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.enums.ResultadoDisparo;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

public class DisparoBO {

    private static DisparoBO instance;

    private DisparoBO() {
    }

    public static synchronized DisparoBO getInstance() {
        if (instance == null) {
            instance = new DisparoBO();
        }
        return instance;
    }

    public EventoDTO procesarDisparo(String idJugador, int fila, int columna) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            Partida partida = Partida.getInstance();
            Jugador jugadorActual = obtenerJugador(partida, idJugador);
            Jugador jugadorOponente = obtenerOponente(partida, idJugador);

            if (!partida.esTurnoJugador(jugadorActual)) {
                respuesta.put("error", "No es tu turno");
                return new EventoDTO(Evento.DISPARAR, respuesta);
            }

            if (partida.esCasillaDisparada(jugadorOponente, fila, columna)) {
                respuesta.put("error", "Casilla ya disparada");
                return new EventoDTO(Evento.DISPARAR, respuesta);
            }

            String resultadoDisparo = realizarDisparo(jugadorOponente, fila, columna);

            respuesta.put("resultado", resultadoDisparo);
            respuesta.put("coordenadaX", fila);
            respuesta.put("coordenadaY", columna);

            actualizarEstadoNaves(jugadorOponente, resultadoDisparo);

            agregarEstadoNaves(respuesta, jugadorActual, jugadorOponente);

            if (partidaTerminada(jugadorOponente)) {
                respuesta.put("finJuego", true);
                respuesta.put("ganador", idJugador);
            } else {
                partida.cambiarTurno();
                respuesta.put("jugadorActual", partida.getJugadorActual().getId());
            }

            registrarDisparo(partida, fila, columna, idJugador, resultadoDisparo);

            return new EventoDTO(Evento.DISPARAR, respuesta);

        } catch (Exception e) {
            System.out.println("Error al procesar disparo: " + e.getMessage());
            respuesta.put("error", "Error interno: " + e.getMessage());
            return new EventoDTO(Evento.DISPARAR, respuesta);
        }
    }

    private Jugador obtenerJugador(Partida partida, String idJugador) {
        return partida.getJugadores().stream()
                .filter(j -> j.getId().equals(idJugador))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Jugador no encontrado"));
    }

    private Jugador obtenerOponente(Partida partida, String idJugador) {
        return partida.getJugadores().stream()
                .filter(j -> !j.getId().equals(idJugador))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Oponente no encontrado"));
    }

    private String realizarDisparo(Jugador jugadorObjetivo, int fila, int columna) {
        Tablero tableroObjetivo = jugadorObjetivo.getTablero();

        if (!tableroObjetivo.tieneNave(fila, columna)) {
            return "AGUA";
        }

        tableroObjetivo.marcarImpacto(fila, columna);

        UbicacionNave ubicacionNave = tableroObjetivo.obtenerNaveEnPosicion(fila, columna);
        if (ubicacionNave != null && verificarHundimiento(jugadorObjetivo, ubicacionNave)) {
            return "HUNDIDO";
        }

        return "IMPACTO";
    }

    private boolean todasLasCasillasImpactadas(UbicacionNave ubicacionNave) {
        return ubicacionNave.getCasillasOcupadas().values().stream()
                .allMatch(impactada -> impactada);
    }

    private boolean verificarHundimiento(Jugador jugador, UbicacionNave ubicacionNave) {
        return ubicacionNave.getCasillasOcupadas().entrySet().stream()
                .allMatch(entry -> entry.getValue());
    }

    private void actualizarEstadoNaves(Jugador jugador, String resultado) {
        switch (resultado) {
            case "IMPACTO":
                jugador.incrementarNavesDanadas();
                break;
            case "HUNDIDO":
                jugador.incrementarNavesDestruidas();
                break;
        }
    }

    private void agregarEstadoNaves(Map<String, Object> respuesta, Jugador jugadorActual, Jugador oponente) {
        respuesta.put("navesIntactasPropias", jugadorActual.getNavesIntactas());
        respuesta.put("navesDanadasPropias", jugadorActual.getNavesDanadas());
        respuesta.put("navesDestruidasPropias", jugadorActual.getNavesDestruidas());

        respuesta.put("navesIntactasRival", oponente.getNavesIntactas());
        respuesta.put("navesDanadasRival", oponente.getNavesDanadas());
        respuesta.put("navesDestruidasRival", oponente.getNavesDestruidas());
    }

    private boolean partidaTerminada(Jugador jugador) {
        return jugador.getNavesDestruidas() == jugador.getTotalNaves();
    }

    private void registrarDisparo(Partida partida, int fila, int columna, String idJugador, String resultado) {
        partida.registrarDisparo(idJugador, fila, columna, resultado);
    }
}
