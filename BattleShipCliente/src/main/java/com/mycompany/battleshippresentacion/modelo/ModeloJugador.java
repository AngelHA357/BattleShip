package com.mycompany.battleshippresentacion.modelo;

/**
 *
 * @author victo
 */
public class ModeloJugador {

    private static final long serialVersionUID = 1L;
    private String id;
    private String nombre;

    public ModeloJugador(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
