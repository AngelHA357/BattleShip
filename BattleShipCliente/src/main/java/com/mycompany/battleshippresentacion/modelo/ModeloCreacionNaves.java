/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.modelo;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshipeventos.eventos.Evento.CREAR_NAVES;
import org.itson.arquitectura.battleshipservidor.comunicacion.ManejadorEventos;
import org.itson.arquitectura.battleshipservidor.dominio.nave.Nave;

/**
 *
 * @author JoseH
 */
public class ModeloCreacionNaves {

    
    public List<ClienteNave> crearNaves() {
        List<ClienteNave> naves = new ArrayList<>();
        Map<String, Object> eventData = null;
        naves.add(new ClienteNave("Barco", 1));
        naves.add(new ClienteNave("Submarino", 2));
        naves.add(new ClienteNave("Crucero", 3));
        naves.add(new ClienteNave("PortaAviones", 4));

        for (ClienteNave nave : naves) {
            eventData = new HashMap<>();
            eventData.put("nombre", nave.getNombre());
            eventData.put("tamano", nave.getTamano());

        }

        EventoDTO event = new EventoDTO(CREAR_NAVES, eventData);
        ManejadorEventos mnjEvts = ManejadorEventos.getInstance();
        List<Nave> listaNaves = (List<Nave>) mnjEvts.manejarEvento(event);
        List<ClienteNave> listaClntNvs = new ArrayList<>();

        for (Nave nave : listaNaves) {
            listaClntNvs.add(new ClienteNave(nave.getNombre(), nave.getTamano()));
        }

        return listaClntNvs;
    }

}
