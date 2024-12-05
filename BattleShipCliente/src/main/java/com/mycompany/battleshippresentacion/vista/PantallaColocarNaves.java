/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.battleshippresentacion.vista;

import com.mycompany.battleshippresentacion.ivista.IVistaColocarNaves;
import com.mycompany.battleshippresentacion.presentador.PresentadorColocarNaves;
import com.mycompany.battleshippresentacion.presentador.PresentadorJugador;
import com.mycompany.battleshippresentacion.presentador.PresentadorPrincipal;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author JoseH
 */
public class PantallaColocarNaves extends javax.swing.JPanel implements IVistaColocarNaves{

    private PresentadorPrincipal navegacion;
    private JFrame framePrincipal;
    private PresentadorColocarNaves presentador;
    private PresentadorJugador presentadorJugador;

    private String naveElegida;
    private JButton[][] casillas;
    private int orientacion = 0;

    /**
     * Creates new form ColocarBarcos
     */
    public PantallaColocarNaves (JFrame framePrincipal, PresentadorJugador presentadorJugador, PresentadorPrincipal navegacion) throws Exception {
        this.framePrincipal = framePrincipal;
        this.presentadorJugador = presentadorJugador;
        this.navegacion = navegacion;
        presentador = new PresentadorColocarNaves(this, navegacion, presentadorJugador);
        casillas = new JButton[10][10];
        initComponents();
        btnConfirmar.setVisible(false);
        cargarFuentes();
        presentador.inicializarJuego();
        presentador.crearNaves();
        btnConfirmar.setVisible(false);
        lblAlerta.setVisible(false);
    }

    public JButton[][] getCasillas() {
        return casillas;
    }

    @Override
    public void crearTablero() {
        try {
            Font fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Micro5-Regular.ttf"));
            fuentePersonalizada = fuentePersonalizada.deriveFont(40f);

            JPanel panelTablero = new JPanel();
            panelTablero.setLayout(new GridLayout(10, 10));
            panelTablero.setPreferredSize(new Dimension(600, 600));
            int margenDerecho = 40;
            int xPos = 1440 - 600 - margenDerecho;
            int yPos = (800 - 600) / 2;

            panelTablero.setBounds(xPos, yPos, 600, 600);

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

            JPanel panelEtiquetasSuperior = new JPanel();
            panelEtiquetasSuperior.setLayout(new GridLayout(1, 10));
            panelEtiquetasSuperior.setBounds(xPos, yPos - 30, 600, 30);
            panelEtiquetasSuperior.setBackground(Color.WHITE);

            for (char letra = 'A'; letra <= 'J'; letra++) {
                JLabel etiqueta = new JLabel(String.valueOf(letra), SwingConstants.CENTER);
                etiqueta.setFont(fuentePersonalizada);
                panelEtiquetasSuperior.add(etiqueta);
            }

            JPanel panelEtiquetasLateral = new JPanel();
            panelEtiquetasLateral.setLayout(new GridLayout(10, 1));
            panelEtiquetasLateral.setBounds(xPos - 30, yPos, 30, 600);
            panelEtiquetasLateral.setBackground(Color.WHITE);

            for (int numero = 1; numero <= 10; numero++) {
                JLabel etiqueta = new JLabel(String.valueOf(numero), SwingConstants.CENTER);
                etiqueta.setFont(fuentePersonalizada);
                panelEtiquetasLateral.add(etiqueta);
            }

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
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (hayNaveEnCasilla(fila, columna)) {
                    int[] coordenadasInicio = obtenerCoordenadasIniciales(fila, columna);
                    int filaInicio = coordenadasInicio[0];
                    int columnaInicio = coordenadasInicio[1];

                    if (fila == filaInicio && columna == columnaInicio) {
                        if (casillas[fila][columna].getIcon() == barco1.getIcon()) {
                            naveElegida = "nave1";
                        } else if (casillas[fila][columna].getIcon() == barco2.getIcon()) {
                            naveElegida = "nave2";
                        } else if (casillas[fila][columna].getIcon() == barco3.getIcon()) {
                            naveElegida = "nave3";
                        } else if (casillas[fila][columna].getIcon() == barco4.getIcon()) {
                            naveElegida = "nave4";
                        }

                        limpiarNavesEnCasillas(filaInicio, columnaInicio, tamañoNave);

                        if (puedeRotar(filaInicio, columnaInicio, tamañoNave)) {

                            colocarNaveEnCasillas(fila, columna, tamañoNave);

                        } else {
                            lblAlerta.setVisible(true);
                            colocarNaveEnCasillas(filaInicio, columnaInicio, tamañoNave);
                        }

                        naveElegida = null;
                        revalidate();
                        repaint();
                    }
                }
            } else if (e.getButton() == MouseEvent.BUTTON1) {
                if (e.getClickCount() == 2) {
                    if (hayNaveEnCasilla(fila, columna)) {
                        if (casillas[fila][columna].getIcon() == barco1.getIcon()) {
                            naveElegida = "nave1";
                        } else if (casillas[fila][columna].getIcon() == barco2.getIcon()) {
                            naveElegida = "nave2";
                        } else if (casillas[fila][columna].getIcon() == barco3.getIcon()) {
                            naveElegida = "nave3";
                        } else if (casillas[fila][columna].getIcon() == barco4.getIcon()) {
                            naveElegida = "nave4";
                        }
                        modificarContadorNave(0);

                        eliminarNaveSeleccionada(fila, columna);
                        lblAlerta.setVisible(false);
                        todasLasNavesColocadas();
                    }
                } else if (naveElegida != null && !hayNaveEnCasilla(fila, columna)) {
                    orientacion = 0;
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

                    if (puedeColocarNave(fila, columna, tamañoNave)) {
                        if (modificarContadorNave(1)) {

                            colocarNaveEnCasillas(fila, columna, tamañoNave);
                            todasLasNavesColocadas();
                            lblAlerta.setVisible(false);
                        }
                    } else {
                        lblAlerta.setText("Colocación de nave inválida");
                        lblAlerta.setVisible(true);
                    }
                    naveElegida = null;
                } else if (naveElegida == null){
                    lblAlerta.setText("Ninguna nave seleccionada");
                    lblAlerta.setVisible(true);
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

    public void naveSeleccionada(String tipoNave) {
        presentador.seleccionarNave(tipoNave);
    }
    
    private void todasLasNavesColocadas() {
        if (lblNumBarcos.getText().equals("0") && lblNumCruceros.getText().equals("0") && lblNumPortaAviones.getText().equals("0") && lblNumSubmarinos.getText().equals("0")) {
            lblNumBarcos.setVisible(false);
            lblNumCruceros.setVisible(false);
            lblNumPortaAviones.setVisible(false);
            lblNumSubmarinos.setVisible(false);
            barco1.setVisible(false);
            barco2.setVisible(false);
            barco3.setVisible(false);
            barco4.setVisible(false);
            btnConfirmar.setVisible(true);
        } else {
            lblNumBarcos.setVisible(true);
            lblNumCruceros.setVisible(true);
            lblNumPortaAviones.setVisible(true);
            lblNumSubmarinos.setVisible(true);
            barco1.setVisible(true);
            barco2.setVisible(true);
            barco3.setVisible(true);
            barco4.setVisible(true);
            btnConfirmar.setVisible(false);
            lblAlerta.setVisible(false);
        }
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

    public boolean tieneBarcosAdyacentes(int fila, int columna, int tamañoNave, int filaInicio, int columnaInicio) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Saltar la casilla central
                if (i == 0 && j == 0) {
                    continue;
                }

                int nuevaFila = fila + i;
                int nuevaColumna = columna + j;

                if (nuevaFila >= 0 && nuevaFila < 10
                        && nuevaColumna >= 0 && nuevaColumna < 10) {
                    if (hayNaveEnCasilla(nuevaFila, nuevaColumna)) {
                        boolean esParteDeLaNaveActual = false;
                        if (naveElegida != null) {
                            for (int k = 0; k < tamañoNave; k++) {
                                int filaNav = filaInicio;
                                int columnaNav = columnaInicio;
                                switch (orientacion) {
                                    case 0:
                                        columnaNav = columnaInicio + k;
                                        break;
                                    case 1:
                                        filaNav = filaInicio + k;
                                        break;
                                    case 2:
                                        columnaNav = columnaInicio - k;
                                        break;
                                    case 3:
                                        filaNav = filaInicio - k;
                                        break;
                                }
                                if (filaNav == nuevaFila && columnaNav == nuevaColumna) {
                                    esParteDeLaNaveActual = true;
                                    break;
                                }
                            }
                        }
                        if (!esParteDeLaNaveActual) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean puedeColocarNave(int filaInicio, int columnaInicio, int tamañoNave) {
        Icon[] iconosTemporales = new Icon[tamañoNave];
        int[] filasTemp = new int[tamañoNave];
        int[] columnasTemp = new int[tamañoNave];

        // Verificar si toda la nave cabe en el tablero y guardar estado
        for (int i = 0; i < tamañoNave; i++) {
            int filaActual = filaInicio;
            int columnaActual = columnaInicio;

            switch (orientacion) {
                case 0:
                    columnaActual = columnaInicio + i;
                    break;
                case 1:
                    filaActual = filaInicio + i;
                    break;
                case 2:
                    columnaActual = columnaInicio - i;
                    break;
                case 3:
                    filaActual = filaInicio - i;
                    break;
            }

            // Verificar límites
            if (filaActual < 0 || filaActual >= 10
                    || columnaActual < 0 || columnaActual >= 10) {
                return false;
            }

            // Guardar estado actual
            filasTemp[i] = filaActual;
            columnasTemp[i] = columnaActual;
            iconosTemporales[i] = casillas[filaActual][columnaActual].getIcon();

            // Limpiar casilla temporalmente
            casillas[filaActual][columnaActual].setIcon(null);
        }

        boolean puedeColocar = true;
        for (int i = 0; i < tamañoNave; i++) {
            if (tieneBarcosAdyacentes(filasTemp[i], columnasTemp[i], tamañoNave, filaInicio, columnaInicio)) {
                puedeColocar = false;
                break;
            }
        }

        // Restaurar estado original
        for (int i = 0; i < tamañoNave; i++) {
            casillas[filasTemp[i]][columnasTemp[i]].setIcon(iconosTemporales[i]);
        }

        return puedeColocar;
    }

    public boolean puedeRotar(int filaInicio, int columnaInicio, int tamañoNave) {
        if (tamañoNave == 1) {
            return true; // Las naves de tamaño 1 siempre pueden rotar
        }
        // Guardar orientación actual y estado
        int orientacionOriginal = orientacion;
        Icon[] iconosTemporales = new Icon[tamañoNave];
        int[] filasTemp = new int[tamañoNave];
        int[] columnasTemp = new int[tamañoNave];

        for (int i = 0; i < tamañoNave; i++) {
            int filaActual = filaInicio;
            int columnaActual = columnaInicio;

            switch (orientacionOriginal) {
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

            if (filaActual >= 0 && filaActual < 10
                    && columnaActual >= 0 && columnaActual < 10) {
                iconosTemporales[i] = casillas[filaActual][columnaActual].getIcon();
                filasTemp[i] = filaActual;
                columnasTemp[i] = columnaActual;
                casillas[filaActual][columnaActual].setIcon(null);
            }
        }

        // Calcular nueva orientación (mantener las 4 direcciones)
        int nuevaOrientacion = (orientacionOriginal + 1) % 4;
        orientacion = nuevaOrientacion;

        // Verificar si la nueva posición es válida
        boolean puedeRotar = true;
        for (int i = 0; i < tamañoNave && puedeRotar; i++) {
            int filaActual = filaInicio;
            int columnaActual = columnaInicio;

            switch (nuevaOrientacion) {
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

            // Verificar límites
            if (filaActual < 0 || filaActual >= 10
                    || columnaActual < 0 || columnaActual >= 10) {
                puedeRotar = false;
                break;
            }

            if (tieneBarcosAdyacentes(filaActual, columnaActual, tamañoNave, filaInicio, columnaInicio)) {
                puedeRotar = false;
                break;
            }
        }

        // Si no puede rotar, restaurar la orientación original
        if (!puedeRotar) {
            orientacion = orientacionOriginal;
        }

        // Restaurar íconos originales
        for (int i = 0; i < tamañoNave; i++) {
            if (filasTemp[i] >= 0 && filasTemp[i] < 10
                    && columnasTemp[i] >= 0 && columnasTemp[i] < 10) {
                casillas[filasTemp[i]][columnasTemp[i]].setIcon(iconosTemporales[i]);
            }
        }

        return puedeRotar;
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

            if (filaActual < 10 && filaActual >= 0 && columnaActual < 10 && columnaActual >= 0) {
                casillas[filaActual][columnaActual].setIcon(null);
            }
        }
    }

    /**
     * Método para actualizar el contador de naves cada vez que el usuario
     * coloca una.
     *
     * @param eliminar Si es 0, el contador sube, y si es 1, el contador baja
     * @return Se devuelve true si se modificó el contador.
     */
    public boolean modificarContadorNave(int eliminar) {
        int contadorActual;

        switch (naveElegida) {
            case "nave1":
                contadorActual = Integer.parseInt(lblNumBarcos.getText());
                if (eliminar == 1 && contadorActual > 0) {
                    contadorActual--;
                } else if (eliminar == 0) {
                    contadorActual++;
                } else {
                    return false;
                }
                lblNumBarcos.setText(String.valueOf(contadorActual));
                return true;

            case "nave2":
                contadorActual = Integer.parseInt(lblNumSubmarinos.getText());
                if (eliminar == 1 && contadorActual > 0) {
                    contadorActual--;
                } else if (eliminar == 0) {
                    contadorActual++;
                } else {
                    return false;
                }
                lblNumSubmarinos.setText(String.valueOf(contadorActual));
                return true;

            case "nave3":
                contadorActual = Integer.parseInt(lblNumCruceros.getText());
                if (eliminar == 1 && contadorActual > 0) {
                    contadorActual--;
                } else if (eliminar == 0) {
                    contadorActual++;
                } else {
                    return false;
                }
                lblNumCruceros.setText(String.valueOf(contadorActual));
                return true;

            case "nave4":
                contadorActual = Integer.parseInt(lblNumPortaAviones.getText());
                if (eliminar == 1 && contadorActual > 0) {
                    contadorActual--;
                } else if (eliminar == 0) {
                    contadorActual++;
                } else {
                    return false;
                }
                lblNumPortaAviones.setText(String.valueOf(contadorActual));
                return true;

            default:
                // Si la nave no es válida
                return false;
        }
    }

    @Override
    public void crearNaves(String color) {
        ImageIcon icon1;
        ImageIcon icon2;
        ImageIcon icon3;
        ImageIcon icon4;
        if (color.equals("AZUL")) {
            icon1 = new ImageIcon("src/main/resources/img/navesAzul/azul1.png");
            icon2 = new ImageIcon("src/main/resources/img/navesAzul/azul2.png");
            icon3 = new ImageIcon("src/main/resources/img/navesAzul/azul3.png");
            icon4 = new ImageIcon("src/main/resources/img/navesAzul/azul4.png");
        } else {
            icon1 = new ImageIcon("src/main/resources/img/navesRojo/rojo1.png");
            icon2 = new ImageIcon("src/main/resources/img/navesRojo/rojo2.png");
            icon3 = new ImageIcon("src/main/resources/img/navesRojo/rojo3.png");
            icon4 = new ImageIcon("src/main/resources/img/navesRojo/rojo4.png");
        }
        
        barco1.setText("");
        barco1.setIcon(rotarImagen(icon1, 90));

        barco2.setText("");
        barco2.setIcon(rotarImagen(icon2, 90));

        barco3.setText("");
        barco3.setIcon(rotarImagen(icon3, 90));

        barco4.setText("");
        barco4.setIcon(rotarImagen(icon4, 90));

        this.repaint();

    }

    public boolean hayNaveEnCasilla(int fila, int columna) {
        if (casillas[fila][columna].getIcon() != null) {
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
            lblAlerta.setFont(fuentePersonalizada);
            lblAlerta.setForeground(Color.red);
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

        int diagonal = (int) Math.ceil(Math.sqrt(anchoOriginal * anchoOriginal + altoOriginal * altoOriginal));

        BufferedImage bufferedImage = new BufferedImage(diagonal, diagonal, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        AffineTransform transform = new AffineTransform();
        transform.translate((diagonal - anchoOriginal) / 2.0, (diagonal - altoOriginal) / 2.0);

        transform.rotate(Math.toRadians(angulo), anchoOriginal / 2.0, altoOriginal / 2.0);

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

        lblAlerta = new javax.swing.JLabel();
        lblNumPortaAviones = new javax.swing.JLabel();
        lblNumCruceros = new javax.swing.JLabel();
        lblNumSubmarinos = new javax.swing.JLabel();
        lblNumBarcos = new javax.swing.JLabel();
        btnConfirmar = new javax.swing.JButton();
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

        lblAlerta.setText("¡Rotación inválida!");
        add(lblAlerta);
        lblAlerta.setBounds(70, 70, 590, 50);

        lblNumPortaAviones.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblNumPortaAviones.setText("2");
        add(lblNumPortaAviones);
        lblNumPortaAviones.setBounds(360, 340, 50, 50);

        lblNumCruceros.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblNumCruceros.setText("2");
        add(lblNumCruceros);
        lblNumCruceros.setBounds(360, 440, 50, 50);

        lblNumSubmarinos.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblNumSubmarinos.setText("4");
        add(lblNumSubmarinos);
        lblNumSubmarinos.setBounds(360, 540, 50, 50);

        lblNumBarcos.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblNumBarcos.setText("3");
        add(lblNumBarcos);
        lblNumBarcos.setBounds(360, 640, 50, 50);

        btnConfirmar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnConfirmar.setText("Confirmar");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });
        add(btnConfirmar);
        btnConfirmar.setBounds(140, 490, 190, 50);

        barco4.setText("barco4");
        barco4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barco4MouseClicked(evt);
            }
        });
        add(barco4);
        barco4.setBounds(90, 320, 260, 100);

        barco1.setText("barco1");
        barco1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barco1MouseClicked(evt);
            }
        });
        add(barco1);
        barco1.setBounds(90, 630, 260, 80);

        barco2.setText("barco2");
        barco2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barco2MouseClicked(evt);
            }
        });
        add(barco2);
        barco2.setBounds(90, 530, 260, 80);

        barco3.setText("barco3");
        barco3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barco3MouseClicked(evt);
            }
        });
        add(barco3);
        barco3.setBounds(90, 430, 260, 80);

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

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        lblAlerta.setText("Esperando al otro jugador...");
        lblAlerta.setForeground(Color.BLACK);
        lblAlerta.setVisible(true);
        
        new Thread(() -> {
            try {
                presentador.enviarTableroCompleto(casillas);
                presentador.getClienteTablero().imprimirTablero();
                presentador.confirmarColocacion();
            } catch (Exception ex) {
                Logger.getLogger(PantallaColocarNaves.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }//GEN-LAST:event_btnConfirmarActionPerformed

    @Override
    public Icon getBarco1Icon() {
        return barco1.getIcon();
    }

    @Override
    public Icon getBarco2Icon() {
        return barco2.getIcon();
    }

    @Override
    public Icon getBarco3Icon() {
        return barco3.getIcon();
    }

    @Override
    public Icon getBarco4Icon() {
        return barco4.getIcon();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel barco1;
    private javax.swing.JLabel barco2;
    private javax.swing.JLabel barco3;
    private javax.swing.JLabel barco4;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAlerta;
    private javax.swing.JLabel lblInstruccion1;
    private javax.swing.JLabel lblInstruccion2;
    private javax.swing.JLabel lblInstruccion3;
    private javax.swing.JLabel lblNumBarcos;
    private javax.swing.JLabel lblNumCruceros;
    private javax.swing.JLabel lblNumPortaAviones;
    private javax.swing.JLabel lblNumSubmarinos;
    // End of variables declaration//GEN-END:variables
}
