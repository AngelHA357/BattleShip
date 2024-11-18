
package org.itson.arquitectura.battleshipservidor.dominio.casilla;

import org.itson.arquitectura.battleshipservidor.dominio.Coordenada;

/**
 *
 * @author victo
 */
public class Casilla {

    private final Coordenada coordenada;
    private final CasillaFlyweight flyweight;

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
