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
    private int[][] casillas;

    public ClienteTablero(int alto, int ancho, int[][] casillas) {
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

    public int[][] getCasillas() {
        return casillas;
    }
    
    public void imprimirTablero() {
        System.out.println("   " + "0123456789".substring(0, ancho)); // Encabezado de columnas
        for (int i = 0; i < alto; i++) {
            System.out.printf("%2d ", i); // Índice de fila
            for (int j = 0; j < ancho; j++) {
                char simbolo = obtenerSimbolo(casillas[i][j]);
                System.out.print(simbolo);
            }
            System.out.println(); // Nueva línea después de cada fila
        }
    }

    private char obtenerSimbolo(int valor) {
        switch (valor) {
            case 0:
                return '.'; // Casilla vacía
            case 1:
                return 'O'; // Parte de una nave
            case 2:
                return 'X'; // Impacto
            case 3:
                return '~'; // Agua fallida
            default:
                return '?'; // Valor desconocido
        }
    }
    
}
