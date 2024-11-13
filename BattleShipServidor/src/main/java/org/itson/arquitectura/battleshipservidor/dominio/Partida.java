
package org.itson.arquitectura.battleshipservidor.dominio;

import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoPartida;
import java.util.List;

/**
 *
 * @author victo
 */
public class Partida {
    
    private String codigoSala;
    private List<Jugador> jugadores;
    private Jugador jugadorEnTurno;
    private EstadoPartida estado;

    public Partida(String codigoSala, List<Jugador> jugadores, Jugador jugadorEnTurno, EstadoPartida estado) {
        this.codigoSala = codigoSala;
        this.jugadores = jugadores;
        this.jugadorEnTurno = jugadorEnTurno;
        this.estado = estado;
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
