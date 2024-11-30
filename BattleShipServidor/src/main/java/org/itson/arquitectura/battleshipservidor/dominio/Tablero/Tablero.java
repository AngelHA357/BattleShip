package org.itson.arquitectura.battleshipservidor.dominio.Tablero;

import java.io.Serializable;
import java.util.List;
import org.itson.arquitectura.battleshipservidor.dominio.Disparo;
import org.itson.arquitectura.battleshipservidor.dominio.UbicacionNave;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;

/**
 *
 * @author victo
 */
public class Tablero implements Serializable {

    private static final long serialVersionUID = 1L;
    private int alto;
    private int ancho;
    private List<Casilla> casillas;
    private List<UbicacionNave> ubicacionesNave;
    private List<Disparo> disparos;

    public Tablero(int alto, int ancho) {
        this.alto = alto;
        this.ancho = ancho;
    }

    public Tablero(int alto, int ancho, List<Casilla> casillas, List<UbicacionNave> ubicacionesNave, List<Disparo> disparos) {
        this.alto = alto;
        this.ancho = ancho;
        this.casillas = casillas;
        this.ubicacionesNave = ubicacionesNave;
        this.disparos = disparos;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public void setCasillas(List<Casilla> casillas) {
        this.casillas = casillas;
    }

    public void setUbicacionesNave(List<UbicacionNave> ubicacionesNave) {
        this.ubicacionesNave = ubicacionesNave;
    }

    public void setDisparos(List<Disparo> disparos) {
        this.disparos = disparos;
    }

    public int getAlto() {
        return alto;
    }

    public int getAncho() {
        return ancho;
    }

    public List<Casilla> getCasillas() {
        return casillas;
    }

    public List<UbicacionNave> getUbicacionesNave() {
        return ubicacionesNave;
    }

    public List<Disparo> getDisparos() {
        return disparos;
    }

    public boolean tieneNave(int x, int y) {
        System.out.println("Verificando nave en posición [" + x + "," + y + "]");

        boolean resultado = ubicacionesNave.stream()
                .anyMatch(ubicacion -> ubicacion.getCasillasOcupadas().keySet().stream()
                .anyMatch(casilla -> {
                    boolean coincide = casilla.getCoordenada().getX() == y
                            && casilla.getCoordenada().getY() == x;
                    if (coincide) {
                        System.out.println("Nave encontrada en [" + x + "," + y + "]");
                    }
                    return coincide;
                }));

        System.out.println("Resultado verificación para [" + x + "," + y + "]: " + resultado);
        return resultado;
    }

    public UbicacionNave obtenerNaveEnPosicion(int x, int y) {
        System.out.println("Buscando nave en posición [" + x + "," + y + "]");
        for (UbicacionNave ubicacion : ubicacionesNave) {
            for (Casilla casilla : ubicacion.getCasillasOcupadas().keySet()) {
                boolean coincide = casilla.getCoordenada().getX() == y
                        && casilla.getCoordenada().getY() == x;
                if (coincide) {
                    System.out.println("Nave encontrada en ubicación");
                    return ubicacion;
                }
            }
        }
        return null;
    }

    public void marcarImpacto(int x, int y) {
        ubicacionesNave.stream()
                .filter(ubicacion -> ubicacion.getCasillasOcupadas().keySet().stream()
                .anyMatch(casilla -> casilla.getCoordenada().getX() == x
                && casilla.getCoordenada().getY() == y))
                .findFirst()
                .ifPresent(ubicacion -> {
                    Casilla casillaImpactada = ubicacion.getCasillasOcupadas().keySet().stream()
                            .filter(casilla -> casilla.getCoordenada().getX() == x
                            && casilla.getCoordenada().getY() == y)
                            .findFirst()
                            .orElse(null);
                    if (casillaImpactada != null) {
                        ubicacion.getCasillasOcupadas().put(casillaImpactada, true);
                        System.out.println("Impacto marcado en [" + x + "," + y + "]");
                    }
                });
    }

    public void imprimirEstado() {
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                boolean hayNave = tieneNave(j, i);
                System.out.print(hayNave ? "O" : ".");
            }
            System.out.println();
        }
    }
}
