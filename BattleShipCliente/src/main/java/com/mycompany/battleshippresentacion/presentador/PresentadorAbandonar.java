package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.ivista.IVistaJugarPartida;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

public class PresentadorAbandonar implements SocketCliente.EventoListener {

    private final IVistaJugarPartida vista;
    private final SocketCliente socketCliente;
    private final String idJugador;
    private final PresentadorPrincipal navegacion;
    private volatile boolean esperandoRespuesta = false;
    private final Object lock = new Object();
    private volatile Exception errorConexion = null;

    public PresentadorAbandonar(IVistaJugarPartida vista, PresentadorJugador presentadorJugador, PresentadorPrincipal navegacion) {
        this.vista = vista;
        this.idJugador = presentadorJugador.getModeloJugador().getId();
        this.navegacion = navegacion;
        this.socketCliente = SocketCliente.getInstance();
        this.socketCliente.setEventoListener(this);
    }

    public void abandonarPartida() {
        int confirmacion = vista.mostrarConfirmacionAbandono();
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Map<String, Object> data = new HashMap<>();
                data.put("idJugador", idJugador);
                EventoDTO eventoDTO = new EventoDTO(Evento.ABANDONAR_PARTIDA, data);

                synchronized (lock) {
                    esperandoRespuesta = true;
                    socketCliente.enviarEvento(eventoDTO);

                    try {
                        lock.wait(5000);
                    } catch (InterruptedException e) {
                        throw new Exception("Interrupción mientras se esperaba respuesta del servidor");
                    }

                    if (errorConexion != null) {
                        throw errorConexion;
                    }
                }

                navegacion.mostrarPantallaInicio();
            } catch (Exception e) {
                vista.mostrarError("Error al abandonar partida: " + e.getMessage());
            }
        }
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
        if (evento.getEvento().equals(Evento.ABANDONAR_PARTIDA)) {
            try {
                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    return;
                }

                String jugadorAbandonoId = (String) datos.get("jugadorAbandonoId");

                if (!jugadorAbandonoId.equals(idJugador)) {
                    vista.mostrarMensajeAbandonoOponente();
                    navegacion.mostrarPantallaInicio();
                }

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
        }
    }
}
