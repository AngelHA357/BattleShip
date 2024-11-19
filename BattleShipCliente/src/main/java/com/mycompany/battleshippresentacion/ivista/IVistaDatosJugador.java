/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.battleshippresentacion.ivista;

import com.mycompany.battleshippresentacion.modelo.ModeloPartida;

/**
 *
 * @author PC
 */
public interface IVistaDatosJugador {

    public void mostrarConfiguracionJugador(ModeloPartida partida);

    public void actualizarVista(ModeloPartida modelo);

    public void mostrarError(String mensaje);
}
