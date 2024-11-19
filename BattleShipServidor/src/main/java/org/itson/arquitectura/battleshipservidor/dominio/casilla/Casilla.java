
package org.itson.arquitectura.battleshipservidor.dominio.casilla;

import org.itson.arquitectura.battleshipservidor.dominio.Coordenada;
import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoCasilla;

/**
 *
 * @author victo
 */
public class Casilla {

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
