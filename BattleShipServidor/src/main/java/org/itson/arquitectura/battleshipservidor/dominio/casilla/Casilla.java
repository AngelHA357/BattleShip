
package org.itson.arquitectura.battleshipservidor.dominio.casilla;

import java.io.Serializable;
import org.itson.arquitectura.battleshipservidor.dominio.Coordenada;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoCasilla;

/**
 *
 * @author victo
 */
public class Casilla implements Serializable{

    private static final long serialVersionUID = 1L;
    private EstadoCasilla estado; 
    private final Coordenada coordenada;
    private final CasillaFlyweight flyweight;


    public void setEstado(EstadoCasilla casilla) {
        this.estado = casilla;
    }

    public EstadoCasilla getEstado() {
        return estado;
    }
    
    public Casilla(Coordenada coordenada, CasillaFlyweight flyweight) {
        this.coordenada = coordenada;
        this.flyweight = flyweight;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public CasillaFlyweight getFlyweight() {
        return flyweight;
    }
    
    
    
}
