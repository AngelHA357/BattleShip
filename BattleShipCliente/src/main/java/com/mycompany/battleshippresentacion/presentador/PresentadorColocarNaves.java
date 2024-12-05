/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.ivista.IVistaColocarNaves;
import com.mycompany.battleshippresentacion.modelo.ClienteNave;
import com.mycompany.battleshippresentacion.modelo.ClienteTablero;
import com.mycompany.battleshippresentacion.vista.PantallaColocarNaves;
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
public class PresentadorColocarNaves implements SocketCliente.EventoListener {

    private final IVistaColocarNaves vista;
    private final PresentadorPrincipal navegacion;
    private ClienteTablero clienteTablero;
    private ClienteNave clienteNave;
    private String naveSeleccionada;
    private int orientacionActual;
    private final SocketCliente socketCliente;
    private String idJugador;

    private volatile boolean esperandoRespuesta = false;
    private final Object lock = new Object();
    private volatile Exception errorConexion = null;
    private volatile boolean respuestaRecibida = false;
    private volatile boolean tableroInicializado = false;
    private PresentadorJugador presentadorJugador;
    
    public PresentadorColocarNaves(PantallaColocarNaves vista, PresentadorPrincipal navegacion, PresentadorJugador presentadorJugador) {
        this.vista = vista;
        orientacionActual = 0;
        socketCliente = SocketCliente.getInstance();
        this.navegacion = navegacion;
        this.idJugador = presentadorJugador.getModeloJugador().getId();
        this.socketCliente.setEventoListener(this);
        this.presentadorJugador = presentadorJugador;

        try {
            if (!socketCliente.estaConectado() && !socketCliente.conectar("localhost")) {
                throw new Exception("No se pudo conectar al servidor");
            }
        } catch (Exception e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }

        clienteTablero = new ClienteTablero(10, 10, new int[10][10]);
    }

    public void crearNaves(){
        String color = presentadorJugador.getModeloJugador().getColor();
        
        vista.crearNaves(color);
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
                if (casillas[i][j].getIcon() == vista.getBarco1Icon()) {
                    casillasModelo[i][j] = 1;
                } else if (casillas[i][j].getIcon() == vista.getBarco2Icon()) {
                    casillasModelo[i][j] = 2;
                } else if (casillas[i][j].getIcon() == vista.getBarco3Icon()) {
                    casillasModelo[i][j] = 3;
                } else if (casillas[i][j].getIcon() == vista.getBarco4Icon()) {
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
        data.put("idJugador", idJugador);
        EventoDTO eventoDTO = new EventoDTO(Evento.JUGADOR_LISTO, data);

        synchronized (lock) {
            esperandoRespuesta = true;
            socketCliente.enviarEvento(eventoDTO);

            try {
                lock.wait(100000);
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
                String jugadorEnTurno = (String) datos.get("jugadorEnTurno");
                
                String nombreRival = (String) datos.get("jugadorRival");
                presentadorJugador.getModeloJugador().setNombreRival(nombreRival);

                boolean esTurnoPropio = jugadorEnTurno.equals(idJugador);
                synchronized (lock) {
                    esperandoRespuesta = false;
                    lock.notify();
                }

                try {
                    navegacion.setPresentadorJugador(presentadorJugador);
                    navegacion.mostrarPantallaJugarPartida(esTurnoPropio, this);
                } catch (Exception e) {
                    System.out.println("Error al navegar: " + e.getMessage());
                    e.printStackTrace();
                }
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
