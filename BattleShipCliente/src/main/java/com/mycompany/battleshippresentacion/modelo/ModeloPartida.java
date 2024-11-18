package com.mycompany.battleshippresentacion.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshipeventos.eventos.Evento.CREAR_PARTIDA;
import org.itson.arquitectura.battleshipservidor.dominio.Jugador;
import org.itson.arquitectura.battleshipservidor.dominio.Partida;
import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoPartida;

/**
 *
 * @author victo
 */
public class ModeloPartida implements SocketCliente.EventoListener {

    private volatile Partida partida;
    private final Object lock = new Object();
    private volatile boolean esperandoRespuesta = false;
    private volatile Exception errorConexion = null;

    public Partida crearPartida() throws Exception {
        try {
            partida = Partida.getInstance();

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

            return partida;

        } catch (Exception e) {
            partida = null;
            throw new Exception("Error al crear la partida: " + e.getMessage(), e);
        }
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
        try {
            if (partida == null) {
                throw new Exception("Partida no inicializada");
            }

            Map<String, Object> datos = evento.getDatos();
            if (datos == null) {
                throw new Exception("Datos del evento son null");
            }

            synchronized (partida) {
                // Actualizar datos de la partida
                if (datos.containsKey("codigoSala")) {
                    partida.setCodigoSala((String) datos.get("codigoSala"));
                }
                if (datos.containsKey("estado")) {
                    Object estadoObj = datos.get("estado");
                    if (estadoObj instanceof EstadoPartida) {
                        partida.setEstado((EstadoPartida) estadoObj);
                    } else {
                        System.out.println("Warning: estado recibido no es del tipo EstadoPartida");
                    }
                }
                if (datos.containsKey("jugadores")) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Jugador> jugadores = (List<Jugador>) datos.get("jugadores");
                        partida.setJugadores(jugadores);
                    } catch (ClassCastException e) {
                        System.out.println("Error al convertir lista de jugadores: " + e.getMessage());
                    }
                }
                if (datos.containsKey("jugadorEnTurno")) {
                    Object jugadorObj = datos.get("jugadorEnTurno");
                    if (jugadorObj instanceof Jugador) {
                        partida.setJugadorEnTurno((Jugador) jugadorObj);
                    } else {
                        System.out.println("Warning: jugadorEnTurno no es del tipo Jugador");
                    }
                }
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
            System.out.println("Error procesando evento: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
