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
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshipeventos.eventos.Evento.COLOCAR_NAVES;

/**
 *
 * @author JoseH
 */
public class ColocarBarcosPresentador implements SocketCliente.EventoListener{

    private final PantallaColocarBarcos vista;
    private ClienteTablero clienteTablero;
    private ClienteNave clienteNave;
    private String naveSeleccionada;
    private int orientacionActual;

    private List<ClienteNave> naves;
    private ClienteTablero tablero;
    
    private volatile boolean esperandoRespuesta = false;
    private final Object lock = new Object();
    private volatile Exception errorConexion = null;

    public ColocarBarcosPresentador(PantallaColocarBarcos vista) {
        this.vista = vista;
        orientacionActual = 0;
    }

    public void inicializarJuego() {
        vista.crearTablero();

    }
    
    public void seleccionarNave(String tipoNave) {
        this.naveSeleccionada = tipoNave;
    }
    
    public void enviarColocacionNave(int fila, int columna, String tipoNave, int orientacion, int tamano) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("coordenadaX", fila);
        data.put("coordenadaY", columna);
        data.put("tipoNave", tipoNave);
        data.put("orientacion", orientacion);
        data.put("tamano", tamano);
        
        EventoDTO eventoDTO = new EventoDTO(COLOCAR_NAVES, data);

        SocketCliente socketCliente = SocketCliente.getInstance();
        socketCliente.setEventoListener(this);

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
        
        
        

    }
    
    @Override
    public void onEventoRecibido(EventoDTO evento) {
        synchronized (lock) {
            try {
                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    errorConexion = new Exception("Datos del evento son null");
                    return;
                }
                // Crear la nave cliente con los datos recibidos
                clienteNave = new ClienteNave(
                        (String) datos.get("tipoNave"),
                        (int) datos.get("tamano"),
                        traducirOrientacion((int) datos.get("orientacion")),
                        "SIN_DAÑOS" // Estado inicial de la nave
                );

                // Actualizar el tablero con la nueva nave
                actualizarTableroConNave(
                        (int) datos.get("coordenadaX"),
                        (int) datos.get("coordenadaY")
                );
            
        } catch (Exception e) {
                errorConexion = e;
            } finally {
                esperandoRespuesta = false;
                lock.notify();
            }
        }
    }
 
    private String traducirOrientacion(int orientacion) {
        return orientacion == 0 ? "HORIZONTAL" : "VERTICAL";
    }

    private void actualizarTableroConNave(int fila, int columna) {
        int alto = clienteTablero.getAlto();
        int ancho = clienteTablero.getAncho();

        // Crear una copia del estado actual del tablero
        int[][] casillasActuales = clienteTablero.getCasillas();
        int[][] nuevasCasillas = new int[alto][ancho];

        // Copiar el estado actual
        for (int i = 0; i < alto; i++) {
            System.arraycopy(casillasActuales[i], 0, nuevasCasillas[i], 0, ancho);
        }

        // Marcar las casillas ocupadas por la nave
        int tamano = clienteNave.getTamano();
        boolean esHorizontal = "HORIZONTAL".equals(clienteNave.getOrientacion());

        for (int i = 0; i < tamano; i++) {
            if (esHorizontal) {
                nuevasCasillas[fila][columna + i] = 1;
            } else {
                nuevasCasillas[fila + i][columna] = 1;
            }
        }

        // Crear y asignar el nuevo tablero con las casillas actualizadas
        clienteTablero = new ClienteTablero(alto, ancho, nuevasCasillas);
    }
}
