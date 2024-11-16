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
    private boolean conectado;
    private EventoListener eventoListener;
    private Thread listenThread;

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
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            conectado = true;

            iniciarEscucha();
            return true;
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return false;
        }
    }

    private void iniciarEscucha() {
        listenThread = new Thread(this::escucharServidor);
        listenThread.setDaemon(true);
        listenThread.start();
    }

    private void escucharServidor() {
        try {
            while (conectado && !socket.isClosed()) {
                EventoDTO mensaje = (EventoDTO) in.readObject();
                procesarEventoRecibido(mensaje);
            }
        } catch (Exception e) {
            System.out.println("Error al escuchar servidor: " + e.getMessage());
            desconectar();
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
                out.writeObject(evento);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("Error al enviar evento: " + e.getMessage());
            desconectar();
        }
    }

    public void desconectar() {
        conectado = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (listenThread != null) {
                listenThread.interrupt();
            }
            socket = null;
            in = null;
            out = null;
        } catch (Exception e) {
            System.out.println("Error al desconectar: " + e.getMessage());
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
