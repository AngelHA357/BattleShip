
package org.itson.arquitectura.battleshipservidor.dominio.nave;

import org.itson.arquitectura.battleshiptransporte.enums.EstadoNave;
import org.itson.arquitectura.battleshiptransporte.enums.Orientacion;

/**
 *
 * @author victo
 */
public abstract class Nave {
    
    private String nombre;
    private int tamano;
    private Orientacion orientacion;
    private EstadoNave estado;

    public Nave() {
    }
    
    public Nave(String nombre, int tamano, Orientacion orientacion, EstadoNave estado) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.orientacion = orientacion;
        this.estado = estado;
    }
    
    public Nave(String nombre, int tamano){
        this.nombre = nombre;
        this.tamano = tamano;
        orientacion = Orientacion.HORIZONTAL;
        estado = EstadoNave.SIN_DANOS;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTamano() {
        return tamano;
    }

    public Orientacion getOrientacion() {
        return orientacion;
    }

    public EstadoNave getEstado() {
        return estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public void setOrientacion(Orientacion orientacion) {
        this.orientacion = orientacion;
    }

    public void setEstado(EstadoNave estado) {
        this.estado = estado;
    }
    
    
    
}
