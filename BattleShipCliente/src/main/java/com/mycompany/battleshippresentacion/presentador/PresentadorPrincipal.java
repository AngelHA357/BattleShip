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
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;

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

    public void conectarse() {
        try {
            SocketCliente socketCliente = SocketCliente.getInstance();
            if (!socketCliente.conectar("localhost")) {
                throw new Exception("No se pudo conectar al servidor");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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

    public void mostrarPantallaMostrarCodigo(String codigo) {
        PantallaMostrarCodigo pantallaMostrarCodigo = new PantallaMostrarCodigo(frame, codigo);
        mostrarPantalla(pantallaMostrarCodigo);
    }

    public void mostrarPantallaOpcionPartida() {
        PantallaOpcionPartida pantallaOpcionPartida = new PantallaOpcionPartida(frame);
        mostrarPantalla(pantallaOpcionPartida);
    }

    public void mostrarPantallaJugarPartida(boolean esTurnoPropio, ColocarBarcosPresentador colocarBarcosPresentador) {
        if (this.idJugador == null || this.idJugador.trim().isEmpty()) {
            throw new IllegalStateException("ID de jugador no establecido");
        }

        System.out.println("Mostrando pantalla jugar partida con ID: " + this.idJugador);

        try {
            PantallaJugarPartida pantallaJugarPartida = new PantallaJugarPartida(frame, this.idJugador, this);
            pantallaJugarPartida.getPresentador().setClienteTablero(colocarBarcosPresentador.getClienteTablero());
            pantallaJugarPartida.crearTablerosDeJuego();
            pantallaJugarPartida.colocarNaves();
            pantallaJugarPartida.getPresentador().inicializarTurno(esTurnoPropio);
            mostrarPantalla(pantallaJugarPartida);
        } catch (Exception e) {
            System.out.println("Error al mostrar pantalla jugar partida: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setIdJugador(String idJugador) {
        if (idJugador == null || idJugador.trim().isEmpty()) {
            throw new IllegalArgumentException("ID de jugador inválido");
        }
        System.out.println("Estableciendo ID en PresentadorPrincipal: " + idJugador);
        this.idJugador = idJugador;
    }
}
