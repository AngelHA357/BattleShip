/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.arquitectura.battleshipservidor.comunicacion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;

/**
 *
 * @author PC
 */
public class ClienteHandler implements Runnable {

    private Socket clienteSocket;
    private Servidor servidor;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int idCliente;
    private static Map<Integer, ClienteHandler> clientesConectados = new ConcurrentHashMap<>();
    private boolean conectado = true;
    private ManejadorEventos manejadorEventos;

    public ClienteHandler(Socket clienteSocket, int idCliente) {
        this.clienteSocket = clienteSocket;
        this.idCliente = idCliente;
        this.manejadorEventos = ManejadorEventos.getInstance();
        clientesConectados.put(idCliente, this);
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clienteSocket.getOutputStream());
            in = new ObjectInputStream(clienteSocket.getInputStream());
            while (conectado && !clienteSocket.isClosed()) {
                EventoDTO evento = (EventoDTO) in.readObject();
                evento.setIdJugador(String.valueOf(idCliente));
                procesarEvento(evento);
            }
        } catch (Exception e) {
            System.out.println("Jugador " + idCliente + " desconectado: " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    private void procesarEvento(EventoDTO evento) {
        try {
            manejadorEventos.manejarEvento(evento);
        } catch (Exception e) {
            System.out.println("Error al procesar evento: " + e.getMessage());
        }
    }

    public void enviarEvento(EventoDTO evento) {
        try {
            if (out != null && conectado) {
                out.writeObject(evento);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Error al enviar evento al cliente " + idCliente + ": " + e.getMessage());
            desconectar();
        }
    }

    public static void enviarEventoATodos(EventoDTO evento) {
        clientesConectados.values().forEach(cliente -> cliente.enviarEvento(evento));
    }

    public static void enviarEventoAJugador(int idJugador, EventoDTO evento) {
        ClienteHandler cliente = clientesConectados.get(idJugador);
        if (cliente != null) {
            cliente.enviarEvento(evento);
        }
    }

    private void desconectar() {
        conectado = false;
        clientesConectados.remove(idCliente);
        try {
            if (clienteSocket != null && !clienteSocket.isClosed()) {
                clienteSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error al cerrar la conexión del cliente " + idCliente + ": " + e.getMessage());
        }
    }

    public int getIdCliente() {
        return idCliente;
    }
}
