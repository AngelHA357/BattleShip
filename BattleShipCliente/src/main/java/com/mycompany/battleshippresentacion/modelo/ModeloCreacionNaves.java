/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.modelo;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshipeventos.eventos.Evento.CREAR_NAVES;

/**
 *
 * @author JoseH
 */
public class ModeloCreacionNaves implements SocketCliente.EventoListener {

    List<ClienteNave> listaClntNvs;

    public List<ClienteNave> crearNaves() {
        List<ClienteNave> naves = new ArrayList<>();
        Map<String, Object> eventData = null;
        naves.add(new ClienteNave("Barco", 1));
        naves.add(new ClienteNave("Submarino", 2));
        naves.add(new ClienteNave("Crucero", 3));
        naves.add(new ClienteNave("PortaAviones", 4));

        eventData = new HashMap<>();
        eventData.put("naves", naves);

        EventoDTO event = new EventoDTO(CREAR_NAVES, eventData);
        SocketCliente socketCliente = SocketCliente.getInstance();

        socketCliente.setEventoListener(this);

        if (socketCliente.conectar("localhost")) {
            socketCliente.enviarEvento(event);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return listaClntNvs;
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
        Map<String, Object> datos = evento.getDatos();

        if (datos.containsKey("naves")) {
            List<Map<String, Object>> navesList = (List<Map<String, Object>>) datos.get("naves");
            for (Map<String, Object> naveMap : navesList) {
                String nombre = (String) naveMap.get("nombre");
                Integer tamano = (Integer) naveMap.get("tamano");
                if (nombre != null && tamano != null) {
                    listaClntNvs.add(new ClienteNave(nombre, tamano));
                }
            }
        }
    }
}
