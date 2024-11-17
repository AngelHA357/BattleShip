
package org.itson.arquitectura.battleshipservidor.dominio.nave;

/**
 *
 * @author victo
 */
public class PortaAvionesFactory extends NaveFactory{

    public PortaAvionesFactory() {
    }

    @Override
    public Nave createNave() {
        return new PortaAviones();
    }
    
}
