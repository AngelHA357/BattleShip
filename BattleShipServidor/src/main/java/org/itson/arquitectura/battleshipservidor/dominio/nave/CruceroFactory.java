
package org.itson.arquitectura.battleshipservidor.dominio.nave;

/**
 *
 * @author victo
 */
public class CruceroFactory extends NaveFactory{

    public CruceroFactory() {
    }

    @Override
    public Nave createNave() {
        return new Crucero();
    }

}
