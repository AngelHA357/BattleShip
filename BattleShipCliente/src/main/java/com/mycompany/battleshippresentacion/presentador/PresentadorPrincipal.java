package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.modelo.ModeloPartida;
import com.mycompany.battleshippresentacion.vista.PantallaColocarBarcos;
import com.mycompany.battleshippresentacion.vista.PantallaDatosJugador;
import com.mycompany.battleshippresentacion.vista.PantallaIngresarCodigo;
import com.mycompany.battleshippresentacion.vista.PantallaInicio;
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

    public void mostrarPantallaInicio(){
        PantallaInicio pantallaInicio = new PantallaInicio();
        frame.dispose();
        pantallaInicio.setVisible(true);
    }
    
    public void mostrarPantallaColocarBarcos() throws Exception {
        PantallaColocarBarcos pantallaColocarBarcos = new PantallaColocarBarcos(frame);
        mostrarPantalla(pantallaColocarBarcos);
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

}
