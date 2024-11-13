
package org.itson.arquitectura.battleshipservidor.dominio.casilla;

import org.itson.arquitectura.battleshipservidor.dominio.Coordenada;

/**
 *
 * @author victo
 */
public class Casilla {
    
    private Coordenada coordenada;
    private CasillaFlyweight flyweight;

    public Casilla(Coordenada coordenada, CasillaFlyweight flyweight) {
        this.coordenada = coordenada;
        this.flyweight = flyweight;
    }
    
    
}
