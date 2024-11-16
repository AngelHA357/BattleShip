/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.arquitectura.battleshipservidor.comunicacion;

import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
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

    public void manejarEvento(EventoDTO evento) {

    }
}
