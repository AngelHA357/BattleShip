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
import org.itson.arquitectura.battleshipservidor.dominio.nave.BarcoFactory;
import org.itson.arquitectura.battleshipservidor.dominio.nave.CruceroFactory;
import org.itson.arquitectura.battleshipservidor.dominio.nave.Nave;
import org.itson.arquitectura.battleshipservidor.dominio.nave.PortaAvionesFactory;
import org.itson.arquitectura.battleshipservidor.dominio.nave.SubmarinoFactory;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoCasilla;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

/**
 *
 * @author JoseH
 */
public class ColocarNavesBO {

    private Tablero tablero;
    private CasillaFlyweightFactory flyweightFactory;

    public ColocarNavesBO() {
        this.flyweightFactory = new CasillaFlyweightFactory();
    }

    
    public EventoDTO inicializarTablero(int alto, int ancho) {
        tablero = new Tablero(alto, ancho);
        
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
        
        Map<String, Object> datosRespuesta = new HashMap<>();
        datosRespuesta.put("exitoso", true);
        EventoDTO respuesta = new EventoDTO(Evento.INICIALIZAR_TABLERO, datosRespuesta);
        
        return respuesta;
    }

    public EventoDTO colocarNave(int tamano, int x, int y, String orientacion) {
        List<Casilla> casillasOcupadas = new ArrayList<>();

        // Validar si las casillas están disponibles
        for (int i = 0; i < tamano; i++) {
            int xActual = orientacion.equals("HORIZONTAL") ? x + i : x;
            int yActual = orientacion.equals("VERTICAL") ? y + i : y;

            if (!esCoordenadaValida(xActual, yActual)) {
                return null; // Coordenada fuera del tablero
            }

            Casilla casilla = obtenerCasilla(xActual, yActual);
            if (casilla.getEstado() != EstadoCasilla.LIBRE) {
                return null; // Casilla ocupada
            }
            casillasOcupadas.add(casilla);
        }

        // Marcar casillas como ocupadas
        for (Casilla casilla : casillasOcupadas) {
            casilla.setEstado(EstadoCasilla.OCUPADA);
        }

        Nave nave = null;
        if (tamano == 1){
            BarcoFactory barcoFactory = new BarcoFactory();
            nave = barcoFactory.createNave();
        } else if (tamano == 2){
            SubmarinoFactory submarinoFactory = new SubmarinoFactory();
            nave = submarinoFactory.createNave();
        } else if (tamano == 3){
            CruceroFactory cruceroFactory = new CruceroFactory();
            nave = cruceroFactory.createNave();
        } else if (tamano == 4){
            PortaAvionesFactory portaAvionesFactory = new PortaAvionesFactory();
            nave = portaAvionesFactory.createNave();
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
        
        Map<String, Object> datosRespuesta = new HashMap<>();
        
        EventoDTO respuesta = new EventoDTO(Evento.COLOCAR_NAVES, datosRespuesta);
        
        datosRespuesta.put("tipoNave", nave.getNombre());
        datosRespuesta.put("tamano", tamano);
        datosRespuesta.put("orientacion", orientacion);
        datosRespuesta.put("coordenadaX", x);
        datosRespuesta.put("coordenadaY", y);
        return respuesta; 
    }

    private boolean esCoordenadaValida(int x, int y) {
        return x >= 0 && x < tablero.getAncho() && y >= 0 && y < tablero.getAlto();
    }

    private Casilla obtenerCasilla(int x, int y) {
        int indice = y * tablero.getAncho() + x;
        return tablero.getCasillas().get(indice);
    }
}
