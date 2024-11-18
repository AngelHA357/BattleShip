/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.modelo.ClienteNave;
import com.mycompany.battleshippresentacion.modelo.ClienteTablero;
import com.mycompany.battleshippresentacion.modelo.ModeloCreacionNaves;
import com.mycompany.battleshippresentacion.modelo.ModeloTablero;
import com.mycompany.battleshippresentacion.vista.PantallaColocarBarcos;
import java.util.List;

/**
 *
 * @author JoseH
 */
public class ColocarBarcosPresentador {
    private final PantallaColocarBarcos vista;
    private final ModeloCreacionNaves navesModelo;
    private final ModeloTablero modelo;
    private String naveSeleccionada;
    private int orientacionActual;
    
    private List<ClienteNave> naves;
    private ClienteTablero tablero;
    
    public ColocarBarcosPresentador(PantallaColocarBarcos vista){
        this.vista = vista;
        navesModelo = new ModeloCreacionNaves();
        modelo = new ModeloTablero();
        orientacionActual = 0;
    }
    
    public void inicializarJuego(){
//        tablero = modelo.inicializarTablero();
        vista.crearTablero();
        
    }
    
    public void crearNaves(){
        //Primero se solicita al modelo crear las naves
        naves = navesModelo.crearNaves();      
        //Finalmente se le notifica a la vista para que se muestren las naves
        vista.crearNaves();
    }
    
    public void seleccionarNave(String tipoNave){
        this.naveSeleccionada = tipoNave;
    }
    
//    public void colocarNave(int fila, int columna, int tamano){
//        ClienteNave nave = navesModelo.obtenerNave(naveSeleccionada);
//        if (modelo.puedeColocarNave(fila, columna, nave.getTamano(), orientacionActual)) {
//            modelo.colocarNave(fila, columna, nave, orientacionActual);
//            vista.colocarNaveEnCasillas(fila, columna, tamano);
//            modelo.enviarEventoColocacion();
//        }
//    }
//    
//    public void rotarNave(int fila, int columna, int tamano){
//        if (modelo.hayNaveEnCasilla(fila, columna)) {
//            ClienteNave nave = modelo.obtenerNaveEn(fila, columna);
//            int nuevaOrientacion = (orientacionActual + 1) % 4;
//            
//            if (modelo.puedeRotarNave(nave, nuevaOrientacion)) {
//                orientacionActual = nuevaOrientacion;
//                modelo.rotarNave(nave, nuevaOrientacion);
//                vista.colocarNaveEnCasillas(fila, columna, tamano);
//                modelo.enviarEventoRotacion(nave, nuevaOrientacion);
//            }
//        }
//    }
}
