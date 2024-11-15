package org.itson.arquitectura.battleshipservidor.comunicacion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author victo
 */
public class Servidor {

    private static AtomicInteger clientIdGenerator = new AtomicInteger(1);
    private final static int PUERTO = 7000;
    private static ServerSocket serverSocket;

    public void iniciar() {
        try {
            serverSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado en puerto " + PUERTO);

            while (true) {
                Socket cliente = serverSocket.accept();
                System.out.println("Cliente conectado");
                int id = clientIdGenerator.getAndIncrement();

                ClienteHandler clienteHandler = new ClienteHandler(cliente, id);
                new Thread(clienteHandler).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void detener() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

