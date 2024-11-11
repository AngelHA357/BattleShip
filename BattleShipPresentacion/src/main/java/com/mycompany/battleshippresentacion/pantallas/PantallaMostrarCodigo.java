/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.battleshippresentacion.pantallas;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author JoseH
 */
public class PantallaMostrarCodigo extends javax.swing.JPanel {
    PantallaInicio pantallaInicio;
    /**
     * Creates new form PantallaIngresarCódigo
     */
    public PantallaMostrarCodigo(PantallaInicio pantallaInicio) {
        this.pantallaInicio = pantallaInicio;
        initComponents();
        cargarFuentes();
    }
    
    private void cargarFuentes(){
        try {
            Font fuentePersonalizada1 = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Jacquard12-Regular.ttf"));
            Font fuentePersonalizada2 = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Micro5-Regular.ttf"));
            fuentePersonalizada1 = fuentePersonalizada1.deriveFont(70f);
            lblTitulo1.setFont(fuentePersonalizada1);
            lblTitulo2.setFont(fuentePersonalizada1);
            
            fuentePersonalizada2 = fuentePersonalizada2.deriveFont(45f);
            lblEsperandoJugador.setFont(fuentePersonalizada2);
            fuentePersonalizada2 = fuentePersonalizada2.deriveFont(30f);
            btnCancelar.setFont(fuentePersonalizada2);
            
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

        lblTitulo2 = new javax.swing.JLabel();
        lblTitulo1 = new javax.swing.JLabel();
        lblEsperandoJugador = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        txtCodigoSala = new javax.swing.JTextField();
        lblPergamino = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1440, 800));
        setMinimumSize(new java.awt.Dimension(1440, 800));
        setLayout(null);

        lblTitulo2.setFont(new java.awt.Font("Segoe UI", 1, 65)); // NOI18N
        lblTitulo2.setText("sala");
        add(lblTitulo2);
        lblTitulo2.setBounds(640, 230, 130, 120);

        lblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 65)); // NOI18N
        lblTitulo1.setText("Código de la ");
        add(lblTitulo1);
        lblTitulo1.setBounds(530, 150, 480, 120);

        lblEsperandoJugador.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        lblEsperandoJugador.setText("Esperando jugador...");
        add(lblEsperandoJugador);
        lblEsperandoJugador.setBounds(570, 520, 290, 40);

        btnCancelar.setBackground(new java.awt.Color(255, 137, 29));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 113, 29), 7, true));
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.setMaximumSize(new java.awt.Dimension(270, 70));
        btnCancelar.setMinimumSize(new java.awt.Dimension(270, 70));
        btnCancelar.setPreferredSize(new java.awt.Dimension(270, 70));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        add(btnCancelar);
        btnCancelar.setBounds(630, 600, 140, 50);

        txtCodigoSala.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));
        txtCodigoSala.setEnabled(false);
        txtCodigoSala.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCodigoSalaMouseClicked(evt);
            }
        });
        add(txtCodigoSala);
        txtCodigoSala.setBounds(510, 390, 380, 60);

        lblPergamino.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pergamino.png"))); // NOI18N
        add(lblPergamino);
        lblPergamino.setBounds(370, 10, 668, 780);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondoInicio.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1440, 1030);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCodigoSalaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodigoSalaMouseClicked
        PantallaDatosJugador pantallaSiguiente = new PantallaDatosJugador(pantallaInicio);
        pantallaInicio.getContentPane().removeAll();
        pantallaInicio.setLayout(new BorderLayout());

        // Agregar el nuevo JPanel al JFrame
        pantallaInicio.add(pantallaSiguiente, BorderLayout.CENTER);
        pantallaInicio.revalidate();                   
        pantallaInicio.repaint();
        pantallaSiguiente.setVisible(true);
    }//GEN-LAST:event_txtCodigoSalaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblEsperandoJugador;
    private javax.swing.JLabel lblPergamino;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTitulo2;
    private javax.swing.JTextField txtCodigoSala;
    // End of variables declaration//GEN-END:variables
}
