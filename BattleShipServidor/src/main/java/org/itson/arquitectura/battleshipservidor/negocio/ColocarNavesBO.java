/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.arquitectura.battleshipservidor.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.itson.arquitectura.battleshipservidor.dominio.Coordenada;
import org.itson.arquitectura.battleshipservidor.dominio.Tablero.Tablero;
import org.itson.arquitectura.battleshipservidor.dominio.UbicacionNave;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.CasillaFlyweightFactory;
import org.itson.arquitectura.battleshipservidor.dominio.nave.Nave;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoCasilla;

/**
 *
 * @author JoseH
 */
public class ColocarNavesBO {

    private Tablero tablero;
    private CasillaFlyweightFactory flyweightFactory;

    public ColocarNavesBO(int alto, int ancho) {
        this.flyweightFactory = new CasillaFlyweightFactory();
        inicializarTablero(alto, ancho);
    }

    private void inicializarTablero(int alto, int ancho) {
        tablero = new Tablero(alto, ancho);

        // Crear casillas con estado LIBRE por defecto
        List<Casilla> casillas = new ArrayList<>();
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                Coordenada coordenada = new Coordenada(x, y);
                Casilla casilla = new Casilla(coordenada, flyweightFactory.getFlyweight(EstadoCasilla.LIBRE));
                casillas.add(casilla);
            }
        }
        tablero.setCasillas(casillas);
        tablero.setUbicacionesNave(new ArrayList<>());
    }

    public boolean colocarNave(Nave nave, int x, int y, String orientacion) {
        List<Casilla> casillasOcupadas = new ArrayList<>();

        // Validar si las casillas están disponibles
        for (int i = 0; i < nave.getTamano(); i++) {
            int xActual = orientacion.equals("HORIZONTAL") ? x + i : x;
            int yActual = orientacion.equals("VERTICAL") ? y + i : y;

            if (!esCoordenadaValida(xActual, yActual)) {
                return false; // Coordenada fuera del tablero
            }

            Casilla casilla = obtenerCasilla(xActual, yActual);
            if (casilla.getEstado() != EstadoCasilla.LIBRE) {
                return false; // Casilla ocupada
            }
            casillasOcupadas.add(casilla);
        }

        // Marcar casillas como ocupadas
        for (Casilla casilla : casillasOcupadas) {
            casilla.setEstado(EstadoCasilla.OCUPADA);
        }

        // Registrar la ubicación de la nave
        UbicacionNave ubicacionNave = new UbicacionNave();
        ubicacionNave.setNave(nave);

        Map<Casilla, Boolean> mapaCasillas = new HashMap<>();
        for (Casilla casilla : casillasOcupadas) {
            mapaCasillas.put(casilla, true);
        }
        ubicacionNave.setCasillasOcupadas(mapaCasillas);

        tablero.getUbicacionesNave().add(ubicacionNave);
        return true; 
    }

    private boolean esCoordenadaValida(int x, int y) {
        return x >= 0 && x < tablero.getAncho() && y >= 0 && y < tablero.getAlto();
    }

    private Casilla obtenerCasilla(int x, int y) {
        int indice = y * tablero.getAncho() + x;
        return tablero.getCasillas().get(indice);
    }
}
