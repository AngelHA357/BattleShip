package org.itson.arquitectura.battleshipservidor.dominio;

import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoPartida;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.Serializable;

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
        // Inicializar las colecciones thread-safe
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

    // Reset para pruebas o nuevo juego
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
        // Retorna una copia defensiva de la lista
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

}
