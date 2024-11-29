/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.modelo.ClienteNave;
import com.mycompany.battleshippresentacion.modelo.ClienteTablero;
import com.mycompany.battleshippresentacion.vista.PantallaColocarBarcos;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

/**
 *
 * @author JoseH
 */
public class ColocarBarcosPresentador implements SocketCliente.EventoListener {

    private final PantallaColocarBarcos vista;
    private PresentadorPrincipal navegacion = null;
    private ClienteTablero clienteTablero;
    private ClienteNave clienteNave;
    private String naveSeleccionada;
    private int orientacionActual;
    private final SocketCliente socketCliente;
    private String idJugador;
    private List<ClienteNave> naves;

    private volatile boolean esperandoRespuesta = false;
    private final Object lock = new Object();
    private volatile Exception errorConexion = null;
    private volatile boolean respuestaRecibida = false;
    private volatile boolean tableroInicializado = false;

    public ColocarBarcosPresentador(PantallaColocarBarcos vista, PresentadorPrincipal navegacion, String idJugador) {
        this.vista = vista;
        orientacionActual = 0;
        socketCliente = SocketCliente.getInstance();
        this.navegacion = navegacion;
        this.idJugador = idJugador;
        this.socketCliente.setEventoListener(this);

        try {
            if (!socketCliente.estaConectado() && !socketCliente.conectar("localhost")) {
                throw new Exception("No se pudo conectar al servidor");
            }
        } catch (Exception e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
        
        clienteTablero = new ClienteTablero(10, 10, new int[10][10]);
    }

    public void inicializarJuego() throws Exception {
        vista.crearTablero();
    }

    public void seleccionarNave(String tipoNave) {
        this.naveSeleccionada = tipoNave;
    }

    public boolean enviarTableroCompleto(JButton[][] casillas) throws Exception {
        int casillasModelo[][] = clienteTablero.getCasillas();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (casillas[i][j].getIcon() == vista.getBarco1Icon()){
                    casillasModelo[i][j] = 1;
                } else if (casillas[i][j].getIcon() == vista.getBarco2Icon()){
                    casillasModelo[i][j] = 2;
                } else if (casillas[i][j].getIcon() == vista.getBarco3Icon()){
                    casillasModelo[i][j] = 3;
                } else if (casillas[i][j].getIcon() == vista.getBarco4Icon()){
                    casillasModelo[i][j] = 4;
                }
            }
        }
        
        Map<String, Object> dataTablero = new HashMap<>();
        dataTablero.put("tablero", casillasModelo);
        
        EventoDTO eventoDTO = new EventoDTO(Evento.CREAR_TABLERO, dataTablero);
        synchronized (lock) {
            esperandoRespuesta = true;
            socketCliente.enviarEvento(eventoDTO);
            try {
                lock.wait(5000);
            } catch (InterruptedException e) {
                throw new Exception("Interrupción mientras se esperaba respuesta de creación del tablero", e);
            }
            if (errorConexion != null) {
                throw errorConexion;
            }
            if (esperandoRespuesta) {
                throw new Exception("Timeout al esperar respuesta de creación del tablero");
            }
        }

        return true;
        
    }

    public void confirmarColocacion() throws Exception {

        Map<String, Object> data = new HashMap<>();
        EventoDTO eventoDTO = new EventoDTO(Evento.JUGADOR_LISTO, data);

        synchronized (lock) {
            esperandoRespuesta = true;
            socketCliente.enviarEvento(eventoDTO);

            try {
                lock.wait(5000);
            } catch (InterruptedException e) {
                throw new Exception("Interrupción mientras se esperaba respuesta del servidor", e);
            }

            if (errorConexion != null) {
                throw errorConexion;
            }
        }
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
        if (evento.getEvento().equals(Evento.CREAR_TABLERO)) {
            try {
                System.out.println("Recibida respuesta de creación del tablero para jugador: " + evento.getIdJugador());
                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    throw new Exception("Datos del evento son null");
                }

                synchronized (lock) {
                    if (datos.containsKey("tablero")) {
                        int[][] matrizTablero = (int[][]) datos.get("tablero");
                        clienteTablero = new ClienteTablero(10, 10, matrizTablero);
                        System.out.println(clienteTablero);
                        System.out.println(matrizTablero);
                        System.out.println("Tablero creado con éxito");
                        respuestaRecibida = true;
                        esperandoRespuesta = false;
                        tableroInicializado = true;
                        lock.notifyAll();
                    } else {
                        throw new Exception("Datos del tablero no encontrados en la respuesta");
                    }
                }
            } catch (Exception e) {
                synchronized (lock) {
                    errorConexion = e;
                    respuestaRecibida = false;
                    esperandoRespuesta = false;
                    lock.notifyAll();
                    System.out.println("Error en inicialización de tablero: " + e.getMessage());
                }
            }
        } else if (evento.getEvento().equals(Evento.JUGADOR_LISTO)) {
            Map<String, Object> datos = evento.getDatos();

            if (datos.containsKey("partidaIniciada") && (boolean) datos.get("partidaIniciada")) {
                // Inicializar la pantalla de juego con el turno inicial
                String jugadorEnTurno = (String) datos.get("jugadorEnTurno");
                System.out.println("ID Jugador actual: " + idJugador);
                System.out.println("Estado tablero antes de cambiar pantalla: " + (clienteTablero == null? "null" : "no null"));
                boolean esTurnoPropio = true; //jugadorEnTurno.equals(idJugador);
                synchronized (lock) {
                    esperandoRespuesta = false;
                    lock.notify();
                }
                navegacion.mostrarPantallaJugarPartida(esTurnoPropio, this);

            }

            synchronized (lock) {
                esperandoRespuesta = false;
                lock.notify();
            }
        }

    }

    public ClienteTablero getClienteTablero() {
        return clienteTablero;
    }

    public void setClienteTablero(ClienteTablero clienteTablero) {
        this.clienteTablero = clienteTablero;
    }
}
