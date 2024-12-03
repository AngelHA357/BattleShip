package org.itson.arquitectura.battleshipservidor.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

            if (partida.esCasillaDisparada(jugadorOponente, y, x)) {
                respuestaDisparador.put("error", "Casilla ya disparada");
                return new EventoDTO(Evento.DISPARAR, respuestaDisparador);
            }

            ResultadoDisparoInfo resultadoInfo = realizarDisparo(jugadorOponente, x, y);
            
            actualizarEstadoNaves(jugadorOponente, resultadoInfo.resultado);
            agregarEstadoNaves(respuestaDisparador, jugadorActual, jugadorOponente);

            if (!partidaTerminada(jugadorOponente)) {
                partida.cambiarTurno();
                nuevoJugadorEnTurno = partida.getJugadorEnTurno().getId();

                respuestaDisparador.put("jugadorActual", nuevoJugadorEnTurno);
                respuestaReceptor.put("jugadorActual", nuevoJugadorEnTurno);

                System.out.println("Turno cambiado al jugador: " + nuevoJugadorEnTurno);
            }

            respuestaDisparador.put("resultado", resultadoInfo.resultado.name());
            respuestaDisparador.put("coordenadaX", x);
            respuestaDisparador.put("coordenadaY", y);

            respuestaReceptor.put("coordenadaX", x);
            respuestaReceptor.put("coordenadaY", y);
            respuestaReceptor.put("resultado", resultadoInfo.resultado.name());

            if (resultadoInfo.resultado == ResultadoDisparo.HUNDIDO && resultadoInfo.casillasHundidas != null) {
                respuestaDisparador.put("casillasHundidas", resultadoInfo.casillasHundidas);
                respuestaReceptor.put("casillasHundidas", resultadoInfo.casillasHundidas);
            }

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

            registrarDisparo(partida, y, x, idJugador, resultadoInfo);
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

    private ResultadoDisparoInfo realizarDisparo(Jugador jugadorObjetivo, int x, int y) {
        try {
            Tablero tableroObjetivo = jugadorObjetivo.getTablero();
            System.out.println("Procesando disparo en [" + x + "," + y + "]");

            if (tableroObjetivo == null) {
                throw new IllegalStateException("Tablero no inicializado");
            }

            if (!tableroObjetivo.tieneNave(x, y)) {
                System.out.println("Agua en [" + x + "," + y + "]");
                return new ResultadoDisparoInfo(ResultadoDisparo.AGUA, null);
            }

            UbicacionNave ubicacionNave = tableroObjetivo.obtenerNaveEnPosicion(x, y);
            if (ubicacionNave == null) {
                return new ResultadoDisparoInfo(ResultadoDisparo.AGUA, null);
            }

            Casilla casillaImpactada = null;
            for (Casilla casilla : ubicacionNave.getCasillasOcupadas().keySet()) {
                if (casilla.getCoordenada().getX() == y
                        && casilla.getCoordenada().getY() == x) {
                    casillaImpactada = casilla;
                    break;
                }
            }

            if (casillaImpactada == null) {
                return new ResultadoDisparoInfo(ResultadoDisparo.AGUA, null);
            }

            ubicacionNave.getCasillasOcupadas().put(casillaImpactada, true);

            System.out.println("Estado de casillas de la nave después del impacto:");
            ubicacionNave.getCasillasOcupadas().forEach((casilla, impactada)
                    -> System.out.println("Casilla [" + casilla.getCoordenada().getX() + ","
                            + casilla.getCoordenada().getY() + "] impactada: " + impactada));

            boolean hundida = ubicacionNave.getCasillasOcupadas().values().stream()
                    .allMatch(impactada -> impactada);
            
            System.out.println("¿Nave hundida? " + hundida);

            if (hundida) {
                System.out.println("Nave hundida detectada. Estado de casillas:");
                ubicacionNave.getCasillasOcupadas().forEach((casilla, impactada)
                        -> System.out.println("Casilla [" + casilla.getCoordenada().getX() + ","
                                + casilla.getCoordenada().getY() + "] impactada: " + impactada));
                List<int[]> casillasNave = new ArrayList<>();
                for (Casilla casilla : ubicacionNave.getCasillasOcupadas().keySet()) {
                    casillasNave.add(new int[]{casilla.getCoordenada().getX(), casilla.getCoordenada().getY()});
                    System.out.println("Agregando casilla hundida: [" + casilla.getCoordenada().getY()
                            + "," + casilla.getCoordenada().getX() + "]");
                }
                return new ResultadoDisparoInfo(ResultadoDisparo.HUNDIDO, casillasNave);
            }

            return new ResultadoDisparoInfo(ResultadoDisparo.IMPACTO, null);
        } catch (Exception e) {
            System.out.println("Error en realizarDisparo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void actualizarEstadoNaves(Jugador jugador, ResultadoDisparo resultado) {
        switch (resultado) {
            case IMPACTO:
                jugador.incrementarNavesDanadas();
                break;
            case HUNDIDO:
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

    private void registrarDisparo(Partida partida, int fila, int columna, String idJugador, ResultadoDisparoInfo resultadoInfo) {
        try {
            Jugador jugadorObjetivo = obtenerOponente(partida, idJugador);
            Tablero tablero = jugadorObjetivo.getTablero();

            if (tablero.getDisparos() == null) {
                tablero.setDisparos(new ArrayList<>());
            }

            Coordenada coordenada = new Coordenada(columna, fila);
            Disparo disparo = new Disparo(coordenada, resultadoInfo.resultado);

            System.out.println("Registrando disparo en [" + fila + "," + columna + "] con resultado: " + resultadoInfo.resultado);
            tablero.getDisparos().add(disparo);

            // Si la nave fue hundida, registrar también todas las casillas hundidas
            if (resultadoInfo.resultado == ResultadoDisparo.HUNDIDO && resultadoInfo.casillasHundidas != null) {
                for (int[] casilla : resultadoInfo.casillasHundidas) {
                    if (casilla[0] != fila || casilla[1] != columna) {
                        Coordenada coordenadaHundida = new Coordenada(casilla[1], casilla[0]);
                        Disparo disparoHundido = new Disparo(coordenadaHundida, ResultadoDisparo.HUNDIDO);
                        tablero.getDisparos().add(disparoHundido);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al registrar disparo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class ResultadoDisparoInfo {

        final ResultadoDisparo resultado;
        final List<int[]> casillasHundidas;

        ResultadoDisparoInfo(ResultadoDisparo resultado, List<int[]> casillasHundidas) {
            this.resultado = resultado;
            this.casillasHundidas = casillasHundidas;
        }
    }
}
