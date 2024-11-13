
package org.itson.arquitectura.battleshipservidor.dominio.nave;

import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoNave;
import org.itson.arquitectura.battleshipservidor.dominio.enums.Orientacion;

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
    
    
}
