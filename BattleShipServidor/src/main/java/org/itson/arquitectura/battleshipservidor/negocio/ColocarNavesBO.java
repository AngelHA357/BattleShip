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

    private Casilla obtenerCasilla(Tablero tablero, int x, int y) {
        if (x < 0 || x >= tablero.getAncho() || y < 0 || y >= tablero.getAlto()) {
            throw new IllegalArgumentException("Coordenadas fuera del tablero");
        }
        int indice = y * tablero.getAncho() + x;
        return tablero.getCasillas().get(indice);
    }

    public void inicializarTablero(String idJugador) {
        Tablero nuevoTablero = new Tablero(10, 10);
        List<Casilla> casillas = new ArrayList<>();
        List<Disparo> disparos = new ArrayList<>();

        // Inicializar casillas
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Coordenada coordenada = new Coordenada(x, y);
                Casilla casilla = new Casilla(coordenada, flyweightFactory.getFlyweight(EstadoCasilla.LIBRE));
                casillas.add(casilla);
            }
        }

        nuevoTablero.setCasillas(casillas);
        nuevoTablero.setUbicacionesNave(new ArrayList<>());
        nuevoTablero.setDisparos(disparos);

        // Obtener el jugador y asignarle el tablero
        Partida partida = Partida.getInstance();
        Jugador jugador = partida.getJugadores().stream()
                .filter(j -> j.getId().equals(idJugador))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Jugador no encontrado"));

        jugador.setTablero(nuevoTablero);
        tablerosJugadores.put(idJugador, nuevoTablero);
    }

    public EventoDTO crearTableroCompleto(String idJugador, int[][] casillas) {
        try {
            inicializarTablero(idJugador);
            Tablero tableroJugador = tablerosJugadores.get(idJugador);
            if (tableroJugador == null) {
                throw new IllegalStateException("Tablero no inicializado para el jugador");
            }

            // Mapa temporal para ir construyendo los barcos
            Map<Integer, UbicacionNave> barcosEnConstruccion = new HashMap<>();

            // Recorrer el tablero una sola vez
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (casillas[i][j] != 0) {
                        int tipoBarco = casillas[i][j];
                        int indice = i * tableroJugador.getAncho() + j;
                        Casilla casilla = tableroJugador.getCasillas().get(indice);
                        casilla.setEstado(EstadoCasilla.OCUPADA);

                        // Obtener o crear ubicación de nave para este tipo
                        UbicacionNave ubicacionNave = barcosEnConstruccion.computeIfAbsent(tipoBarco, k -> {
                            UbicacionNave nuevaUbicacion = new UbicacionNave();
                            nuevaUbicacion.setNave(crearNave(k));
                            nuevaUbicacion.setCasillasOcupadas(new HashMap<>());
                            tableroJugador.getUbicacionesNave().add(nuevaUbicacion);
                            return nuevaUbicacion;
                        });

                        // Agregar casilla a la ubicación
                        ubicacionNave.getCasillasOcupadas().put(casilla, false);
                    }
                }
            }
            
            Map<String, Object> datosRespuesta = new HashMap<>();
            datosRespuesta.put("exitoso", true);
            datosRespuesta.put("tablero", obtenerMatrizTablero(tableroJugador));
            return new EventoDTO(Evento.CREAR_TABLERO, datosRespuesta);

        } catch (Exception e) {
            Map<String, Object> datosError = new HashMap<>();
            datosError.put("exitoso", false);
            datosError.put("error", e.getMessage());
            return new EventoDTO(Evento.CREAR_TABLERO, datosError);
        }
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
