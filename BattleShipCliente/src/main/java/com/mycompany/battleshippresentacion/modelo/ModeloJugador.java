package com.mycompany.battleshippresentacion.modelo;

/**
 *
 * @author victo
 */
public class ModeloJugador {

    private String id;
    private String nombre;
    private String color;
    private String nombreRival;
    

    public ModeloJugador(String id, String nombre, String color) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setNombreRival(String nombreRival) {
        this.nombreRival = nombreRival;
    }

    public String getNombreRival() {
        return nombreRival;
    }
    
    
    
    
}
