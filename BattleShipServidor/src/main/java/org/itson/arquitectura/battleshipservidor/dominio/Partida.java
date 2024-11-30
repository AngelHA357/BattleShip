package org.itson.arquitectura.battleshipservidor.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.Serializable;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoPartida;
import org.itson.arquitectura.battleshiptransporte.enums.ResultadoDisparo;

/**
 * Clase que representa una partida del juego. Implementa el patrón Singleton y
 * manejo thread-safe de sus propiedades.
 */
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;

    // Lock específico para el singleton
    private static final Object INSTANCE_LOCK = new Object();

    // Lock para sincronización de operaciones
    private final Object stateLock = new Object();

    private static volatile Partida instance;

    // Uso de volatile para visibilidad entre hilos
    private volatile String codigoSala;
    private volatile EstadoPartida estado;
    private final List<Jugador> jugadores;
    private volatile Jugador jugadorEnTurno;

    private Partida() {
        this.jugadores = new CopyOnWriteArrayList<>();
        this.estado = EstadoPartida.ESPERANDO;
    }

    public static Partida getInstance() {
        Partida result = instance;
        if (result == null) {
            synchronized (INSTANCE_LOCK) {
                result = instance;
                if (result == null) {
                    instance = result = new Partida();
                }
            }
        }
        return result;
    }

    public static void reset() {
        synchronized (INSTANCE_LOCK) {
            instance = null;
        }
    }

    public String getCodigoSala() {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala) {
        synchronized (stateLock) {
            this.codigoSala = codigoSala;
        }
    }

    public List<Jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public void setJugadores(List<Jugador> jugadores) {
        synchronized (stateLock) {
            this.jugadores.clear();
            if (jugadores != null) {
                this.jugadores.addAll(jugadores);
            }
        }
    }

    public void agregarJugador(Jugador jugador) {
        if (jugador != null) {
            synchronized (stateLock) {
                if (!this.jugadores.contains(jugador)) {
                    this.jugadores.add(jugador);
                }
            }
        }
    }

    public void removerJugador(Jugador jugador) {
        if (jugador != null) {
            synchronized (stateLock) {
                this.jugadores.remove(jugador);
                if (jugadorEnTurno != null && jugadorEnTurno.equals(jugador)) {
                    jugadorEnTurno = jugadores.isEmpty() ? null : jugadores.get(0);
                }
            }
        }
    }

    public Jugador getJugadorEnTurno() {
        synchronized (stateLock) {
            return jugadorEnTurno;
        }
    }

    public void setJugadorEnTurno(Jugador jugadorEnTurno) {
        synchronized (stateLock) {
            this.jugadorEnTurno = jugadorEnTurno;
        }
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {
        synchronized (stateLock) {
            this.estado = estado;
        }
    }

    public boolean esTurnoJugador(Jugador jugador) {
        synchronized (stateLock) {
            return jugadorEnTurno != null && jugadorEnTurno.equals(jugador);
        }
    }

    public void cambiarTurno() {
        synchronized (stateLock) {
            if (!jugadores.isEmpty()) {
                int indiceActual = jugadores.indexOf(jugadorEnTurno);
                int siguienteIndice = (indiceActual + 1) % jugadores.size();
                jugadorEnTurno = jugadores.get(siguienteIndice);
            }
        }
    }

    public boolean esCasillaDisparada(Jugador jugadorObjetivo, int x, int y) {
        List<Disparo> disparosRealizados = jugadorObjetivo.getTablero().getDisparos();
        System.out.println("Verificando si casilla [" + x + "," + y + "] ya fue disparada");
        boolean yaDisparada = disparosRealizados.stream()
                .anyMatch(d -> {
                    boolean coincide = d.getCoordenada().getX() == y
                            && d.getCoordenada().getY() == x;
                    if (coincide) {
                        System.out.println("Casilla [" + x + "," + y + "] ya fue disparada");
                    }
                    return coincide;
                });
        return yaDisparada;
    }

    public void registrarDisparo(String idJugador, int x, int y, String resultado) {
        synchronized (stateLock) {
            Jugador jugadorObjetivo = jugadores.stream()
                    .filter(j -> !j.getId().equals(idJugador))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Jugador objetivo no encontrado"));

            ResultadoDisparo resultadoEnum = ResultadoDisparo.valueOf(resultado);
            Coordenada coordenada = new Coordenada(x, y);
            Disparo disparo = new Disparo(coordenada, resultadoEnum);

            jugadorObjetivo.getTablero().getDisparos().add(disparo);
        }
    }

    public Jugador getJugadorActual() {
        synchronized (stateLock) {
            return jugadorEnTurno;
        }
    }
}
