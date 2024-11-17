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

/**
 *
 * @author JoseH
 */
public class ModeloCreacionNaves {

    
    public List<ClienteNave> crearNaves(){
        List<ClienteNave> naves = new ArrayList<>();
        
        naves.add(new ClienteNave("Barco", 1));
        naves.add(new ClienteNave("Submarino", 2));
        naves.add(new ClienteNave("Crucero", 3));
        naves.add(new ClienteNave("PortaAviones", 4));
        
        return naves;
    }
    
    public void enviarEvento(List<ClienteNave> naves){
        for (ClienteNave nave : naves){
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("nombre", nave.getNombre());
            eventData.put("tamano", nave.getTamano());
            
            EventoDTO event = new EventoDTO(CREAR_NAVES, eventData);
        }
    }
    
    
    
}
