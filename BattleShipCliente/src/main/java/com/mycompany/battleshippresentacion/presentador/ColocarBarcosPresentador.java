/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.modelo.ClienteNave;
import com.mycompany.battleshippresentacion.modelo.ClienteTablero;
import com.mycompany.battleshippresentacion.modelo.ModeloColocacionNaves;
import com.mycompany.battleshippresentacion.modelo.ModeloCreacionNaves;
import com.mycompany.battleshippresentacion.modelo.ModeloCreacionTablero;
import com.mycompany.battleshippresentacion.vista.PantallaColocarBarcos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JButton;
import org.itson.arquitectura.battleshipservidor.dominio.Coordenada;
import org.itson.arquitectura.battleshipservidor.dominio.Tablero.Tablero;
import org.itson.arquitectura.battleshipservidor.dominio.UbicacionNave;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.CasillaFlyweight;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.CasillaFlyweightFactory;
import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoCasilla;
import org.itson.arquitectura.battleshipservidor.dominio.nave.Nave;

/**
 *
 * @author JoseH
 */
public class ColocarBarcosPresentador {

    private final PantallaColocarBarcos vista;
    private final ModeloCreacionNaves navesModelo;
    private final ModeloCreacionTablero modelo;
    private final ModeloColocacionNaves modeloColocarNaves;
    private String naveSeleccionada;
    private int orientacionActual;

    private List<ClienteNave> naves;
    private ClienteTablero tablero;

    public ColocarBarcosPresentador(PantallaColocarBarcos vista) {
        this.vista = vista;
        navesModelo = new ModeloCreacionNaves();
        modelo = new ModeloCreacionTablero();
        orientacionActual = 0;
        modeloColocarNaves = new ModeloColocacionNaves();
    }

    public void inicializarJuego() {
        vista.crearTablero();

    }

    public void crearNaves(){
        try {
            //Primero se solicita al modelo crear las naves
            naves = navesModelo.crearNaves();
        } catch (Exception ex) {
            Logger.getLogger(ColocarBarcosPresentador.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Finalmente se le notifica a la vista para que se muestren las naves
        vista.crearNaves();
    }
    
    
    public void seleccionarNave(String tipoNave) {
        this.naveSeleccionada = tipoNave;
    }
    
    public void colocarNave(int fila, int columna, String tipoNave, int orientacion, int tamano) throws Exception{
        Map<String, Object> data = new HashMap<>();
        data.put("coordenadaX", fila);
        data.put("coordenadaY", columna);
        data.put("tipoNave", tipoNave);
        data.put("orientacion", orientacion);
        data.put("tamano", tamano);
        
        modeloColocarNaves.enviarColocacionNave(data);
    }
}
