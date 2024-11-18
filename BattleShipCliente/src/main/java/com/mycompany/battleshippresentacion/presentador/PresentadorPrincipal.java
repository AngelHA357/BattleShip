
package com.mycompany.battleshippresentacion.presentador;

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

    public void mostrarPantalla(JPanel nuevaPantalla) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(nuevaPantalla, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        nuevaPantalla.setVisible(true);
    }
}
