package com.mycompany.battleshippresentacion.ivista;

import com.mycompany.battleshippresentacion.modelo.ModeloPartida;

/**
 *
 * @author victo
 */
public interface IVistaPartida {

    public void mostrarPartidaCreada(ModeloPartida partida);

    public void actualizarVista(ModeloPartida modelo);

    public void mostrarError(String mensaje);
   
}
