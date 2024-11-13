
package org.itson.arquitectura.battleshipservidor.dominio.Tablero;

import java.util.List;
import org.itson.arquitectura.battleshipservidor.dominio.Disparo;
import org.itson.arquitectura.battleshipservidor.dominio.UbicacionNave;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;

/**
 *
 * @author victo
 */
public class Tablero {
    
    private int alto;
    private int ancho;
    private List<Casilla> casillas;
    private List<UbicacionNave> ubicacionesNave;
    private List<Disparo> disparos;

    public Tablero(int alto, int ancho, List<Casilla> casillas, List<UbicacionNave> ubicacionesNave, List<Disparo> disparos) {
        this.alto = alto;
        this.ancho = ancho;
        this.casillas = casillas;
        this.ubicacionesNave = ubicacionesNave;
        this.disparos = disparos;
    }
    
    
}
