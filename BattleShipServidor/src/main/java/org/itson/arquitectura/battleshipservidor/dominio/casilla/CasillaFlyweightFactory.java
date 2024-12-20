
package org.itson.arquitectura.battleshipservidor.dominio.casilla;

import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshiptransporte.enums.EstadoCasilla;

/**
 *
 * @author victo
 */
public class CasillaFlyweightFactory {
    
    private Map<EstadoCasilla, CasillaFlyweight> cache;

    public CasillaFlyweightFactory() {
        this.cache = new HashMap<>();
    }
    
    public CasillaFlyweight getFlyweight(EstadoCasilla estado) {
        // Verifica si ya existe un Flyweight para el estado
        if (!cache.containsKey(estado)) {
            cache.put(estado, new CasillaFlyweight(estado));
        }
        return cache.get(estado);
    }
    
    
}
