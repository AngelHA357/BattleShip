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
import org.itson.arquitectura.battleshipservidor.dominio.Disparo;
import org.itson.arquitectura.battleshipservidor.dominio.Jugador;
import org.itson.arquitectura.battleshipservidor.dominio.Partida;
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

    private int[][] obtenerMatrizTablero(Tablero tablero) {
        int[][] matrizTablero = new int[tablero.getAlto()][tablero.getAncho()];
        for (int i = 0; i < tablero.getAlto(); i++) {
            for (int j = 0; j < tablero.getAncho(); j++) {
                Casilla casilla = obtenerCasilla(tablero, j, i);
                matrizTablero[i][j] = casilla.getEstado() == EstadoCasilla.OCUPADA ? 1 : 0;
            }
        }
        return matrizTablero;
    }

    public EventoDTO inicializarTablero(String idJugador, int alto, int ancho) {
        try {
            Tablero nuevoTablero = new Tablero(alto, ancho);
            List<Casilla> casillas = new ArrayList<>();
            List<Disparo> disparos = new ArrayList<>();  // Inicializar lista de disparos

            // Inicializar casillas
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    Coordenada coordenada = new Coordenada(x, y);
                    Casilla casilla = new Casilla(coordenada, flyweightFactory.getFlyweight(EstadoCasilla.LIBRE));
                    casillas.add(casilla);
                }
            }

            nuevoTablero.setCasillas(casillas);
            nuevoTablero.setUbicacionesNave(new ArrayList<>());
            nuevoTablero.setDisparos(disparos);  // Establecer lista de disparos

            // Obtener el jugador y asignarle el tablero
            Partida partida = Partida.getInstance();
            Jugador jugador = partida.getJugadores().stream()
                    .filter(j -> j.getId().equals(idJugador))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Jugador no encontrado"));

            jugador.setTablero(nuevoTablero);
            tablerosJugadores.put(idJugador, nuevoTablero);

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);
            datosRespuesta.put("tablero", obtenerMatrizTablero(nuevoTablero));

            return new EventoDTO(Evento.INICIALIZAR_TABLERO, datosRespuesta);
        } catch (Exception e) {
            Map<String, Object> datosError = new HashMap<>();
            datosError.put("exitoso", false);
            datosError.put("error", "Error al inicializar tablero: " + e.getMessage());
            return new EventoDTO(Evento.INICIALIZAR_TABLERO, datosError);
        }
    }

    public EventoDTO colocarNave(String idJugador, int tamano, int x, int y, String orientacion) {
        try {
            Tablero tableroJugador = tablerosJugadores.get(idJugador);
            if (tableroJugador == null) {
                throw new IllegalStateException("Tablero no inicializado para el jugador");
            }

            List<Casilla> casillasOcupadas = new ArrayList<>();

            // Validar posición
            for (int i = 0; i < tamano; i++) {
                int xActual = orientacion.equals("HORIZONTAL") ? x + i : x;
                int yActual = orientacion.equals("VERTICAL") ? y + i : y;

                if (xActual >= tableroJugador.getAncho() || yActual >= tableroJugador.getAlto()) {
                    throw new IllegalArgumentException("Posición fuera del tablero");
                }

                Casilla casilla = obtenerCasilla(tableroJugador, xActual, yActual);
                if (casilla.getEstado() == EstadoCasilla.OCUPADA) {
                    throw new IllegalStateException("Casilla ya ocupada");
                }
                casillasOcupadas.add(casilla);
            }

            // Colocar nave
            Nave nave = crearNave(tamano);
            UbicacionNave ubicacionNave = new UbicacionNave();
            ubicacionNave.setNave(nave);

            Map<Casilla, Boolean> mapaCasillas = new HashMap<>();
            for (Casilla casilla : casillasOcupadas) {
                casilla.setEstado(EstadoCasilla.OCUPADA);
                mapaCasillas.put(casilla, true);
            }
            ubicacionNave.setCasillasOcupadas(mapaCasillas);
            tableroJugador.getUbicacionesNave().add(ubicacionNave);

            // Preparar respuesta
            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);
            datosRespuesta.put("tipoNave", nave.getNombre());
            datosRespuesta.put("tamano", tamano);
            datosRespuesta.put("orientacion", orientacion);
            datosRespuesta.put("coordenadaX", x);
            datosRespuesta.put("coordenadaY", y);
            datosRespuesta.put("accion", "COLOCAR");
            datosRespuesta.put("tablero", obtenerMatrizTablero(tableroJugador));

            return new EventoDTO(Evento.COLOCAR_NAVES, datosRespuesta);

        } catch (Exception e) {
            Map<String, Object> datosError = new HashMap<>();
            datosError.put("exitoso", false);
            datosError.put("error", e.getMessage());
            return new EventoDTO(Evento.COLOCAR_NAVES, datosError);
        }
    }

    public EventoDTO limpiarNave(String idJugador, int tamano, int x, int y, String orientacion) {
        try {
            Tablero tableroJugador = tablerosJugadores.get(idJugador);
            if (tableroJugador == null) {
                throw new IllegalStateException("Tablero no inicializado para el jugador");
            }

            List<Casilla> casillasLimpiar = new ArrayList<>();

            // Obtener casillas a limpiar
            for (int i = 0; i < tamano; i++) {
                int xActual = orientacion.equals("HORIZONTAL") ? x + i : x;
                int yActual = orientacion.equals("VERTICAL") ? y + i : y;

                if (xActual >= tableroJugador.getAncho() || yActual >= tableroJugador.getAlto()) {
                    throw new IllegalArgumentException("Posición fuera del tablero");
                }

                Casilla casilla = obtenerCasilla(tableroJugador, xActual, yActual);
                casillasLimpiar.add(casilla);
            }

            // Limpiar casillas
            for (Casilla casilla : casillasLimpiar) {
                casilla.setEstado(EstadoCasilla.LIBRE);
            }

            // Eliminar ubicación de la nave
            tableroJugador.getUbicacionesNave().removeIf(ubicacion
                    -> ubicacion.getCasillasOcupadas().keySet().containsAll(casillasLimpiar));

            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);
            datosRespuesta.put("accion", "LIMPIAR");
            datosRespuesta.put("tamano", tamano);
            datosRespuesta.put("orientacion", orientacion);
            datosRespuesta.put("coordenadaX", x);
            datosRespuesta.put("coordenadaY", y);
            datosRespuesta.put("tablero", obtenerMatrizTablero(tableroJugador));

            return new EventoDTO(Evento.LIMPIAR_NAVES, datosRespuesta);

        } catch (Exception e) {
            Map<String, Object> datosError = new HashMap<>();
            datosError.put("exitoso", false);
            datosError.put("error", e.getMessage());
            return new EventoDTO(Evento.LIMPIAR_NAVES, datosError);
        }
    }

    private Casilla obtenerCasilla(Tablero tablero, int x, int y) {
        if (x < 0 || x >= tablero.getAncho() || y < 0 || y >= tablero.getAlto()) {
            throw new IllegalArgumentException("Coordenadas fuera del tablero");
        }
        int indice = y * tablero.getAncho() + x;
        return tablero.getCasillas().get(indice);
    }

    private Nave crearNave(int tamano) {
        return switch (tamano) {
            case 1 ->
                new BarcoFactory().createNave();
            case 2 ->
                new SubmarinoFactory().createNave();
            case 3 ->
                new CruceroFactory().createNave();
            case 4 ->
                new PortaAvionesFactory().createNave();
            default ->
                throw new IllegalArgumentException("Tamaño de nave no válido: " + tamano);
        };
    }
}
