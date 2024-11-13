
package org.itson.arquitectura.battleshipservidor.dominio;

import org.itson.arquitectura.battleshipservidor.dominio.enums.ResultadoDisparo;

/**
 *
 * @author victo
 */
public class Disparo {
    private Coordenada coordenada;
    private ResultadoDisparo resultado;

    public Disparo(Coordenada coordenada, ResultadoDisparo resultado) {
        this.coordenada = coordenada;
        this.resultado = resultado;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public ResultadoDisparo getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoDisparo resultado) {
        this.resultado = resultado;
    }
    
    
}
