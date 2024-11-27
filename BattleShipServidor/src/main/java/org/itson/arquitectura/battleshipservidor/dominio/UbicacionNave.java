package org.itson.arquitectura.battleshipservidor.dominio;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;
import org.itson.arquitectura.battleshipservidor.dominio.nave.Nave;

/**
 *
 * @author victo
 */
public class UbicacionNave implements Serializable {

    private static final long serialVersionUID = 1L;
    private Nave nave;
    private Map<Casilla, Boolean> casillasOcupadas;

    public UbicacionNave() {
        this.casillasOcupadas = new HashMap<>();
    }

    public UbicacionNave(Nave nave, Map<Casilla, Boolean> casillasOcupadas) {
        this.nave = nave;
        this.casillasOcupadas = casillasOcupadas;
    }

    public Nave getNave() {
        return nave;
    }

    public void setNave(Nave nave) {
        this.nave = nave;
    }

    public Map<Casilla, Boolean> getCasillasOcupadas() {
        return casillasOcupadas;
    }

    public void setCasillasOcupadas(Map<Casilla, Boolean> casillasOcupadas) {
        this.casillasOcupadas = new HashMap<>();
        casillasOcupadas.forEach((casilla, ocupada)
                -> this.casillasOcupadas.put(casilla, false));
    }

}
