
package org.itson.arquitectura.battleshipservidor.dominio;

import java.io.Serializable;

/**
 *
 * @author victo
 */
public class Coordenada implements Serializable{
    private int x;
    private int y;
    private static final long serialVersionUID = 1L;

    public Coordenada(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
}
