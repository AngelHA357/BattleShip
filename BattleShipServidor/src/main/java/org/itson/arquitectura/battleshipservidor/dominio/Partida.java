package org.itson.arquitectura.battleshipservidor.dominio;

import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoPartida;
import java.util.List;

/**
 *
 * @author victo
 */
public class Partida {

    private static Partida instance;
    private String codigoSala;
    private List<Jugador> jugadores;
    private Jugador jugadorEnTurno;
    private EstadoPartida estado;

    private Partida() {
    }
    
    public static Partida getInstance(){
        if (instance == null) {
            instance = new Partida();
        }
        return instance;
    }

    public String getCodigoSala() {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala) {
        this.codigoSala = codigoSala;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Jugador getJugadorEnTurno() {
        return jugadorEnTurno;
    }

    public void setJugadorEnTurno(Jugador jugadorEnTurno) {
        this.jugadorEnTurno = jugadorEnTurno;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

}
