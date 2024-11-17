/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.battleshippresentacion.vista;

import com.mycompany.battleshippresentacion.presentador.ColocarBarcosPresentador;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author JoseH
 */
public class PantallaColocarBarcos extends javax.swing.JPanel {

    PantallaInicio pantallaInicio;
    
    private ColocarBarcosPresentador presentador;
    
    private String naveElegida;
    private JButton[][] casillas;
    private int orientacion = 0;
    
    /**
     * Creates new form ColocarBarcos
     */
    public PantallaColocarBarcos(PantallaInicio pantallaInicio) {
        presentador = new ColocarBarcosPresentador(this);
        casillas = new JButton[10][10];
        this.pantallaInicio = pantallaInicio;
        initComponents();
        cargarFuentes();
        crearTablero();
        presentador.inicializarJuego();
    }

    
    public void crearTablero() {
        try {
            // Cargar la fuente personalizada
            Font fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Micro5-Regular.ttf"));
            fuentePersonalizada = fuentePersonalizada.deriveFont(50f);

            // Crear el panel del tablero
            JPanel panelTablero = new JPanel();
            panelTablero.setLayout(new GridLayout(10, 10));
            panelTablero.setPreferredSize(new Dimension(600, 600)); // Tamaño del tablero 
            int margenDerecho = 40;
            int xPos = 1440 - 600 - margenDerecho; 
            int yPos = (800 - 600) / 2; 

            panelTablero.setBounds(xPos, yPos, 600, 600);

            // Crear los botones para el tablero
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    JButton casilla = new JButton();
                    casilla.setPreferredSize(new Dimension(60, 60));
                    casilla.setBackground(new Color(139, 69, 19));
                    panelTablero.add(casilla);
                    casillas[i][j] = casilla;
                    casillas[i][j].addMouseListener(new ButtonClickListener(i, j, casillas));
                }
            }
            
            panelTablero.setFocusable(true);


            // Se crea el panel para las etiquetas superiores (A-J) 
            JPanel panelEtiquetasSuperior = new JPanel();
            panelEtiquetasSuperior.setLayout(new GridLayout(1, 10));
            panelEtiquetasSuperior.setBounds(xPos, yPos - 30, 600, 30);
            panelEtiquetasSuperior.setBackground(Color.WHITE);

            // Se añaden etiquetas de A a J 
            for (char letra = 'A'; letra <= 'J'; letra++) {
                JLabel etiqueta = new JLabel(String.valueOf(letra), SwingConstants.CENTER);
                etiqueta.setFont(fuentePersonalizada);
                panelEtiquetasSuperior.add(etiqueta);
            }

            // Se crea el panel para las etiquetas laterales (1-10)
            JPanel panelEtiquetasLateral = new JPanel();
            panelEtiquetasLateral.setLayout(new GridLayout(10, 1));
            panelEtiquetasLateral.setBounds(xPos - 30, yPos, 30, 600);
            panelEtiquetasLateral.setBackground(Color.WHITE);

            // Se añaden etiquetas de 1 a 10
            for (int numero = 1; numero <= 10; numero++) {
                JLabel etiqueta = new JLabel(String.valueOf(numero), SwingConstants.CENTER);
                etiqueta.setFont(fuentePersonalizada);
                panelEtiquetasLateral.add(etiqueta);
            }

            // Se agregan los paneles al JFrame
            add(panelEtiquetasSuperior);
            add(panelEtiquetasLateral);
            add(panelTablero);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ButtonClickListener implements MouseListener {

        private int tamañoNave;
        private int fila;
        private int columna;
        private JButton[][] casillas;

        public ButtonClickListener(int fila, int columna, JButton[][] casillas) {
            this.fila = fila;
            this.columna = columna;
            this.casillas = casillas;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) { // Clic derecho para rotar
                if (hayNaveEnCasilla(fila, columna)) {
                    int[] coordenadasInicio = obtenerCoordenadasIniciales(fila, columna);
                    int filaInicio = coordenadasInicio[0];
                    int columnaInicio = coordenadasInicio[1];

                    if (fila == filaInicio && columna == columnaInicio) {
                    if(casillas[fila][columna].getIcon() == barco1.getIcon()){
                        naveElegida = "nave1";
                    } else if (casillas[fila][columna].getIcon() == barco2.getIcon()){
                        naveElegida = "nave2";
                    } else if (casillas[fila][columna].getIcon() == barco3.getIcon()){
                        naveElegida = "nave3";
                    } else if (casillas[fila][columna].getIcon() == barco4.getIcon()) {
                        naveElegida = "nave4";
                    }
                    
                    // Limpiar las casillas actuales de la nave
                    limpiarNavesEnCasillas(filaInicio, columnaInicio, tamañoNave);
                    
                    // Alternar orientación antes de limpiar las casillas
                    orientacion = (orientacion + 1) % 4;

                    if (puedeRotar(filaInicio, columnaInicio, tamañoNave)) {
                    // Coloca la nave rotada en la nueva orientación
                    presentador.rotarNave(fila, columna, tamañoNave);
                    } else {
                        orientacion = (orientacion + 3) % 4;
                    }

                    revalidate();
                    repaint(); // Actualiza la interfaz
                }
                }
            } else if (e.getButton() == MouseEvent.BUTTON1) { // Clic izquierdo para colocar la nave
                if(e.getClickCount() == 2){
                    if (hayNaveEnCasilla(fila, columna)) {
                        eliminarNaveSeleccionada(fila, columna);
                        presentador.eliminarNave(fila, columna);
                    }
                }
                if (naveElegida != null && !hayNaveEnCasilla(fila, columna)) {
                    switch (naveElegida) {
                        case "nave1":
                            tamañoNave = 1;
                            break;
                        case "nave2":
                            tamañoNave = 2;
                            break;
                        case "nave3":
                            tamañoNave = 3;
                            break;
                        case "nave4":
                            tamañoNave = 4;
                            break;
                    }
                    
                    presentador.colocarNave(fila, columna, tamañoNave, naveElegida);
                    naveElegida = null;
                } else {
                    System.out.println("No se ha seleccionado ninguna nave.");
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    
    public void naveSeleccionada(String tipoNave){
        presentador.seleccionarNave(tipoNave);
    }
    
    public void eliminarNaveSeleccionada(int columna, int fila) {
        if (naveElegida != null) {
            int tamañoNave = 0;

            switch (naveElegida) {
                case "nave1":
                    tamañoNave = 1;
                    break;
                case "nave2":
                    tamañoNave = 2;
                    break;
                case "nave3":
                    tamañoNave = 3;
                    break;
                case "nave4":
                    tamañoNave = 4;
                    break;
            }

            int[] coordenadasInicio = obtenerCoordenadasIniciales(columna, fila);
            int filaInicio = coordenadasInicio[0];
            int columnaInicio = coordenadasInicio[1];

            limpiarNavesEnCasillas(filaInicio, columnaInicio, tamañoNave);

            naveElegida = null; 
        
            revalidate();
            repaint(); 
        }
    }
    
    public boolean puedeRotar(int filaInicio, int columnaInicio, int tamañoNave) {
        for (int i = 0; i < tamañoNave; i++) {
            int filaActual = filaInicio;
            int columnaActual = columnaInicio;

            switch (orientacion) {
                case 0: // Derecha
                    columnaActual = columnaInicio + i;
                    break;
                case 1: // Abajo
                    filaActual = filaInicio + i;
                    break;
                case 2: // Izquierda
                    columnaActual = columnaInicio - i;
                    break;
                case 3: // Arriba
                    filaActual = filaInicio - i;
                    break;
            }

            // Verificar si la casilla está fuera de los límites
            if (filaActual < 0 || filaActual >= 10 || columnaActual < 0 || columnaActual >= 10) {
                return false; // Se sale de los límites
            }

            // Verificar si la casilla ya tiene una nave
            if (hayNaveEnCasilla(filaActual, columnaActual)) {
                return false; // Hay otra nave en la posición
            }
        }
        return true; // Puede rotar
    }

    public int[] obtenerCoordenadasIniciales(int fila, int columna) {
        int filaInicio = fila;
        int columnaInicio = columna;

        switch (orientacion) {
            case 0: // Derecha
                // Mientras esté en los límites y haya una nave a la izquierda, retrocede la columna
                while (columnaInicio > 0 && hayNaveEnCasilla(fila, columnaInicio - 1)) {
                    columnaInicio--;
                }
                break;
            case 1: // Abajo
                // Mientras esté en los límites y haya una nave arriba, retrocede la fila
                while (filaInicio > 0 && hayNaveEnCasilla(filaInicio - 1, columna)) {
                    filaInicio--;
                }
                break;
            case 2: // Izquierda
                // Mientras esté en los límites y haya una nave a la derecha, avanza la columna
                while (columnaInicio < 9 && hayNaveEnCasilla(fila, columnaInicio + 1)) {
                    columnaInicio++;
                }
                break;
            case 3: // Arriba
                // Mientras esté en los límites y haya una nave abajo, avanza la fila
                while (filaInicio < 9 && hayNaveEnCasilla(filaInicio + 1, columna)) {
                    filaInicio++;
                }
                break;
        }

        return new int[]{filaInicio, columnaInicio};
    }

    public void colocarNaveEnCasillas(int filaInicio, int columnaInicio, int tamañoNave) {
        for (int i = 0; i < tamañoNave; i++) {
            int filaActual = filaInicio;
            int columnaActual = columnaInicio;

            switch (orientacion) {
                case 0: // Derecha
                    columnaActual = columnaInicio + i;
                    break;
                case 1: // Abajo
                    filaActual = filaInicio + i;
                    break;
                case 2: // Izquierda
                    columnaActual = columnaInicio - i;
                    break;
                case 3: // Arriba
                    filaActual = filaInicio - i;
                    break;
            }

            
            if (filaActual < 10 && filaActual >= 0 && columnaActual < 10 && columnaActual >= 0) {    
                switch (naveElegida) {
                    case "nave1":
                        casillas[filaActual][columnaActual].setIcon(barco1.getIcon());
                        break;
                    case "nave2":
                        casillas[filaActual][columnaActual].setIcon(barco2.getIcon());
                        break;
                    case "nave3":
                        casillas[filaActual][columnaActual].setIcon(barco3.getIcon());
                        break;
                    case "nave4":
                        casillas[filaActual][columnaActual].setIcon(barco4.getIcon());
                        break;
                }
            }
        }
    }
    
    public void limpiarNavesEnCasillas(int filaInicio, int columnaInicio, int tamañoNave) {
        for (int i = 0; i < tamañoNave; i++) {
            int filaActual = filaInicio;
            int columnaActual = columnaInicio;

            switch (orientacion) {
                case 0: // Derecha
                    columnaActual = columnaInicio + i;
                    break;
                case 1: // Abajo
                    filaActual = filaInicio + i;
                    break;
                case 2: // Izquierda
                    columnaActual = columnaInicio - i;
                    break;
                case 3: // Arriba
                    filaActual = filaInicio - i;
                    break;
            }

            // Verificar si la posición está dentro de los límites del tablero antes de limpiar
            if (filaActual < 10 && filaActual >= 0 && columnaActual < 10 && columnaActual >= 0) {
                casillas[filaActual][columnaActual].setIcon(null); // Limpiar la casilla
            }
        }
    }

    public void crearNaves() {
        ImageIcon icon1 = new ImageIcon("src/main/resources/img/navesAzul/azul1.png");
        ImageIcon icon2 = new ImageIcon("src/main/resources/img/navesAzul/azul2.png");
        ImageIcon icon3 = new ImageIcon("src/main/resources/img/navesAzul/azul3.png");
        ImageIcon icon4 = new ImageIcon("src/main/resources/img/navesAzul/azul4.png");
        barco1.setText("");
        barco1.setIcon(rotarImagen(icon1, 90));

        barco2.setText("");
        barco2.setIcon(rotarImagen(icon2, 90));

        barco3.setText("");
        barco3.setIcon(rotarImagen(icon3, 90));

        barco4.setText("");
        barco4.setIcon(rotarImagen(icon4, 90));

        barco4.setIcon(rotarImagen(icon4, 90));

        this.repaint();

    }
    
    public boolean hayNaveEnCasilla(int fila, int columna){
        if (casillas[fila][columna].getIcon() != null){
            return true;
        } else {
            return false;
        }
    }

    public void cargarFuentes() {
        try {
            Font fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Micro5-Regular.ttf"));
            fuentePersonalizada = fuentePersonalizada.deriveFont(45f);
            lblInstruccion2.setFont(fuentePersonalizada);
            lblInstruccion3.setFont(fuentePersonalizada);
            lblInstruccion1.setFont(fuentePersonalizada);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param imagenOriginal Imagen con rotación original
     * @param angulo Ángulo que determinará si el barco girará horizontalmente o
     * verticalmente (90 0 180 grados)
     * @return Imagen rotada
     */
    public ImageIcon rotarImagen(ImageIcon imagenOriginal, int angulo) {
        Image imagen = imagenOriginal.getImage();
        int anchoOriginal = imagen.getWidth(null);
        int altoOriginal = imagen.getHeight(null);

        // Se calcula la longitud de la diagonal para el nuevo tamaño de imagen
        int diagonal = (int) Math.ceil(Math.sqrt(anchoOriginal * anchoOriginal + altoOriginal * altoOriginal));

        // Se crea una nueva imagen con dimensiones cuadradas basadas en la diagonal
        BufferedImage bufferedImage = new BufferedImage(diagonal, diagonal, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Se mueve la imagen al centro para rotarla correctamente
        AffineTransform transform = new AffineTransform();
        transform.translate((diagonal - anchoOriginal) / 2.0, (diagonal - altoOriginal) / 2.0);

        //Rotación de la imagen
        transform.rotate(Math.toRadians(angulo), anchoOriginal / 2.0, altoOriginal / 2.0);

        // Se dibuja la imagen rotada
        g2d.drawImage(imagen, transform, null);
        g2d.dispose();

        return new ImageIcon(bufferedImage);
    }
    
    public void naveMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getSource() == barco1) {
            // Acción para barco1
            naveElegida = "nave1";
        } else if (evt.getSource() == barco2) {
            // Acción para barco2
            naveElegida = "nave2";
        } else if (evt.getSource() == barco3) {
            // Acción para barco3
            naveElegida = "nave3";
        } else if (evt.getSource() == barco4) {
            // Acción para barco4
            naveElegida = "nave4";
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

        barco4 = new javax.swing.JLabel();
        barco1 = new javax.swing.JLabel();
        barco2 = new javax.swing.JLabel();
        barco3 = new javax.swing.JLabel();
        lblInstruccion1 = new javax.swing.JLabel();
        lblInstruccion2 = new javax.swing.JLabel();
        lblInstruccion3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1440, 800));
        setMinimumSize(new java.awt.Dimension(1440, 800));
        setPreferredSize(new java.awt.Dimension(1440, 800));
        setLayout(null);

        barco4.setText("barco4");
        barco4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barco4MouseClicked(evt);
            }
        });
        add(barco4);
        barco4.setBounds(90, 320, 390, 100);

        barco1.setText("barco1");
        barco1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barco1MouseClicked(evt);
            }
        });
        add(barco1);
        barco1.setBounds(90, 630, 330, 80);

        barco2.setText("barco2");
        barco2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barco2MouseClicked(evt);
            }
        });
        add(barco2);
        barco2.setBounds(90, 530, 330, 80);

        barco3.setText("barco3");
        barco3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barco3MouseClicked(evt);
            }
        });
        add(barco3);
        barco3.setBounds(90, 430, 330, 80);

        lblInstruccion1.setText("Haga click a la nave y luego en la casilla");
        add(lblInstruccion1);
        lblInstruccion1.setBounds(90, 190, 600, 30);

        lblInstruccion2.setText("para colocarla.");
        add(lblInstruccion2);
        lblInstruccion2.setBounds(90, 220, 600, 30);

        lblInstruccion3.setText("Haga click derecho para rotar la nave.");
        add(lblInstruccion3);
        lblInstruccion3.setBounds(90, 250, 600, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/palmaFondo.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 120, 630, 680);
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void barco1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barco1MouseClicked
         naveMouseClicked(evt);
    }//GEN-LAST:event_barco1MouseClicked

    private void barco2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barco2MouseClicked
        naveMouseClicked(evt);
    }//GEN-LAST:event_barco2MouseClicked

    private void barco3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barco3MouseClicked
         naveMouseClicked(evt);
    }//GEN-LAST:event_barco3MouseClicked

    private void barco4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barco4MouseClicked
         naveMouseClicked(evt);
    }//GEN-LAST:event_barco4MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel barco1;
    private javax.swing.JLabel barco2;
    private javax.swing.JLabel barco3;
    private javax.swing.JLabel barco4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblInstruccion1;
    private javax.swing.JLabel lblInstruccion2;
    private javax.swing.JLabel lblInstruccion3;
    // End of variables declaration//GEN-END:variables
}
