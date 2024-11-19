package com.mycompany.battleshippresentacion.modelo;

import java.util.ArrayList;
import java.util.List;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoPartida;

public class ModeloPartida {

    private String codigoSala;
    private EstadoPartida estado;
    private List<ModeloJugador> jugadores;
    private ModeloJugador jugadorEnTurno;

    public ModeloPartida() {
        this.jugadores = new ArrayList<>();
        this.estado = EstadoPartida.ESPERANDO;
    }

    public String getCodigoSala() {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala) {
        this.codigoSala = codigoSala;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    public List<ModeloJugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public void setJugadores(List<ModeloJugador> jugadores) {
        this.jugadores = new ArrayList<>(jugadores);
    }

    public ModeloJugador getJugadorEnTurno() {
        return jugadorEnTurno;
    }

    public void setJugadorEnTurno(ModeloJugador jugadorEnTurno) {
        this.jugadorEnTurno = jugadorEnTurno;
    }
}
