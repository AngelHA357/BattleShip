/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.modelo;

import java.util.Map;

/**
 *
 * @author JoseH
 */
public class ClienteNave {
    private String nombre;
    private int tamano;
    private String orientacion, estado;

    public ClienteNave() {
    }
    
    public ClienteNave(String nombre, int tamano){
        this.nombre = nombre;
        this.tamano = tamano;
    }

    public ClienteNave(String nombre, int tamano, String orientacion, String estado) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.orientacion = orientacion;
        this.estado = estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTamano() {
        return tamano;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public String getEstado() {
        return estado;
    }
    
    
    
}
