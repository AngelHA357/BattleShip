package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.ivista.IVistaPartida;
import com.mycompany.battleshippresentacion.modelo.ModeloJugador;
import com.mycompany.battleshippresentacion.modelo.ModeloPartida;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshipeventos.eventos.Evento.CREAR_PARTIDA;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoPartida;

/**
 *
 * @author victo
 */
public class PresentadorPartida implements SocketCliente.EventoListener {
    private final ModeloPartida modelo;
    private final IVistaPartida vista;
    private final Object lock = new Object();
    private volatile boolean esperandoRespuesta = false;
    private volatile Exception errorConexion = null;

    public PresentadorPartida(IVistaPartida vista) {
        this.modelo = new ModeloPartida();
        this.vista = vista;
    }

    public void crearPartida() {
        try {
            
            SocketCliente socketCliente = SocketCliente.getInstance();
            socketCliente.setEventoListener(this);

            if (!socketCliente.conectar("localhost")) {
                throw new Exception("No se pudo conectar al servidor");
            }

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

            vista.mostrarPartidaCreada(modelo);
        } catch (Exception e) {
            vista.mostrarError("Error al crear partida: " + e.getMessage());
        }
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
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
            if (datos.containsKey("jugadores")) {
                try {
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> jugadoresData = (List<Map<String, String>>) datos.get("jugadores");
                    List<ModeloJugador> jugadoresModelo = jugadoresData.stream()
                        .map(j -> new ModeloJugador(j.get("id"), j.get("nombre")))
                        .collect(Collectors.toList());
                    modelo.setJugadores(jugadoresModelo);
                } catch (ClassCastException e) {
                    System.out.println("Error al convertir lista de jugadores: " + e.getMessage());
                }
            }
            if (datos.containsKey("jugadorEnTurno")) {
                Object jugadorObj = datos.get("jugadorEnTurno");
                if (jugadorObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> jugadorData = (Map<String, String>) jugadorObj;
                    modelo.setJugadorEnTurno(new ModeloJugador(
                        jugadorData.get("id"), 
                        jugadorData.get("nombre")
                    ));
                }
            }

            synchronized (lock) {
                esperandoRespuesta = false;
                lock.notify();
            }

            vista.actualizarVista(modelo);

        } catch (Exception e) {
            synchronized (lock) {
                errorConexion = e;
                esperandoRespuesta = false;
                lock.notify();
            }
            vista.mostrarError("Error procesando evento: " + e.getMessage());
        }
    }
}
