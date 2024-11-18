
package org.itson.arquitectura.battleshipservidor.dominio.Tablero;

import org.itson.arquitectura.battleshipservidor.dominio.UbicacionNave;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;

/**
 *
 * @author victo
 */
public interface Builder {
    Builder reset();
    Builder setAlto(int alto);
    Builder setAncho(int ancho);
    Builder addUbicacionNave(UbicacionNave ubicacion);
    Builder addCasilla(Casilla casilla);
    Tablero build();
}
