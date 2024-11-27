package org.itson.arquitectura.battleshipcliente.comunicacion;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

/**
 *
 * @author victo
 */
public class SocketCliente {

    private static SocketCliente instance = null;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final static int PUERTO = 7000;
    private volatile boolean conectado;
    private EventoListener eventoListener;
    private Thread listenThread;
    private static final int TIMEOUT = 60000; // 60 segundos de timeout
    private String sessionId;
    private final Object reconnectLock = new Object();

    private SocketCliente() {
        this.sessionId = UUID.randomUUID().toString();
    }

    public static synchronized SocketCliente getInstance() {
        if (instance == null) {
            instance = new SocketCliente();
        }
        return instance;
    }

    public boolean conectar(String host) {
        synchronized (reconnectLock) {
            try {
                if (socket != null && !socket.isClosed()) {
                    return true;
                }

                socket = new Socket(host, PUERTO);
                socket.setSoTimeout(TIMEOUT);
                socket.setKeepAlive(true);
                socket.setTcpNoDelay(true);

                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());

//                 Inicializar sesión
                Map<String, Object> sessionData = new HashMap<>();
                sessionData.put("sessionId", sessionId);
                EventoDTO sessionEvento = new EventoDTO(Evento.SESSION_INIT, sessionData);

                out.writeObject(sessionEvento);
                out.flush();
                out.reset();

                EventoDTO respuesta = (EventoDTO) in.readObject();
                if (respuesta == null || respuesta.getEvento() != Evento.SESSION_INIT) {
                    throw new IOException("Fallo en la inicialización de sesión");
                }

                conectado = true;
                iniciarEscucha();
                return true;
            } catch (Exception e) {
                System.out.println("Error al conectar: " + e.getMessage());
                e.printStackTrace();
                desconectar();
                return false;
            }
        }
    }

    private void iniciarEscucha() {
        listenThread = new Thread(() -> {
            while (conectado && !Thread.currentThread().isInterrupted()) {
                try {
                    escucharServidor();
                } catch (Exception e) {
                    if (conectado) {
                        System.out.println("Error en el hilo de escucha: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
        listenThread.setDaemon(true);
        listenThread.start();
    }

    private void escucharServidor() {
        while (conectado && !Thread.currentThread().isInterrupted()) {
            try {
                if (socket == null || socket.isClosed()) {
                    reconectar();
                    continue;
                }

                EventoDTO mensaje = (EventoDTO) in.readObject();
                if (mensaje != null) {
                    procesarEventoRecibido(mensaje);
                }
            } catch (SocketTimeoutException e) {
                // Timeout normal, continuar escuchando
                continue;
            } catch (EOFException | StreamCorruptedException e) {
                System.out.println("Error en la conexión, intentando reconectar...");
                reconectar();
            } catch (Exception e) {
                if (conectado) {
                    System.out.println("Error al escuchar servidor: " + e.getMessage());
                    e.printStackTrace();
                    reconectar();
                }
            }
        }
    }

    private void procesarEventoRecibido(EventoDTO evento) {
        try {
            System.out.println("Procesando evento recibido con ID de jugador: " + evento.getIdJugador());
            if (eventoListener != null) {
                eventoListener.onEventoRecibido(evento);
            }
        } catch (Exception e) {
            System.out.println("Error procesando evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void enviarEvento(EventoDTO evento) {
        try {
            if (!estaConectado()) {
                reconectar();
            }

            if (conectado && out != null) {
                synchronized (out) {
                    out.writeObject(evento);
                    out.flush();
                    out.reset();
                }
            }
        } catch (Exception e) {
            System.out.println("Error al enviar evento: " + e.getMessage());
            e.printStackTrace();
            desconectar();
        }
    }

    private void reconectar() {
        synchronized (reconnectLock) {
            try {
                desconectar();
                Thread.sleep(1000); // Esperar un segundo antes de reconectar

                if (!conectar("localhost")) {
                    System.out.println("Fallo en la reconexión, reintentando...");
                }
            } catch (Exception e) {
                System.out.println("Error al reconectar: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void desconectar() {
        if (!conectado) {
            return;
        }

        conectado = false;
        try {
            if (listenThread != null) {
                listenThread.interrupt();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("Error al desconectar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            socket = null;
            in = null;
            out = null;
        }
    }

    public void setEventoListener(EventoListener listener) {
        this.eventoListener = listener;
    }

    public boolean estaConectado() {
        return conectado && socket != null && !socket.isClosed();
    }

    public EventoListener getEventoListener() {
        return eventoListener;
    }

    public interface EventoListener {

        void onEventoRecibido(EventoDTO evento);
    }
}
