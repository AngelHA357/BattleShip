package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.modelo.ModeloPartida;
import org.itson.arquitectura.battleshipservidor.dominio.Partida;

/**
 *
 * @author victo
 */
public class PresentadorPartida {

    private final ModeloPartida modelo;
//    private final VistaPartida vista;

    public PresentadorPartida() {
        modelo = new ModeloPartida();
    }

    public void onCrearPartidaClick() {
        try {
//            vista.mostrarCargando();
            Partida partida = modelo.crearPartida();
//            vista.mostrarPartidaCreada(partida);
        } catch (Exception e) {
//            vista.mostrarError("Error al crear partida: " + e.getMessage());
        } 
    }
}
