package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.ivista.IVistaDatosJugador;
import com.mycompany.battleshippresentacion.modelo.ModeloJugador;
import com.mycompany.battleshippresentacion.modelo.ModeloPartida;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

/**
 *
 * @author victo
 */
public class PresentadorJugador implements SocketCliente.EventoListener {

    private final IVistaDatosJugador vistaDatosJugador;
    private PresentadorPrincipal navegacion = null;
    private final Object lock = new Object();
    private volatile boolean esperandoRespuesta = false;
    private volatile Exception errorConexion = null;

    public PresentadorJugador(IVistaDatosJugador vistaDatosJugador, PresentadorPrincipal navegacion) {
        this.vistaDatosJugador = vistaDatosJugador;
        this.navegacion = navegacion;
    }

    public void configurarJugador(String nombreJugador, String colorBarco) {
        try {
            SocketCliente socketCliente = SocketCliente.getInstance();
            socketCliente.setEventoListener(this);

            Map<String, Object> eventData = new HashMap<>();
            eventData.put("nombreJugador", nombreJugador);
            eventData.put("colorBarco", colorBarco);
            EventoDTO event = new EventoDTO(Evento.CONFIGURAR_JUGADOR, eventData);

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
        } catch (Exception e) {
            vistaDatosJugador.mostrarError("Error al configurar jugador: " + e.getMessage());
        }
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
        if (evento.getEvento().equals(Evento.CONFIGURAR_JUGADOR)) {
            try {
                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    throw new Exception("Datos del evento son null");
                }
                synchronized (lock) {
                    if (datos.containsKey("exitoso") && (Boolean) datos.get("exitoso")) {
                        esperandoRespuesta = false;
                        lock.notify();

                    } else {
                        errorConexion = new Exception("No se pudo configurar el jugador");
                        esperandoRespuesta = false;
                        lock.notify();
                    }
                }
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
