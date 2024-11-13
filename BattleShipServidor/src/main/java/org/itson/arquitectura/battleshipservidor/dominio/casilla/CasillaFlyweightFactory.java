
package org.itson.arquitectura.battleshipservidor.dominio.casilla;

import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoCasilla;
import java.util.Map;

/**
 *
 * @author victo
 */
public class CasillaFlyweightFactory {
    
    private Map<EstadoCasilla, CasillaFlyweight> cache;

    public CasillaFlyweightFactory(Map<EstadoCasilla, CasillaFlyweight> cache) {
        this.cache = cache;
    }
    
    
}
