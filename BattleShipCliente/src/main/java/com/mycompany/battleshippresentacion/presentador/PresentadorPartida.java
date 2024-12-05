package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.ivista.IVistaPartida;
import com.mycompany.battleshippresentacion.modelo.ModeloPartida;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshiptransporte.eventos.Evento.CREAR_PARTIDA;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoPartida;
import static org.itson.arquitectura.battleshiptransporte.eventos.Evento.UNIRSE_PARTIDA;

/**
 *
 * @author victo
 */
public class PresentadorPartida implements SocketCliente.EventoListener {

    private final ModeloPartida modelo;
    private final IVistaPartida vista;
    private PresentadorPrincipal navegacion = null;
    private final Object lock = new Object();
    private volatile boolean esperandoRespuesta = false;
    private volatile Exception errorConexion = null;

    public PresentadorPartida(IVistaPartida vista, PresentadorPrincipal navegacion) {
        this.modelo = new ModeloPartida();
        this.vista = vista;
        this.navegacion = navegacion;
    }

    public void crearPartida() {
        try {

            SocketCliente socketCliente = SocketCliente.getInstance();
            socketCliente.setEventoListener(this);

            Map<String, Object> eventData = new HashMap<>();
            eventData.put("estado", EstadoPartida.ESPERANDO);
            EventoDTO event = new EventoDTO(CREAR_PARTIDA, eventData);

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
            vista.mostrarError("Error al crear partida: " + e.getMessage());
        }
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
        if (evento.getEvento().equals(CREAR_PARTIDA)) {
            try {

                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    throw new Exception("Datos del evento son null");
                }

                if (datos.containsKey("codigoSala")) {
                    modelo.setCodigoSala((String) datos.get("codigoSala"));
                }
                if (datos.containsKey("estado")) {
                    Object estadoObj = datos.get("estado");
                    if (estadoObj instanceof EstadoPartida) {
                        modelo.setEstado((EstadoPartida) estadoObj);
                    }
                }

                synchronized (lock) {
                    esperandoRespuesta = false;
                    lock.notify();
                }

                vista.mostrarCodigo(modelo.getCodigoSala());

            } catch (Exception e) {
                synchronized (lock) {
                    errorConexion = e;
                    esperandoRespuesta = false;
                    lock.notify();
                }
                vista.mostrarError("Error procesando evento: " + e.getMessage());
            }

        } else if (evento.getEvento().equals(UNIRSE_PARTIDA)) {
            Map<String, Object> datos = evento.getDatos();
            if (datos.containsKey("cantidadJugadores")) {
                Object cantidadObj = datos.get("cantidadJugadores");
                modelo.setCantidadJugadores((Integer) cantidadObj);

                if (modelo.getCantidadJugadores() == 2) {
                    navegacion.mostrarPantallaDatosJugador();
                    return;
                }
            }
            if (datos.containsKey("estado")) {
                Object estadoObj = datos.get("estado");
                if (estadoObj instanceof EstadoPartida) {
                    modelo.setEstado((EstadoPartida) estadoObj);
                }
            }
        }

    }
}
