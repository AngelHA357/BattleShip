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

    private SocketCliente() {

    }

    public static SocketCliente getInstance() {
        if (instance == null) {
            instance = new SocketCliente();
        }
        return instance;
    }

    public boolean conectar(String host) {
        try {
            socket = new Socket(host, PUERTO);
            out = new ObjectOutputStream(socket.getOutputStream());
            conectado = true;
            
//            new Thread(this::escucharServidor).start();
            return true;
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return false;
        }
    }
    
//    private void escucharServidor() {
//        try {
//            while (conectado && !socket.isClosed()) {
//                EventoDTO mensaje = (EventoDTO) in.readObject();
//                procesarMensajeServidor(mensaje);
//            }
//        } catch (Exception e) {
//            System.out.println("Error al escuchar servidor: " + e.getMessage());
//            desconectar();
//        }
//    }
}
