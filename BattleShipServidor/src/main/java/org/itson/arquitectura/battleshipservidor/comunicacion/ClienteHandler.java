/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.arquitectura.battleshipservidor.comunicacion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author PC
 */
public class ClienteHandler implements Runnable {

    private Socket clienteSocket;
    private Servidor servidor;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int idCliente;
    private boolean conectado;

    public ClienteHandler(Socket clienteSocket, int idCliente) {
        this.clienteSocket = clienteSocket;
        this.idCliente = idCliente;
        this.conectado = true;
        try {
            out = new ObjectOutputStream(clienteSocket.getOutputStream());
            in = new ObjectInputStream(clienteSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        
    }

}
