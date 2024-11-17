
package org.itson.arquitectura.battleshipservidor.dominio.nave;

/**
 *
 * @author victo
 */
public class SubmarinoFactory extends NaveFactory{

    @Override
    public Nave createNave() {
        return new Submarino();
    }

}
