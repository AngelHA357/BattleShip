/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.arquitectura.battleshipservidor.comunicacion;

import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshipeventos.eventos.Evento.CREAR_NAVES;
import static org.itson.arquitectura.battleshipeventos.eventos.Evento.INICIALIZAR_TABLERO;
import org.itson.arquitectura.battleshipservidor.controlador.ControladorEventos;

/**
 *
 * @author PC
 */
public class ManejadorEventos {

    private static ManejadorEventos instance;
    private ControladorEventos controlador;

    private ManejadorEventos() {
        this.controlador = new ControladorEventos();
    }

    public static synchronized ManejadorEventos getInstance() {
        if (instance == null) {
            instance = new ManejadorEventos();
        }
        return instance;
    }

    public Object manejarEvento(EventoDTO evento) {
        if (evento.getEvento() == CREAR_NAVES){
            return controlador.crearNaves();
        } else if (evento.getEvento() == INICIALIZAR_TABLERO){
            return controlador.inicializarTablero();
        }
        
        return null;
    }
}
