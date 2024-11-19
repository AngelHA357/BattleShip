
package org.itson.arquitectura.battleshipservidor.dominio.casilla;

import org.itson.arquitectura.battleshiptransporte.enums.EstadoCasilla;

/**
 *
 * @author victo
 */
public class CasillaFlyweight {

    private final EstadoCasilla estado;

    public CasillaFlyweight(EstadoCasilla estado) {
        this.estado = estado;
    }

    public EstadoCasilla getEstado() {
        return estado;
    }
}
