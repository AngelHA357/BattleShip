
package org.itson.arquitectura.battleshipservidor.dominio;

import org.itson.arquitectura.battleshipservidor.dominio.Tablero.Tablero;
import org.itson.arquitectura.battleshipservidor.dominio.enums.Color;

/**
 *
 * @author victo
 */
public class Jugador {
    
    private String nombre;
    private Color color;
    private Tablero tablero;
    private int tiempoTurno;

    public Jugador(String nombre, Color color, Tablero tablero, int tiempoTurno) {
        this.nombre = nombre;
        this.color = color;
        this.tablero = tablero;
        this.tiempoTurno = tiempoTurno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public int getTiempoTurno() {
        return tiempoTurno;
    }

    public void setTiempoTurno(int tiempoTurno) {
        this.tiempoTurno = tiempoTurno;
    }
    
    
    
}
