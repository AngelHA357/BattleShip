
package org.itson.arquitectura.battleshipservidor.dominio.Tablero;

import org.itson.arquitectura.battleshipservidor.dominio.UbicacionNave;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;

/**
 *
 * @author victo
 */
public interface Builder {
    
    public void reset();
    
    public void setAlto(int alto);
    
    public void setAncho(int ancho);
    
    public void addUbicacionNave(UbicacionNave ubicacion);
    
    public void addCasilla(Casilla casilla);
}
