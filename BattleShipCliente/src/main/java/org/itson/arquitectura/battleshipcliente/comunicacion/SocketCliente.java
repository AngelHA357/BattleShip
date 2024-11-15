package org.itson.arquitectura.battleshipcliente.comunicacion;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author victo
 */
public class SocketCliente {

    private static SocketCliente instance = null;
    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private final static int PUERTO = 7000;
    private boolean conectado;

    private SocketCliente(){
        
    }
    public static SocketCliente getInstance(){
        if (instance == null) {
            instance = new SocketCliente();
        }
        return instance;
    }
    
    public boolean conectar(String host){
        try {
            socket = new Socket(host, PUERTO);
            out = socket.getOutputStream();
            in = socket.getInputStream();
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
