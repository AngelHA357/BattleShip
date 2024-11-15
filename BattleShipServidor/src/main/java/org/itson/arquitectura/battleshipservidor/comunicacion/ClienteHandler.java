/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.arquitectura.battleshipservidor.comunicacion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;

/**
 *
 * @author PC
 */
public class ClienteHandler implements Runnable {

    private Socket clienteSocket;
    private Servidor servidor;
    private ObjectInputStream in;
    private int idCliente;

    public ClienteHandler(Socket clienteSocket, int idCliente) {
        this.clienteSocket = clienteSocket;
        this.idCliente = idCliente;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(clienteSocket.getInputStream());
            while (!clienteSocket.isClosed()) {
                EventoDTO mensaje = (EventoDTO) in.readObject();
                mensaje.setIdJugador(String.valueOf(idCliente));

//                controlador.procesarEvento(mensaje.getEvento(), mensaje.getDatos());
            }
        } catch (Exception e) {
            System.out.println("Jugador " + idCliente + " desconectado: " + e.getMessage());
        } finally {
            try {
                clienteSocket.close();
                System.out.println("Conexión cerrada para jugador " + idCliente);
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexión del jugador " + idCliente);
            }
        }
    }

}
