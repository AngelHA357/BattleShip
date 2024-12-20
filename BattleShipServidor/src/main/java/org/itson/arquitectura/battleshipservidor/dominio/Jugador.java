package org.itson.arquitectura.battleshipservidor.dominio;

import java.io.Serializable;
import org.itson.arquitectura.battleshipservidor.dominio.Tablero.Tablero;
import org.itson.arquitectura.battleshiptransporte.enums.Color;

/**
 *
 * @author victo
 */
public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String nombre;
    private Color color;
    private Tablero tablero;
    private int navesIntactas;
    private int navesDanadas;
    private int navesDestruidas;
    private boolean listo;
    private static final int TOTAL_NAVES = 11;

    public Jugador() {
        this.navesIntactas = TOTAL_NAVES;
        this.navesDanadas = 0;
        this.navesDestruidas = 0;
        this.tablero = new Tablero(10, 10);
    }

    public Jugador(String nombre, Color color) {
        this.nombre = nombre;
        this.color = color;
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

    public boolean isListo() {
        return listo;
    }

    public void setListo(boolean listo) {
        this.listo = listo;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNavesIntactas() {
        return navesIntactas;
    }

    public int getNavesDanadas() {
        return navesDanadas;
    }

    public int getNavesDestruidas() {
        return navesDestruidas;
    }

    public int getTotalNaves() {
        return TOTAL_NAVES;
    }

    public boolean esCasillaImpactada(int x, int y) {
        return tablero.getDisparos().stream()
                .anyMatch(d -> d.getCoordenada().getX() == x
                && d.getCoordenada().getY() == y);
    }

    public void decrementarNavesIntactas() {
        if (navesIntactas > 0) {
            navesIntactas--;
        }
    }

    public void incrementarNavesDanadas() {
        if (navesDanadas + navesDestruidas < TOTAL_NAVES) {
            navesDanadas++;
        }
    }

    public void decrementarNavesDanadas() {
        if (navesDanadas > 0) {
            navesDanadas--;
        }
    }

    public void incrementarNavesDestruidas() {
        if (navesDestruidas < TOTAL_NAVES) {
            navesDestruidas++;
        }
    }
}
