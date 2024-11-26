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

    public ColocarBarcosPresentador(PantallaColocarBarcos vista, PresentadorPrincipal navegacion, String idJugador) {
        this.vista = vista;
        orientacionActual = 0;
        socketCliente = SocketCliente.getInstance();
        socketCliente.setEventoListener(this);
        this.navegacion = navegacion;
        this.idJugador = idJugador;
    }

    public void inicializarJuego() throws Exception {
        if (inicializarTablero()) {
            vista.crearTablero();
        }
    }

    public void seleccionarNave(String tipoNave) {
        this.naveSeleccionada = tipoNave;
    }

    public boolean enviarColocacionNave(int fila, int columna, int orientacion, int tamano) throws Exception {
        String orientacionString;

        if (orientacion == 0) {
            orientacionString = "HORIZONTAL";
        } else {
            orientacionString = "VERTICAL";
        }

        Map<String, Object> data = new HashMap<>();
        data.put("coordenadaX", columna);
        data.put("coordenadaY", fila);
        data.put("orientacion", orientacionString);
        data.put("tamano", tamano);

        EventoDTO eventoDTO = new EventoDTO(Evento.COLOCAR_NAVES, data);

        synchronized (lock) {
            esperandoRespuesta = true;
            socketCliente.enviarEvento(eventoDTO);

            try {
                lock.wait(5000); // 5 segundos de timeout
            } catch (InterruptedException e) {
                throw new Exception("Interrupción mientras se esperaba respuesta del servidor", e);
            }

            if (errorConexion != null) {
                throw errorConexion;
            }

            if (esperandoRespuesta) {
                throw new Exception("Timeout al esperar respuesta del servidor");
            }
        }

        return true;
    }

    public boolean enviarRotacionNave(int fila, int columna, int orientacionNueva, int tamano, int orientacionPrevia) throws Exception {
        // Convertir orientaciones a strings
        String orientacionStringPrevia = (orientacionPrevia == 0) ? "HORIZONTAL" : "VERTICAL";
        String orientacionStringNueva = (orientacionNueva == 0) ? "HORIZONTAL" : "VERTICAL";

        System.out.println(orientacionStringPrevia + " y la nueva es " + orientacionStringNueva);

        // Primero limpiamos la nave en su posición actual
        Map<String, Object> dataLimpieza = new HashMap<>();
        dataLimpieza.put("coordenadaX", columna);
        dataLimpieza.put("coordenadaY", fila);
        dataLimpieza.put("orientacion", orientacionStringPrevia);
        dataLimpieza.put("tamano", tamano);

        EventoDTO eventoDTOLimpieza = new EventoDTO(Evento.LIMPIAR_NAVES, dataLimpieza);
        synchronized (lock) {
            esperandoRespuesta = true;
            socketCliente.enviarEvento(eventoDTOLimpieza);
            try {
                lock.wait(5000);
            } catch (InterruptedException e) {
                throw new Exception("Interrupción mientras se esperaba respuesta de limpieza", e);
            }
            if (errorConexion != null) {
                throw errorConexion;
            }
            if (esperandoRespuesta) {
                throw new Exception("Timeout al esperar respuesta de limpieza");
            }
        }

        // Luego colocamos la nave en su nueva orientación
        Map<String, Object> dataColocacion = new HashMap<>();
        dataColocacion.put("coordenadaX", fila);
        dataColocacion.put("coordenadaY", columna);
        dataColocacion.put("orientacion", orientacionStringNueva);
        dataColocacion.put("tamano", tamano);

        EventoDTO eventoDTOColocacion = new EventoDTO(Evento.COLOCAR_NAVES, dataColocacion);

        synchronized (lock) {
            esperandoRespuesta = true;
            socketCliente.enviarEvento(eventoDTOColocacion);
            try {
                lock.wait(5000);
            } catch (InterruptedException e) {
                throw new Exception("Interrupción mientras se esperaba respuesta de colocación", e);
            }
            if (errorConexion != null) {
                throw errorConexion;
            }
            if (esperandoRespuesta) {
                throw new Exception("Timeout al esperar respuesta de colocación");
            }
        }

        return true;
    }

    public boolean enviarEliminacionNave(int fila, int columna, int orientacion, int tamano) throws Exception {
        // Convertir orientaciones a strings
        String orientacionStringPrevia = (orientacion == 0) ? "HORIZONTAL" : "VERTICAL";
        String orientacionStringNueva = (orientacion == 0) ? "HORIZONTAL" : "VERTICAL";

        System.out.println(orientacionStringPrevia + " y la nueva es " + orientacionStringNueva);

        // Primero limpiamos la nave en su posición actual
        Map<String, Object> dataLimpieza = new HashMap<>();
        dataLimpieza.put("coordenadaX", columna);
        dataLimpieza.put("coordenadaY", fila);
        dataLimpieza.put("orientacion", orientacionStringPrevia);
        dataLimpieza.put("tamano", tamano);

        EventoDTO eventoDTOLimpieza = new EventoDTO(Evento.LIMPIAR_NAVES, dataLimpieza);
        synchronized (lock) {
            esperandoRespuesta = true;
            socketCliente.enviarEvento(eventoDTOLimpieza);
            try {
                lock.wait(5000);
            } catch (InterruptedException e) {
                throw new Exception("Interrupción mientras se esperaba respuesta de limpieza", e);
            }
            if (errorConexion != null) {
                throw errorConexion;
            }
            if (esperandoRespuesta) {
                throw new Exception("Timeout al esperar respuesta de limpieza");
            }
        }

        return true;
    }

    public boolean inicializarTablero() throws Exception {
        if (!socketCliente.conectar("localhost")) {
            throw new Exception("No se pudo conectar al servidor");
        }

        Map<String, Object> data = new HashMap<>();
        EventoDTO eventoDTO = new EventoDTO(Evento.INICIALIZAR_TABLERO, data);

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

            if (esperandoRespuesta) {
                throw new Exception("Timeout al esperar respuesta del servidor");
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
        if (evento.getEvento().equals(Evento.COLOCAR_NAVES)) {
            try {
                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    errorConexion = new Exception("Datos del evento son null");
                    return;
                }

                System.out.println("Datos recibidos: " + datos);

                if (datos.containsKey("tablero")) {
                    int[][] matrizTablero = (int[][]) datos.get("tablero");
                    clienteTablero = new ClienteTablero(10, 10, matrizTablero);
                }

                // Crear la nave cliente con los datos recibidos
//                clienteNave = new ClienteNave(
//                        (String) datos.get("tipoNave"),
//                        (int) datos.get("tamano"), (String) datos.get("orientacion"),
//                        "SIN_DAÑOS" // Estado inicial de la nave
//                );

                synchronized (lock) {
                    esperandoRespuesta = false;
                    lock.notify();
                }
            } catch (Exception e) {
                synchronized (lock) {
                    errorConexion = e;
                    esperandoRespuesta = false;
                    lock.notify();
                }
            }
        } else if (evento.getEvento().equals(Evento.LIMPIAR_NAVES)) {
            try {
                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    errorConexion = new Exception("Datos del evento son null");
                    return;
                }

                System.out.println("Datos recibidos: " + datos);
                limpiarTablero(
                        (int) datos.get("coordenadaX"),
                        (int) datos.get("coordenadaY"),
                        (int) datos.get("tamano"),
                        (String) datos.get("orientacion")
                );
                synchronized (lock) {
                    esperandoRespuesta = false;
                    lock.notify();
                }
            } catch (Exception e) {
                synchronized (lock) {
                    errorConexion = e;
                    esperandoRespuesta = false;
                    lock.notify();
                }
            }
        } else if (evento.getEvento().equals(Evento.INICIALIZAR_TABLERO)) {
            try {
                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    throw new Exception("Datos del evento son null");
                }
   
                int[][] matrizTablero = (int[][]) datos.get("tablero");
                clienteTablero = new ClienteTablero(10, 10, matrizTablero);

                synchronized (lock) {
                    esperandoRespuesta = false;
                    lock.notify();
                }
            } catch (Exception e) {
                synchronized (lock) {
                    errorConexion = e;
                    esperandoRespuesta = false;
                    lock.notify();
                }
            }
        } else if (evento.getEvento().equals(Evento.JUGADOR_LISTO)) {
            Map<String, Object> datos = evento.getDatos();

            if (datos.containsKey("partidaIniciada") && (boolean) datos.get("partidaIniciada")) {
                // Inicializar la pantalla de juego con el turno inicial
                String jugadorEnTurno = (String) datos.get("jugadorEnTurno");
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

    private void limpiarTablero(int fila, int columna, int tamano, String orientacion) {
        int alto = clienteTablero.getAlto();
        int ancho = clienteTablero.getAncho();

        // Crear una copia del estado actual del tablero
        int[][] casillasActuales = clienteTablero.getCasillas();
        int[][] nuevasCasillas = new int[alto][ancho];

        // Copiar el estado actual
        for (int i = 0; i < alto; i++) {
            System.arraycopy(casillasActuales[i], 0, nuevasCasillas[i], 0, ancho);
        }

        // Limpiar las casillas ocupadas por la nave
        boolean esHorizontal = "HORIZONTAL".equals(orientacion);
        for (int i = 0; i < tamano; i++) {
            if (esHorizontal) {
                nuevasCasillas[fila][columna + i] = 0;
            } else {
                nuevasCasillas[fila + i][columna] = 0;
            }
        }

        // Crear y asignar el nuevo tablero con las casillas actualizadas
        clienteTablero = new ClienteTablero(alto, ancho, nuevasCasillas);
    }

    public ClienteTablero getClienteTablero() {
        return clienteTablero;
    }

    public void setClienteTablero(ClienteTablero clienteTablero) {
        this.clienteTablero = clienteTablero;
    }
}
