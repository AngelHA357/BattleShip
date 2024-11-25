package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.modelo.ModeloPartida;
import com.mycompany.battleshippresentacion.vista.PantallaColocarBarcos;
import com.mycompany.battleshippresentacion.vista.PantallaDatosJugador;
import com.mycompany.battleshippresentacion.vista.PantallaIngresarCodigo;
import com.mycompany.battleshippresentacion.vista.PantallaInicio;
import com.mycompany.battleshippresentacion.vista.PantallaJugarPartida;
import com.mycompany.battleshippresentacion.vista.PantallaMostrarCodigo;
import com.mycompany.battleshippresentacion.vista.PantallaOpcionPartida;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author victo
 */
public class PresentadorPrincipal {

    private final JFrame frame;
    private String idJugador;

    public PresentadorPrincipal(JFrame frame) {
        this.frame = frame;
    }

    private void mostrarPantalla(JPanel nuevaPantalla) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(nuevaPantalla, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        nuevaPantalla.setVisible(true);
    }

    public void mostrarPantallaInicio() {
        PantallaInicio pantallaInicio = new PantallaInicio();
        frame.dispose();
        pantallaInicio.setVisible(true);
    }

    public void mostrarPantallaColocarBarcos() throws Exception {
        try {
            System.out.println("Creando pantalla colocar barcos...");
            PantallaColocarBarcos pantallaColocarBarcos = new PantallaColocarBarcos(frame, idJugador);
            System.out.println("Mostrando pantalla colocar barcos");
            mostrarPantalla(pantallaColocarBarcos);
        } catch (Exception e) {
            System.out.println("Error al mostrar pantalla colocar barcos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarPantallaDatosJugador() {
        PantallaDatosJugador pantallaDatosJugador = new PantallaDatosJugador(frame);
        mostrarPantalla(pantallaDatosJugador);
    }

    public void mostrarPantallaIngresarCodigo() {
        PantallaIngresarCodigo pantallaIngresarCodigo = new PantallaIngresarCodigo(frame);
        mostrarPantalla(pantallaIngresarCodigo);
    }

    public void mostrarPantallaMostrarCodigo(ModeloPartida modelo) {
        PantallaMostrarCodigo pantallaMostrarCodigo = new PantallaMostrarCodigo(frame, modelo);
        mostrarPantalla(pantallaMostrarCodigo);
    }

    public void mostrarPantallaOpcionPartida() {
        PantallaOpcionPartida pantallaOpcionPartida = new PantallaOpcionPartida(frame);
        mostrarPantalla(pantallaOpcionPartida);
    }

    public void mostrarPantallaJugarPartida(boolean esTurnoPropio) {
        PantallaJugarPartida pantallaJugarPartida = new PantallaJugarPartida(frame);
        pantallaJugarPartida.getPresentador().setIdJugador(idJugador);
        pantallaJugarPartida.getPresentador().inicializarTurno(esTurnoPropio);
        mostrarPantalla(pantallaJugarPartida);
    }

    public void setIdJugador(String idJugador) {
        this.idJugador = idJugador;
    }

}
