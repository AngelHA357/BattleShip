package org.itson.arquitectura.battleshipcliente.comunicacion;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;

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

    private SocketCliente() {

    }

    public static synchronized SocketCliente getInstance() {
        if (instance == null) {
            instance = new SocketCliente();
        }
        return instance;
    }

    public boolean conectar(String host) {
        try {
            socket = new Socket(host, PUERTO);
            socket.setSoTimeout(TIMEOUT);  // Establecer timeout
            socket.setKeepAlive(true);

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // Flush inmediato para evitar deadlock
            in = new ObjectInputStream(socket.getInputStream());
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
        try {
            while (conectado && !socket.isClosed()) {
                EventoDTO mensaje = (EventoDTO) in.readObject();
                if (mensaje != null) {
                    procesarEventoRecibido(mensaje);
                }
            }
        } catch (Exception e) {
            if (conectado) {
                System.out.println("Error al escuchar servidor: " + e.getMessage());
                e.printStackTrace();
                desconectar();
            }
        }
    }

    private void procesarEventoRecibido(EventoDTO evento) {
        if (eventoListener != null) {
            eventoListener.onEventoRecibido(evento);
        }
    }

    public void enviarEvento(EventoDTO evento) {
        try {
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

    public interface EventoListener {

        void onEventoRecibido(EventoDTO evento);
    }
}
