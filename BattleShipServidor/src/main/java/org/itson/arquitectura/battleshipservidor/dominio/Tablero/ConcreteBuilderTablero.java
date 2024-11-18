
package org.itson.arquitectura.battleshipservidor.dominio.Tablero;

import java.util.ArrayList;
import java.util.List;
import org.itson.arquitectura.battleshipservidor.dominio.Disparo;
import org.itson.arquitectura.battleshipservidor.dominio.UbicacionNave;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;

/**
 *
 * @author victo
 */
public class ConcreteBuilderTablero implements Builder {
    private int alto;
    private int ancho;
    private List<Casilla> casillas;
    private List<UbicacionNave> ubicacionesNave;
    private List<Disparo> disparos;

    public ConcreteBuilderTablero() {
        reset();
    }

    @Override
    public Builder reset() {
        this.alto = 0;
        this.ancho = 0;
        this.casillas = new ArrayList<>();
        this.ubicacionesNave = new ArrayList<>();
        this.disparos = new ArrayList<>();
        return this;
    }

    @Override
    public Builder setAlto(int alto) {
        this.alto = alto;
        return this;
    }

    @Override
    public Builder setAncho(int ancho) {
        this.ancho = ancho;
        return this;
    }

    @Override
    public Builder addUbicacionNave(UbicacionNave ubicacion) {
        this.ubicacionesNave.add(ubicacion);
        return this;
    }

    @Override
    public Builder addCasilla(Casilla casilla) {
        this.casillas.add(casilla);
        return this;
    }

    @Override
    public Tablero build() {
        return new Tablero(alto, ancho, casillas, ubicacionesNave, disparos);
    }
}
