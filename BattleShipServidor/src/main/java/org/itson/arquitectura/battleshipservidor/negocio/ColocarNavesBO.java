/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.arquitectura.battleshipservidor.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    private static final Map<String, Tablero> tablerosJugadores = new HashMap<>();
    private static ColocarNavesBO instance;
    private CasillaFlyweightFactory flyweightFactory;

    private ColocarNavesBO() {
        this.flyweightFactory = new CasillaFlyweightFactory();
    }
    
     public static synchronized ColocarNavesBO getInstance() {
        if (instance == null) {
            instance = new ColocarNavesBO();
        }
        return instance;
    }

    
    public EventoDTO inicializarTablero(String idJugador, int alto, int ancho) {
        Tablero nuevoTablero = new Tablero(alto, ancho);
        
        List<Casilla> casillas = new ArrayList<>();
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                Coordenada coordenada = new Coordenada(x, y);
                Casilla casilla = new Casilla(coordenada, flyweightFactory.getFlyweight(EstadoCasilla.LIBRE));
                casillas.add(casilla);
            }
        }
        nuevoTablero.setCasillas(casillas);
        nuevoTablero.setUbicacionesNave(new ArrayList<>());

        System.out.println("ID usado para inicializar: " + idJugador);
        tablerosJugadores.put(idJugador, nuevoTablero);
        
        Map<String, Object> datosRespuesta = new HashMap<>();
        datosRespuesta.put("exitoso", true);
        EventoDTO respuesta = new EventoDTO(Evento.INICIALIZAR_TABLERO, datosRespuesta);
        
        return respuesta;
    }

    public EventoDTO colocarNave(String idJugador, int tamano, int x, int y, String orientacion) {
        Tablero tableroJugador = tablerosJugadores.get(idJugador);
        System.out.println("ID usado para colocar nave: " + idJugador);
        
        if (tableroJugador == null) {
            Map<String, Object> datosError = new HashMap<>();
            datosError.put("exitoso", false);
            datosError.put("error", "Tablero no inicializado para el jugador");
            return new EventoDTO(Evento.COLOCAR_NAVES, datosError);
        }
        
        List<Casilla> casillasOcupadas = new ArrayList<>();

        // Validar si las casillas están disponibles
        for (int i = 0; i < tamano; i++) {
            int xActual = orientacion.equals("HORIZONTAL") ? x + i : x;
            int yActual = orientacion.equals("VERTICAL") ? y + i : y;

            Casilla casilla = obtenerCasilla(tableroJugador, xActual, yActual);
            casillasOcupadas.add(casilla);
        }

        // Marcar casillas como ocupadas
        for (Casilla casilla : casillasOcupadas) {
            casilla.setEstado(EstadoCasilla.OCUPADA);
        }

        Nave nave = crearNave(tamano);
        // Registrar la ubicación de la nave
        UbicacionNave ubicacionNave = new UbicacionNave();
        ubicacionNave.setNave(nave);

        Map<Casilla, Boolean> mapaCasillas = new HashMap<>();
        for (Casilla casilla : casillasOcupadas) {
            mapaCasillas.put(casilla, true);
        }
        ubicacionNave.setCasillasOcupadas(mapaCasillas);

        tableroJugador.getUbicacionesNave().add(ubicacionNave);
        
        Map<String, Object> datosRespuesta = new HashMap<>();
        
        datosRespuesta.put("exitoso", true);
        datosRespuesta.put("tipoNave", nave.getNombre());
        datosRespuesta.put("tamano", tamano);
        datosRespuesta.put("orientacion", orientacion);
        datosRespuesta.put("coordenadaX", x);
        datosRespuesta.put("coordenadaY", y);
        datosRespuesta.put("accion", "COLOCAR");
  
        EventoDTO respuesta = new EventoDTO(Evento.COLOCAR_NAVES, datosRespuesta);
        
        return respuesta; 
    }
    
    public EventoDTO limpiarNave(String idJugador, int tamano, int x, int y, String orientacion) {
        Tablero tableroJugador = tablerosJugadores.get(idJugador);
        System.out.println("ID usado para limpiar nave: " + idJugador);
        
        if (tableroJugador == null) {
            Map<String, Object> datosError = new HashMap<>();
            datosError.put("exitoso", false);
            datosError.put("error", "Tablero no inicializado para el jugador");
            return new EventoDTO(Evento.LIMPIAR_NAVES, datosError);
        }
        
        List<Casilla> casillasLimpiar = new ArrayList<>();
        
        // Obtener las casillas a limpiar
        for (int i = 0; i < tamano; i++) {
            int xActual = orientacion.equals("HORIZONTAL") ? x + i : x;
            int yActual = orientacion.equals("VERTICAL") ? y + i : y;
            
            Casilla casilla = obtenerCasilla(tableroJugador, xActual, yActual);
            casillasLimpiar.add(casilla);
        }
        
        // Marcar casillas como libres
        for (Casilla casilla : casillasLimpiar) {
            casilla.setEstado(EstadoCasilla.LIBRE);
        }
        
        // Eliminar la ubicación de la nave
        Iterator<UbicacionNave> iterator = tableroJugador.getUbicacionesNave().iterator();
        while (iterator.hasNext()) {
            UbicacionNave ubicacion = iterator.next();
            if (ubicacion.getCasillasOcupadas().keySet().containsAll(casillasLimpiar)) {
                iterator.remove();
                break;
            }
        }
        
        Map<String, Object> datosRespuesta = new HashMap<>();
        datosRespuesta.put("exitoso", true);
        datosRespuesta.put("accion", "LIMPIAR");
        datosRespuesta.put("tamano", tamano);
        datosRespuesta.put("orientacion", orientacion);
        datosRespuesta.put("coordenadaX", x);
        datosRespuesta.put("coordenadaY", y);
        
        System.out.println(datosRespuesta);
        return new EventoDTO(Evento.LIMPIAR_NAVES, datosRespuesta);
    }

    private Casilla obtenerCasilla(Tablero tablero, int x, int y) {
        int indice = y * tablero.getAncho() + x;
        return tablero.getCasillas().get(indice);
    }
    
    private Nave crearNave(int tamano) {
        return switch (tamano) {
            case 1 -> new BarcoFactory().createNave();
            case 2 -> new SubmarinoFactory().createNave();
            case 3 -> new CruceroFactory().createNave();
            case 4 -> new PortaAvionesFactory().createNave();
            default -> null;
        };
    }
}
