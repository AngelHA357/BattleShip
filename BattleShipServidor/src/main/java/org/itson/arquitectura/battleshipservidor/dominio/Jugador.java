package org.itson.arquitectura.battleshipservidor.dominio;

import org.itson.arquitectura.battleshipservidor.dominio.Tablero.Tablero;
import org.itson.arquitectura.battleshiptransporte.enums.Color;

/**
 *
 * @author victo
 */
public class Jugador {

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

    public void incrementarNavesDanadas() {
        navesIntactas--;
        navesDanadas++;
    }

    public void incrementarNavesDestruidas() {
        navesDanadas--;
        navesDestruidas++;
    }

    public boolean esCasillaImpactada(int x, int y) {
        return tablero.getDisparos().stream()
                .anyMatch(d -> d.getCoordenada().getX() == x
                && d.getCoordenada().getY() == y);
    }
}
