package org.itson.arquitectura.battleshipservidor.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipservidor.dominio.Coordenada;
import org.itson.arquitectura.battleshipservidor.dominio.Disparo;
import org.itson.arquitectura.battleshipservidor.dominio.Jugador;
import org.itson.arquitectura.battleshipservidor.dominio.Partida;
import org.itson.arquitectura.battleshipservidor.dominio.Tablero.Tablero;
import org.itson.arquitectura.battleshipservidor.dominio.UbicacionNave;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;
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
        try {
            Tablero tableroObjetivo = jugadorObjetivo.getTablero();
            System.out.println("Procesando disparo en [" + fila + "," + columna + "] para jugador " + jugadorObjetivo.getId());

            if (tableroObjetivo == null) {
                throw new IllegalStateException("Tablero no inicializado");
            }

            if (!tableroObjetivo.tieneNave(fila, columna)) {
                System.out.println("Agua en [" + fila + "," + columna + "]");
                return "AGUA";
            }

            UbicacionNave ubicacionNave = tableroObjetivo.obtenerNaveEnPosicion(fila, columna);
            if (ubicacionNave == null) {
                throw new IllegalStateException("No se encontró nave en la posición del impacto");
            }

            // Marcar el impacto específico
            Casilla casillaImpactada = ubicacionNave.getCasillasOcupadas().keySet().stream()
                    .filter(c -> c.getCoordenada().getX() == fila && c.getCoordenada().getY() == columna)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Casilla no encontrada"));

            ubicacionNave.getCasillasOcupadas().put(casillaImpactada, true);

            // Verificar si está hundida
            boolean hundida = ubicacionNave.getCasillasOcupadas().values().stream()
                    .allMatch(impactada -> impactada);

            System.out.println("Casillas impactadas en la nave: "
                    + ubicacionNave.getCasillasOcupadas().values().stream()
                            .filter(v -> v).count()
                    + " de " + ubicacionNave.getCasillasOcupadas().size());

            if (hundida) {
                System.out.println("Nave hundida");
                return "HUNDIDO";
            }

            System.out.println("Impacto normal");
            return "IMPACTO";
        } catch (Exception e) {
            System.out.println("Error en realizarDisparo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
        try {
            Jugador jugadorObjetivo = obtenerOponente(partida, idJugador);
            Tablero tablero = jugadorObjetivo.getTablero();

            // Inicializar lista de disparos si es null
            if (tablero.getDisparos() == null) {
                tablero.setDisparos(new ArrayList<>());
            }

            ResultadoDisparo resultadoEnum;
            switch (resultado) {
                case "HUNDIDO":
                    resultadoEnum = ResultadoDisparo.HUNDIDO;
                    break;
                case "AGUA":
                    resultadoEnum = ResultadoDisparo.AGUA;
                    break;
                case "IMPACTO":
                    resultadoEnum = ResultadoDisparo.IMPACTO;
                    break;
                default:
                    throw new IllegalArgumentException("Resultado no válido: " + resultado);
            }

            Coordenada coordenada = new Coordenada(fila, columna);
            Disparo disparo = new Disparo(coordenada, resultadoEnum);
            tablero.getDisparos().add(disparo);
        } catch (Exception e) {
            System.out.println("Error al registrar disparo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
