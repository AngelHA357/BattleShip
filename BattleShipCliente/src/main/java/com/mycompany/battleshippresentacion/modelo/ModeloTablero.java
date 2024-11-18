/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.modelo;

import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshipeventos.eventos.Evento.INICIALIZAR_TABLERO;

/**
 *
 * @author JoseH
 */
public class ModeloTablero {
    
    
    public void inicializarTablero(){
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("", null);
            
            EventoDTO event = new EventoDTO(INICIALIZAR_TABLERO, eventData);
    }
    
}
