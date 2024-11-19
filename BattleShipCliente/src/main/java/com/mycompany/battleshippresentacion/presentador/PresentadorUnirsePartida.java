/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.ivista.IVistaDatosJugador;
import com.mycompany.battleshippresentacion.ivista.IVistaPartida;
import com.mycompany.battleshippresentacion.modelo.ModeloPartida;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

/**
 *
 * @author PC
 */
public class PresentadorUnirsePartida implements SocketCliente.EventoListener {

    private final ModeloPartida modelo;
    private final IVistaDatosJugador vistaDatosJugador;
    private final Object lock = new Object();
    private volatile boolean esperandoRespuesta = false;
    private volatile Exception errorConexion = null;

    public PresentadorUnirsePartida(IVistaDatosJugador vistaDatosJugador) {
        this.modelo = new ModeloPartida();
        this.vistaDatosJugador = vistaDatosJugador;
    }

    public ModeloPartida unirsePartida(String codigoSala) {
        try {
            SocketCliente socketCliente = SocketCliente.getInstance();
            socketCliente.setEventoListener(this);

            if (!socketCliente.conectar("localhost")) {
                throw new Exception("No se pudo conectar al servidor");
            }

            Map<String, Object> eventData = new HashMap<>();
            eventData.put("codigoSala", codigoSala);
            EventoDTO event = new EventoDTO(Evento.UNIRSE_PARTIDA, eventData);

            synchronized (lock) {
                esperandoRespuesta = true;
                socketCliente.enviarEvento(event);

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

            return modelo;
        } catch (Exception e) {
            vistaDatosJugador.mostrarError("Error al unirse a la partida: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
        if (evento.getEvento().equals(Evento.UNIRSE_PARTIDA)) {
            try {
                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    throw new Exception("Datos del evento son null");
                }

                if (datos.containsKey("exitoso") && (Boolean) datos.get("exitoso")) {
                    if (datos.containsKey("cantidadJugadores")) {
                        Object cantidadObj = datos.get("cantidadJugadores");
                        modelo.setCantidadJugadores((Integer) cantidadObj);
                    }
                    if (modelo.getCantidadJugadores() == 2) {
                        vistaDatosJugador.mostrarConfiguracionJugador(modelo);
                    }
                } else {
                    throw new Exception("No se pudo unir a la partida");
                }

                synchronized (lock) {
                    esperandoRespuesta = false;
                    lock.notify();
                }

                vistaDatosJugador.actualizarVista(modelo);

            } catch (Exception e) {
                synchronized (lock) {
                    errorConexion = e;
                    esperandoRespuesta = false;
                    lock.notify();
                }
                vistaDatosJugador.mostrarError("Error procesando evento: " + e.getMessage());
            }
        }
    }
}
