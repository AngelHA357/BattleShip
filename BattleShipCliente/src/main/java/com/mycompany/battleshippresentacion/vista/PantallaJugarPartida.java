/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.battleshippresentacion.vista;

import com.mycompany.battleshippresentacion.ivista.IVistaJugarPartida;
import com.mycompany.battleshippresentacion.presentador.PresentadorDisparo;
import com.mycompany.battleshippresentacion.presentador.PresentadorPrincipal;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author PC
 */
public class PantallaJugarPartida extends javax.swing.JPanel implements IVistaJugarPartida {

    private JButton[][] casillasPropio = new JButton[10][10];
    private JButton[][] casillasDisparos = new JButton[10][10];
    private PresentadorDisparo presentador;
    private JFrame framePrincipal;
    private PresentadorPrincipal navegacion;

    /**
     * Creates new form PantallaJugarPartida
     */
    public PantallaJugarPartida(JFrame framePrincipal) {
        this.framePrincipal = framePrincipal;
        this.navegacion = new PresentadorPrincipal(framePrincipal);
        initComponents();
        this.presentador = new PresentadorDisparo(this);
        crearTablerosDeJuego();
    }

    public void crearTablerosDeJuego() {
        try {
            Font fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Micro5-Regular.ttf"));
            fuentePersonalizada = fuentePersonalizada.deriveFont(50f);

            int anchoTablero = 400;
            int altoTablero = 400;
            int espacioEntreTableros = 100;

            int yPos = (800 - altoTablero) / 2;
            int xPosTablero1 = 300;
            int xPosTablero2 = xPosTablero1 + anchoTablero + espacioEntreTableros;

            // Crear el primer tablero (naves propias)
            JPanel panelTablero1 = crearPanelTablero(anchoTablero, altoTablero, false);
            panelTablero1.setBounds(xPosTablero1, yPos, anchoTablero, altoTablero);

            // Crear el segundo tablero (disparos)
            JPanel panelTablero2 = crearPanelTablero(anchoTablero, altoTablero, true);
            panelTablero2.setBounds(xPosTablero2, yPos, anchoTablero, altoTablero);

            crearEtiquetasTablero(xPosTablero1, yPos, anchoTablero, fuentePersonalizada, "TU FLOTA");
            crearEtiquetasTablero(xPosTablero2, yPos, anchoTablero, fuentePersonalizada, "DISPAROS");

            add(panelTablero1);
            add(panelTablero2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel crearPanelTablero(int ancho, int alto, boolean esTableroDisparos) {
        JPanel panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(10, 10));
        panelTablero.setPreferredSize(new Dimension(ancho, alto));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton casilla = new JButton();
                casilla.setPreferredSize(new Dimension(ancho / 10, alto / 10));
                casilla.setBackground(new Color(139, 69, 19));

                if (esTableroDisparos) {
                    final int fila = i;
                    final int columna = j;
                    casilla.addActionListener(e -> {
                        try {
                            presentador.enviarDisparo(fila, columna);
                        } catch (Exception ex) {
                            mostrarError("Error al realizar disparo: " + ex.getMessage());
                        }
                    });
                }

                panelTablero.add(casilla);
                if (!esTableroDisparos) {
                    casillasPropio[i][j] = casilla;
                } else {
                    casillasDisparos[i][j] = casilla;
                }
            }
        }

        return panelTablero;
    }

    private void crearEtiquetasTablero(int xPos, int yPos, int anchoTablero, Font fuente, String titulo) {
        JLabel labelTitulo = new JLabel(titulo, SwingConstants.CENTER);
        labelTitulo.setFont(fuente.deriveFont(30f));
        labelTitulo.setBounds(xPos, yPos - 50, anchoTablero, 40);
        add(labelTitulo);

        JPanel panelEtiquetasSuperior = new JPanel();
        panelEtiquetasSuperior.setLayout(new GridLayout(1, 10));
        panelEtiquetasSuperior.setBounds(xPos, yPos - 30, anchoTablero, 30);
        panelEtiquetasSuperior.setBackground(Color.WHITE);

        for (char letra = 'A'; letra <= 'J'; letra++) {
            JLabel etiqueta = new JLabel(String.valueOf(letra), SwingConstants.CENTER);
            etiqueta.setFont(fuente.deriveFont(20f));
            panelEtiquetasSuperior.add(etiqueta);
        }

        JPanel panelEtiquetasLateral = new JPanel();
        panelEtiquetasLateral.setLayout(new GridLayout(10, 1));
        panelEtiquetasLateral.setBounds(xPos - 30, yPos, 30, anchoTablero);
        panelEtiquetasLateral.setBackground(Color.WHITE);

        for (int numero = 1; numero <= 10; numero++) {
            JLabel etiqueta = new JLabel(String.valueOf(numero), SwingConstants.CENTER);
            etiqueta.setFont(fuente.deriveFont(20f));
            panelEtiquetasLateral.add(etiqueta);
        }

        add(panelEtiquetasSuperior);
        add(panelEtiquetasLateral);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelAbandonar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labelNavesIntactas = new javax.swing.JLabel();
        labelNavesDañadas = new javax.swing.JLabel();
        labelNavesDestruidas = new javax.swing.JLabel();
        labelTurno = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        labelNavesIntactasRival = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        labelNavesDestruidasRival = new javax.swing.JLabel();
        labelNavesDañadasRival = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(null);

        labelAbandonar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/botonAbandonar.png"))); // NOI18N
        add(labelAbandonar);
        labelAbandonar.setBounds(1070, 750, 220, 60);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/hojasDecoracion.png"))); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(820, 0, 479, 430);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Green Rectangle.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        add(jLabel1);
        jLabel1.setBounds(40, 80, 20, 20);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Yellow Rectangle.png"))); // NOI18N
        jLabel3.setText("jLabel1");
        add(jLabel3);
        jLabel3.setBounds(40, 20, 20, 20);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Red Rectangle.png"))); // NOI18N
        jLabel4.setText("jLabel1");
        add(jLabel4);
        jLabel4.setBounds(40, 50, 20, 20);

        labelNavesIntactas.setText("11");
        add(labelNavesIntactas);
        labelNavesIntactas.setBounds(70, 80, 12, 16);

        labelNavesDañadas.setText("0");
        add(labelNavesDañadas);
        labelNavesDañadas.setBounds(70, 20, 6, 16);

        labelNavesDestruidas.setText("0");
        add(labelNavesDestruidas);
        labelNavesDestruidas.setBounds(70, 50, 6, 16);

        labelTurno.setText("Tu turno");
        add(labelTurno);
        labelTurno.setBounds(620, 750, 110, 40);

        jLabel6.setText("Jugador Dos");
        add(jLabel6);
        jLabel6.setBounds(1000, 120, 70, 16);

        labelNavesIntactasRival.setText("11");
        add(labelNavesIntactasRival);
        labelNavesIntactasRival.setBounds(860, 80, 12, 16);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Green Rectangle.png"))); // NOI18N
        jLabel7.setText("jLabel1");
        add(jLabel7);
        jLabel7.setBounds(830, 80, 20, 20);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Red Rectangle.png"))); // NOI18N
        jLabel8.setText("jLabel1");
        add(jLabel8);
        jLabel8.setBounds(830, 50, 20, 20);

        labelNavesDestruidasRival.setText("0");
        add(labelNavesDestruidasRival);
        labelNavesDestruidasRival.setBounds(860, 50, 6, 16);

        labelNavesDañadasRival.setText("0");
        add(labelNavesDañadasRival);
        labelNavesDañadasRival.setBounds(860, 20, 6, 16);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Yellow Rectangle.png"))); // NOI18N
        jLabel9.setText("jLabel1");
        add(jLabel9);
        jLabel9.setBounds(830, 20, 20, 20);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondoArena.png"))); // NOI18N
        add(jLabel10);
        jLabel10.setBounds(0, 640, 1300, 190);

        jLabel11.setText("Jugador Uno");
        add(jLabel11);
        jLabel11.setBounds(220, 120, 70, 16);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel labelAbandonar;
    private javax.swing.JLabel labelNavesDañadas;
    private javax.swing.JLabel labelNavesDañadasRival;
    private javax.swing.JLabel labelNavesDestruidas;
    private javax.swing.JLabel labelNavesDestruidasRival;
    private javax.swing.JLabel labelNavesIntactas;
    private javax.swing.JLabel labelNavesIntactasRival;
    private javax.swing.JLabel labelTurno;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actualizarCasillaDisparo(int fila, int columna, String resultado) {
        // Actualizar el color
        Color color;
        switch (resultado) {
            case "AGUA":
                color = Color.BLUE;
                break;
            case "IMPACTO":
                color = Color.RED;
                break;
            case "HUNDIDO":
                color = Color.DARK_GRAY;
                break;
            default:
                color = new Color(139, 69, 19);
        }
        casillasDisparos[fila][columna].setBackground(color);
    }

    @Override
    public void actualizarContadoresNavesPropio(int navesIntactas, int navesDanadas, int navesDestruidas) {
        labelNavesIntactas.setText(String.valueOf(navesIntactas));
        labelNavesDañadas.setText(String.valueOf(navesDanadas));
        labelNavesDestruidas.setText(String.valueOf(navesDestruidas));
    }

    @Override
    public void actualizarContadoresNavesRival(int navesIntactas, int navesDanadas, int navesDestruidas) {
        labelNavesIntactasRival.setText(String.valueOf(navesIntactas));
        labelNavesDañadasRival.setText(String.valueOf(navesDanadas));
        labelNavesDestruidasRival.setText(String.valueOf(navesDestruidas));
    }

    @Override
    public void actualizarCasillaPropia(int fila, int columna, String estado) {
        Color color;
        switch (estado) {
            case "AGUA":
                color = Color.BLUE;
                break;
            case "IMPACTO":
                color = Color.RED;
                break;
            case "HUNDIDO":
                color = Color.DARK_GRAY;
                break;
            default:
                return;
        }
        casillasPropio[fila][columna].setBackground(color);
    }

    @Override
    public void mostrarFinJuego(String ganador) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "¡Fin del juego! Ganador: " + ganador,
                "Fin del Juego",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this,
                mensaje,
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actualizarTurno(boolean esTurnoPropio) {
        String mensaje = esTurnoPropio ? "Tu turno" : "Turno del rival";
        this.labelTurno.setText(mensaje);
    }

    @Override
    public void habilitarTableroDisparos(boolean habilitado) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                casillasDisparos[i][j].setEnabled(habilitado);
            }
        }
    }
}
