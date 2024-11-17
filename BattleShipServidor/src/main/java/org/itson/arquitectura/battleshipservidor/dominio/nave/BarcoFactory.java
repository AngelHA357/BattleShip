
package org.itson.arquitectura.battleshipservidor.dominio.nave;

/**
 *
 * @author victo
 */
public class BarcoFactory extends NaveFactory{

    public BarcoFactory() {
    }

    @Override
    public Nave createNave() {
        return new Barco();
    }
 
}
