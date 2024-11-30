package org.itson.arquitectura.battleshipservidor.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipservidor.comunicacion.ClienteHandler;
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

    public EventoDTO procesarDisparo(String idJugador, int y, int x) {
        if (idJugador == null) {
            throw new IllegalArgumentException("ID de jugador no puede ser null");
        }

        Map<String, Object> respuestaDisparador = new HashMap<>();
        Map<String, Object> respuestaReceptor = new HashMap<>();
        try {
            Partida partida = Partida.getInstance();
            Jugador jugadorActual = obtenerJugador(partida, idJugador);

            String nuevoJugadorEnTurno = partida.getJugadorEnTurno().getId();
            System.out.println("Procesando disparo - ID jugador: " + idJugador
                    + ", Nuevo jugador en turno: " + nuevoJugadorEnTurno);

            Jugador jugadorOponente = obtenerOponente(partida, idJugador);

            if (!partida.esTurnoJugador(jugadorActual)) {
                respuestaDisparador.put("error", "No es tu turno");
                return new EventoDTO(Evento.DISPARAR, respuestaDisparador);
            }

            String resultadoDisparo = realizarDisparo(jugadorOponente, x, y);

            if (partida.esCasillaDisparada(jugadorOponente, y, x)) {
                respuestaDisparador.put("error", "Casilla ya disparada");
                return new EventoDTO(Evento.DISPARAR, respuestaDisparador);
            }

            if (!partidaTerminada(jugadorOponente)) {
                partida.cambiarTurno();
                nuevoJugadorEnTurno = partida.getJugadorEnTurno().getId();

                // Usar el mismo jugador en turno para todas las respuestas
                respuestaDisparador.put("jugadorActual", nuevoJugadorEnTurno);
                respuestaReceptor.put("jugadorActual", nuevoJugadorEnTurno);

                System.out.println("Turno cambiado al jugador: " + nuevoJugadorEnTurno);
            }

            respuestaDisparador.put("resultado", resultadoDisparo);
            respuestaDisparador.put("coordenadaX", x);
            respuestaDisparador.put("coordenadaY", y);

            respuestaReceptor.put("coordenadaX", x);
            respuestaReceptor.put("coordenadaY", y);
            respuestaReceptor.put("resultado", resultadoDisparo);

            actualizarEstadoNaves(jugadorOponente, resultadoDisparo);
            agregarEstadoNaves(respuestaDisparador, jugadorActual, jugadorOponente);

            if (partidaTerminada(jugadorOponente)) {
                respuestaDisparador.put("finJuego", true);
                respuestaDisparador.put("ganador", idJugador);
                respuestaReceptor.put("finJuego", true);
                respuestaReceptor.put("ganador", idJugador);
            }

            ClienteHandler.enviarEventoAJugador(
                    Integer.parseInt(jugadorOponente.getId()),
                    new EventoDTO(Evento.RECIBIR_DISPARO, respuestaReceptor)
            );

            registrarDisparo(partida, y, x, idJugador, resultadoDisparo);
            return new EventoDTO(Evento.DISPARAR, respuestaDisparador);

        } catch (Exception e) {
            System.out.println("Error al procesar disparo: " + e.getMessage());
            respuestaDisparador.put("error", "Error interno: " + e.getMessage());
            return new EventoDTO(Evento.DISPARAR, respuestaDisparador);
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

    private String realizarDisparo(Jugador jugadorObjetivo, int x, int y) {
        try {
            Tablero tableroObjetivo = jugadorObjetivo.getTablero();
            System.out.println("Procesando disparo en [" + x + "," + y + "] para jugador " + jugadorObjetivo.getId());

            if (tableroObjetivo == null) {
                throw new IllegalStateException("Tablero no inicializado");
            }

            // Verificar que las coordenadas están dentro del tablero
            if (x < 0 || x >= tableroObjetivo.getAncho()
                    || y < 0 || y >= tableroObjetivo.getAlto()) {
                throw new IllegalArgumentException("Coordenadas fuera del tablero");
            }
            
            // Imprimir el estado del tablero para debug
            System.out.println("Estado del tablero antes del disparo:");
            tableroObjetivo.imprimirEstado();
            
            if (!tableroObjetivo.tieneNave(x, y)) {
                System.out.println("Agua en [" + x + "," + y + "]");
                return "AGUA";
            }

            UbicacionNave ubicacionNave = tableroObjetivo.obtenerNaveEnPosicion(x, y);
            if (ubicacionNave == null) {
                throw new IllegalStateException("No se encontró nave en la posición del impacto");
            }

            Casilla casillaImpactada = ubicacionNave.getCasillasOcupadas().keySet().stream()
                    .filter(c -> c.getCoordenada().getX() == x && c.getCoordenada().getY() == y)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Casilla no encontrada"));

            ubicacionNave.getCasillasOcupadas().put(casillaImpactada, true);

            boolean hundida = ubicacionNave.getCasillasOcupadas().values().stream()
                    .allMatch(impactada -> impactada);

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

            Coordenada coordenada = new Coordenada(columna, fila);
            Disparo disparo = new Disparo(coordenada, resultadoEnum);
            tablero.getDisparos().add(disparo);
        } catch (Exception e) {
            System.out.println("Error al registrar disparo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
