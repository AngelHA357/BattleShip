/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.battleshippresentacion.vista;

import com.mycompany.battleshippresentacion.ivista.IVistaDatosJugador;
import com.mycompany.battleshippresentacion.modelo.ModeloPartida;
import com.mycompany.battleshippresentacion.presentador.PresentadorJugador;
import com.mycompany.battleshippresentacion.presentador.PresentadorPrincipal;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author JoseH
 */
public class PantallaDatosJugador extends javax.swing.JPanel implements IVistaDatosJugador {

    private PresentadorPrincipal navegacion;
    private PresentadorJugador presentadorJugador;
    private JFrame framePrincipal;

    /**
     * Creates new form PantallaDatosJugador
     *
     * @param framePrincipal
     */
    public PantallaDatosJugador(JFrame framePrincipal) {
        this.framePrincipal = framePrincipal;
        this.navegacion = new PresentadorPrincipal(framePrincipal);
        this.presentadorJugador = new PresentadorJugador(this, navegacion);
        initComponents();
        cargarFuentes();
    }

    private void cargarFuentes() {
        try {
            Font fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Micro5-Regular.ttf"));
            fuentePersonalizada = fuentePersonalizada.deriveFont(45f);
            lblNombre.setFont(fuentePersonalizada);
            lblColor.setFont(fuentePersonalizada);

            fuentePersonalizada = fuentePersonalizada.deriveFont(30f);
            txtNombre.setFont(fuentePersonalizada);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        lblEsperando = new javax.swing.JLabel();
        lblColor = new javax.swing.JLabel();
        btnContinuar = new javax.swing.JLabel();
        radioRojo = new javax.swing.JRadioButton();
        radioAzul = new javax.swing.JRadioButton();
        lblRojo = new javax.swing.JLabel();
        lblAzul = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblPergamino = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1440, 800));
        setMinimumSize(new java.awt.Dimension(1440, 800));
        setPreferredSize(new java.awt.Dimension(1440, 800));
        setLayout(null);

        lblEsperando.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        lblEsperando.setText("...");
        add(lblEsperando);
        lblEsperando.setBounds(710, 600, 30, 50);

        lblColor.setFont(new java.awt.Font("Segoe UI", 0, 25)); // NOI18N
        lblColor.setText("Elegir color:");
        add(lblColor);
        lblColor.setBounds(490, 320, 200, 40);

        btnContinuar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/btnContinuar.png"))); // NOI18N
        btnContinuar.setLabelFor(btnContinuar);
        btnContinuar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnContinuarMouseClicked(evt);
            }
        });
        add(btnContinuar);
        btnContinuar.setBounds(570, 580, 260, 80);

        buttonGroup1.add(radioRojo);
        radioRojo.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        add(radioRojo);
        radioRojo.setBounds(790, 530, 20, 20);

        buttonGroup1.add(radioAzul);
        radioAzul.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        add(radioAzul);
        radioAzul.setBounds(620, 530, 20, 20);

        lblRojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconoRojo.png"))); // NOI18N
        add(lblRojo);
        lblRojo.setBounds(720, 390, 140, 120);

        lblAzul.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconoAzul.png"))); // NOI18N
        add(lblAzul);
        lblAzul.setBounds(550, 380, 150, 130);

        lblNombre.setFont(new java.awt.Font("Segoe UI", 0, 25)); // NOI18N
        lblNombre.setText("Nombre:");
        add(lblNombre);
        lblNombre.setBounds(490, 180, 120, 40);

        txtNombre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));
        txtNombre.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        add(txtNombre);
        txtNombre.setBounds(490, 230, 400, 60);

        lblPergamino.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pergamino.png"))); // NOI18N
        add(lblPergamino);
        lblPergamino.setBounds(370, 10, 668, 780);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondoInicio.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1440, 1030);
    }// </editor-fold>//GEN-END:initComponents

    private void btnContinuarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnContinuarMouseClicked
         try {
            if (!txtNombre.getText().isBlank() && (radioAzul.isSelected() || radioRojo.isSelected())) {
                btnContinuar.setVisible(false);
                lblEsperando.setVisible(true);
                presentadorJugador.configurarJugador(txtNombre.getText(), radioAzul.isSelected() ? "Azul" : "Rojo");
                navegacion.mostrarPantallaColocarBarcos();
            }
        } catch (Exception ex) {
            Logger.getLogger(PantallaDatosJugador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnContinuarMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnContinuar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAzul;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblEsperando;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPergamino;
    private javax.swing.JLabel lblRojo;
    private javax.swing.JRadioButton radioAzul;
    private javax.swing.JRadioButton radioRojo;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables


    @Override
    public void mostrarError(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void mostrarConfiguracionJugador() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
