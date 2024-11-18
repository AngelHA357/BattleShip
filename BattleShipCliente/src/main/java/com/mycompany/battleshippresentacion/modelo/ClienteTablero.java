/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.modelo;

import java.util.List;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;

/**
 *
 * @author JoseH
 */
public class ClienteTablero {
    private int alto;
    private int ancho;
    private List<Casilla> casillas;

    public ClienteTablero(int alto, int ancho, List<Casilla> casillas) {
        this.alto = alto;
        this.ancho = ancho;
        this.casillas = casillas;
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
   
    
}
