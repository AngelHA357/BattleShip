package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.ivista.IVistaPartida;
import com.mycompany.battleshippresentacion.modelo.ModeloJugador;
import com.mycompany.battleshippresentacion.modelo.ModeloPartida;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshiptransporte.eventos.Evento.CREAR_PARTIDA;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoPartida;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

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

    public ModeloPartida crearPartida() {
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
            return modelo;
        } catch (Exception e) {
            vista.mostrarError("Error al crear partida: " + e.getMessage());
            return null;
        }
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
            eventData.put("estado", EstadoPartida.ESPERANDO);
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

//            vista.mostrarUnidoAPartida(modelo);
            return modelo;
        } catch (Exception e) {
            vista.mostrarError("Error al unirse a la partida: " + e.getMessage());
            return null;
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

//            if (datos.containsKey("jugadores")) {
//                try {
//                    @SuppressWarnings("unchecked")
//                    List<Map<String, String>> jugadoresData = (List<Map<String, String>>) datos.get("jugadores");
//                    List<ModeloJugador> jugadoresModelo = jugadoresData.stream()
//                            .map(j -> new ModeloJugador(j.get("id"), j.get("nombre")))
//                            .collect(Collectors.toList());
//                    modelo.setJugadores(jugadoresModelo);
//                } catch (ClassCastException e) {
//                    System.out.println("Error al convertir lista de jugadores: " + e.getMessage());
//                }
//            }
//            if (datos.containsKey("jugadorEnTurno")) {
//                Object jugadorObj = datos.get("jugadorEnTurno");
//                if (jugadorObj instanceof Map) {
//                    @SuppressWarnings("unchecked")
//                    Map<String, String> jugadorData = (Map<String, String>) jugadorObj;
//                    modelo.setJugadorEnTurno(new ModeloJugador(
//                            jugadorData.get("id"),
//                            jugadorData.get("nombre")
//                    ));
//                }
//            }
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

        } else if (evento.getEvento().equals(Evento.UNIRSE_PARTIDA)) {
            try {
                if (evento.getEvento().equals(Evento.UNIRSE_PARTIDA)) {
                    Map<String, Object> datos = evento.getDatos();
                    if (datos == null) {
                        throw new Exception("Datos del evento son null");
                    }

                    // Actualizar código de sala
                    if (datos.containsKey("codigoSala")) {
                        modelo.setCodigoSala((String) datos.get("codigoSala"));
                    }

                    // Actualizar estado
                    if (datos.containsKey("estado")) {
                        Object estadoObj = datos.get("estado");
                        if (estadoObj instanceof EstadoPartida) {
                            modelo.setEstado((EstadoPartida) estadoObj);
                        }
                    }

                    // Actualizar lista de jugadores
                    if (datos.containsKey("jugadores")) {
                        try {
                            @SuppressWarnings("unchecked")
                            List<Map<String, String>> jugadoresData
                                    = (List<Map<String, String>>) datos.get("jugadores");
                            List<ModeloJugador> jugadoresModelo = jugadoresData.stream()
                                    .map(j -> new ModeloJugador(j.get("id"), j.get("nombre")))
                                    .collect(Collectors.toList());
                            modelo.setJugadores(jugadoresModelo);
                        } catch (ClassCastException e) {
                            throw new Exception("Error al procesar lista de jugadores: " + e.getMessage());
                        }
                    }

                    synchronized (lock) {
                        esperandoRespuesta = false;
                        lock.notify();
                    }

                    vista.actualizarVista(modelo);
                }
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
}
