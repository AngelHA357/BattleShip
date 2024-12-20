package com.mycompany.battleshippresentacion.vista;

import com.mycompany.battleshippresentacion.ivista.IVistaPartida;
import com.mycompany.battleshippresentacion.presentador.PresentadorPartida;
import com.mycompany.battleshippresentacion.presentador.PresentadorPrincipal;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author JoseH
 */
public class PantallaOpcionPartida extends javax.swing.JPanel implements IVistaPartida{

    private PresentadorPrincipal navegacion;
    private PresentadorPartida partida;
    private String codigo;
    
    private JFrame framePrincipal;

    /**
     * Creates new form PantallaOpcionPartida
     */
    public PantallaOpcionPartida(JFrame framePrincipal) {
        this.framePrincipal = framePrincipal;
        this.navegacion = new PresentadorPrincipal(framePrincipal);
        this.partida = new PresentadorPartida(this, navegacion);
        initComponents();
        cargarFuentes();
    }

    private void cargarFuentes() {
        try {
            Font fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Micro5-Regular.ttf"));
            fuentePersonalizada = fuentePersonalizada.deriveFont(40f);
            btnUnirsePartida.setFont(fuentePersonalizada);
            btnCrearPartida.setFont(fuentePersonalizada);
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

        btnUnirsePartida = new javax.swing.JButton();
        btnCrearPartida = new javax.swing.JButton();
        lblPergamino = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1440, 800));
        setMinimumSize(new java.awt.Dimension(1440, 800));
        setLayout(null);

        btnUnirsePartida.setBackground(new java.awt.Color(255, 137, 29));
        btnUnirsePartida.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        btnUnirsePartida.setForeground(new java.awt.Color(255, 255, 255));
        btnUnirsePartida.setText("Unirse a una partida");
        btnUnirsePartida.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 113, 29), 7, true));
        btnUnirsePartida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUnirsePartida.setMaximumSize(new java.awt.Dimension(270, 70));
        btnUnirsePartida.setMinimumSize(new java.awt.Dimension(270, 70));
        btnUnirsePartida.setPreferredSize(new java.awt.Dimension(270, 70));
        btnUnirsePartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnirsePartidaActionPerformed(evt);
            }
        });
        add(btnUnirsePartida);
        btnUnirsePartida.setBounds(550, 290, 310, 70);

        btnCrearPartida.setBackground(new java.awt.Color(255, 137, 29));
        btnCrearPartida.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        btnCrearPartida.setForeground(new java.awt.Color(255, 255, 255));
        btnCrearPartida.setText("Crear una partida");
        btnCrearPartida.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 113, 29), 7, true));
        btnCrearPartida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCrearPartida.setMaximumSize(new java.awt.Dimension(270, 70));
        btnCrearPartida.setMinimumSize(new java.awt.Dimension(270, 70));
        btnCrearPartida.setPreferredSize(new java.awt.Dimension(270, 70));
        btnCrearPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearPartidaActionPerformed(evt);
            }
        });
        add(btnCrearPartida);
        btnCrearPartida.setBounds(550, 420, 310, 70);

        lblPergamino.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pergamino.png"))); // NOI18N
        add(lblPergamino);
        lblPergamino.setBounds(370, 10, 668, 780);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondoInicio.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1440, 1030);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUnirsePartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnirsePartidaActionPerformed
        navegacion.mostrarPantallaIngresarCodigo();
    }//GEN-LAST:event_btnUnirsePartidaActionPerformed

    private void btnCrearPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearPartidaActionPerformed
        partida.crearPartida();
    }//GEN-LAST:event_btnCrearPartidaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCrearPartida;
    private javax.swing.JButton btnUnirsePartida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblPergamino;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mostrarCodigo(String codigo) {
        navegacion.mostrarPantallaMostrarCodigo(codigo);
    }

    @Override
    public void mostrarError(String mensaje) {

    }

}
