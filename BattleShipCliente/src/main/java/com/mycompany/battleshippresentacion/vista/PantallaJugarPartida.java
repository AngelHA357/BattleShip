/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.battleshippresentacion.vista;

import com.mycompany.battleshippresentacion.ivista.IVistaJugarPartida;
import com.mycompany.battleshippresentacion.modelo.ClienteTablero;
import com.mycompany.battleshippresentacion.presentador.PresentadorAbandonar;
import com.mycompany.battleshippresentacion.presentador.PresentadorDisparo;
import com.mycompany.battleshippresentacion.presentador.PresentadorJugador;
import com.mycompany.battleshippresentacion.presentador.PresentadorPrincipal;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author PC
 */
public class PantallaJugarPartida extends javax.swing.JPanel implements IVistaJugarPartida {

    private JButton[][] casillasPropio = new JButton[10][10];
    private JButton[][] casillasDisparos = new JButton[10][10];
    private PresentadorDisparo presentador;
    private JFrame framePrincipal;
    private final PresentadorPrincipal navegacion;
    private PresentadorAbandonar presentadorAbandonar;
    private PresentadorJugador presentadorJugador;

    /**
     * Creates new form PantallaJugarPartida
     */
    public PantallaJugarPartida(JFrame framePrincipal, PresentadorJugador presentadorJugador, PresentadorPrincipal navegacion) {
        initComponents();
        cargarFuentes();
        this.presentadorJugador = presentadorJugador;
        this.navegacion = navegacion;
        this.presentadorAbandonar = new PresentadorAbandonar(this, presentadorJugador, navegacion);
        this.presentador = new PresentadorDisparo(this, navegacion);
        presentador.setDatosJugador(presentadorJugador);
        this.presentador.setIdJugador(this.presentadorJugador);

        labelAbandonar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                presentadorAbandonar.abandonarPartida();
            }
        });
    }
    
    private void cargarFuentes() {
        try {
            Font fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Micro5-Regular.ttf"));
            fuentePersonalizada = fuentePersonalizada.deriveFont(14f);
            labelNavesDañadas.setFont(fuentePersonalizada);
            labelNavesDañadasRival.setFont(fuentePersonalizada);
            labelNavesDestruidas.setFont(fuentePersonalizada);
            labelNavesDestruidasRival.setFont(fuentePersonalizada);
            labelNavesIntactas.setFont(fuentePersonalizada);
            labelNavesIntactasRival.setFont(fuentePersonalizada);
            fuentePersonalizada = fuentePersonalizada.deriveFont(20f);
            jugador1lbl.setFont(fuentePersonalizada);
            jugador2lbl.setFont(fuentePersonalizada);
            fuentePersonalizada = fuentePersonalizada.deriveFont(30f);
            labelTurno.setFont(fuentePersonalizada);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarNombresJugadores(String nombreJugador, String nombreRival) {
        SwingUtilities.invokeLater(() -> {

            if (nombreJugador != null && !nombreJugador.isEmpty()) {
                jugador1lbl.setText(nombreJugador);
            }
            if (nombreRival != null && !nombreRival.isEmpty()) {
                jugador2lbl.setText(nombreRival);
            }

            revalidate();
            repaint();
        });
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

    public void setIdJugador(PresentadorJugador presentadorJugador) {
        if (presentador != null) {
            presentador.setIdJugador(presentadorJugador);
        }
    }

    public void crearTablerosDeJuego() {
        try {
            Font fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Micro5-Regular.ttf"));
            fuentePersonalizada = fuentePersonalizada.deriveFont(50f);

            int anchoTablero = 400;
            int altoTablero = 400;
            int espacioEntreTableros = 100;

            int yPos = 180;

            int xPosTablero1 = 220;
            int xPosTablero2 = 820;

            JPanel panelTablero1 = crearPanelTablero(anchoTablero, altoTablero, false);
            panelTablero1.setBounds(xPosTablero1, yPos, anchoTablero, altoTablero);

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

    public void colocarNaves() {
        ClienteTablero tablero = presentador.getClienteTablero();
        System.out.println("Iniciando colocación de naves");
        System.out.println("Tablero recibido: " + tablero);
        if (tablero == null) {
            return;
        }

        int[][] casillas = tablero.getCasillas();

        System.out.println("Estado del tablero:");
        for (int i = 0; i < tablero.getAlto(); i++) {
            for (int j = 0; j < tablero.getAncho(); j++) {
                System.out.print(casillas[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Estado del tablero:");
        for (int i = 0; i < tablero.getAlto(); i++) {
            for (int j = 0; j < tablero.getAncho(); j++) {
                if (casillas[i][j] == 1) {
                    Dimension naveDimension = obtenerDimensionNave(casillas, i, j);
                    if (naveDimension != null) {
                        colocarNaveVisual(i, j, naveDimension.width, naveDimension.height);
                        marcarCasillasProcesadas(casillas, i, j, naveDimension.width, naveDimension.height);
                    }
                }
            }
        }
    }

    private Dimension obtenerDimensionNave(int[][] casillas, int fila, int columna) {
        int horizontal = 0;
        int vertical = 0;

        for (int j = columna; j < casillas[0].length && casillas[fila][j] == 1; j++) {
            horizontal++;
        }

        for (int i = fila; i < casillas.length && casillas[i][columna] == 1; i++) {
            vertical++;
        }

        if (horizontal > vertical) {
            return new Dimension(horizontal, 1);
        } else if (vertical > horizontal) {
            return new Dimension(1, vertical);
        } else {
            return new Dimension(1, 1);
        }
    }

    private void colocarNaveVisual(int fila, int columna, int horizontal, int vertical) {
        Icon iconoNave = determinarIconoNave(Math.max(horizontal, vertical));

        if (horizontal > vertical) {
            System.out.println("Colocando nave horizontal");
            for (int j = 0; j < horizontal; j++) {
                casillasPropio[fila][columna + j].setIcon(iconoNave);
            }
        }
        else {
            System.out.println("Colocando nave vertical");
            for (int i = 0; i < vertical; i++) {
                casillasPropio[fila + i][columna].setIcon(iconoNave);
            }
        }
    }

    private void marcarCasillasProcesadas(int[][] casillas, int fila, int columna, int horizontal, int vertical) {
        if (horizontal > vertical) {
            for (int j = 0; j < horizontal; j++) {
                casillas[fila][columna + j] = 2;
            }
        } else {
            for (int i = 0; i < vertical; i++) {
                casillas[fila + i][columna] = 2;
            }
        }
    }

    private Icon determinarIconoNave(int tamano) {
        ImageIcon icon1;
        ImageIcon icon2;
        ImageIcon icon3;
        ImageIcon icon4;
        if (presentador.colorJugador(presentadorJugador).equals("AZUL")) {
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

        icon1 = rotarImagen(icon1, 90);
        icon2 = rotarImagen(icon2, 90);
        icon3 = rotarImagen(icon3, 90);
        icon4 = rotarImagen(icon4, 90);

        return switch (tamano) {
            case 1 ->
                icon1;
            case 2 ->
                icon2;
            case 3 ->
                icon3;
            case 4 ->
                icon4;
            default ->
                null;
        };
    }

    private JPanel crearPanelTablero(int ancho, int alto, boolean esTableroDisparos) {
        JPanel panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(10, 10));
        panelTablero.setPreferredSize(new Dimension(ancho, alto));

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                JButton casilla = new JButton();
                casilla.setPreferredSize(new Dimension(ancho / 10, alto / 10));
                casilla.setBackground(new Color(139, 69, 19));

                if (esTableroDisparos) {
                    final int columna = x;
                    final int fila = y;
                    casilla.addActionListener(e -> {
                        try {
                            presentador.enviarDisparo(columna, fila);
                        } catch (Exception ex) {
                            mostrarError("Error al realizar disparo: " + ex.getMessage());
                        }
                    });
                }

                panelTablero.add(casilla);
                if (!esTableroDisparos) {
                    casillasPropio[y][x] = casilla;
                } else {
                    casillasDisparos[y][x] = casilla;
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

    @Override
    public void actualizarCasillaDisparo(int x, int y, String resultado) {
        Color color;
        switch (resultado) {
            case "AGUA":
                color = Color.BLUE;
                break;
            case "IMPACTO":
                color = Color.RED;
                break;
            case "HUNDIDO":
                System.out.println("Pintando casilla hundida [" + x + "," + y + "] de gris");
                color = Color.DARK_GRAY;
                break;
            default:
                color = new Color(139, 69, 19);
        }
        casillasDisparos[y][x].setBackground(color);
        casillasDisparos[y][x].repaint();
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
    public void actualizarCasillaPropia(int x, int y, String resultado) {
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
                return;
        }
        casillasPropio[y][x].setBackground(color);
    }

    @Override
    public void mostrarFinJuego(String ganador) {
        SwingUtilities.invokeLater(() -> {
            String mensaje;
            if (ganador.equals(presentadorJugador.getModeloJugador().getId())) {
                mensaje = "¡Felicidades! Has ganado la partida";
            } else {
                mensaje = "Game Over - Ha ganado el jugador " + ganador;
            }

            JOptionPane.showMessageDialog(
                    this,
                    mensaje,
                    "Fin del Juego",
                    JOptionPane.INFORMATION_MESSAGE
            );

            habilitarTableroDisparos(false);
        });
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
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

    @Override
    public int mostrarConfirmacionAbandono() {
        return JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro que deseas abandonar la partida?",
                "Confirmar Abandono",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
    }

    @Override
    public void mostrarMensajeAbandonoOponente() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    this,
                    "El oponente ha abandonado la partida",
                    "Partida Terminada",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    public PresentadorDisparo getPresentador() {
        return presentador;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jugador2lbl = new javax.swing.JLabel();
        labelAbandonar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labelNavesIntactas = new javax.swing.JLabel();
        labelNavesDañadas = new javax.swing.JLabel();
        labelNavesDestruidas = new javax.swing.JLabel();
        labelTurno = new javax.swing.JLabel();
        labelNavesIntactasRival = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        labelNavesDestruidasRival = new javax.swing.JLabel();
        labelNavesDañadasRival = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jugador1lbl = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1440, 800));
        setMinimumSize(new java.awt.Dimension(1440, 800));
        setLayout(null);

        jugador2lbl.setText("Jugador Dos");
        add(jugador2lbl);
        jugador2lbl.setBounds(780, 120, 170, 30);

        labelAbandonar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/botonAbandonar.png"))); // NOI18N
        add(labelAbandonar);
        labelAbandonar.setBounds(1160, 720, 220, 60);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/hojasDecoracion.png"))); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(960, 0, 479, 430);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Green Rectangle.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        add(jLabel1);
        jLabel1.setBounds(120, 80, 20, 20);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Yellow Rectangle.png"))); // NOI18N
        jLabel3.setText("jLabel1");
        add(jLabel3);
        jLabel3.setBounds(120, 20, 20, 20);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Red Rectangle.png"))); // NOI18N
        jLabel4.setText("jLabel1");
        add(jLabel4);
        jLabel4.setBounds(120, 50, 20, 20);

        labelNavesIntactas.setText("11");
        add(labelNavesIntactas);
        labelNavesIntactas.setBounds(150, 80, 80, 16);

        labelNavesDañadas.setText("0");
        add(labelNavesDañadas);
        labelNavesDañadas.setBounds(150, 20, 70, 16);

        labelNavesDestruidas.setText("0");
        add(labelNavesDestruidas);
        labelNavesDestruidas.setBounds(150, 50, 60, 16);

        labelTurno.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        labelTurno.setText("Tu turno");
        add(labelTurno);
        labelTurno.setBounds(600, 730, 260, 50);

        labelNavesIntactasRival.setText("11");
        add(labelNavesIntactasRival);
        labelNavesIntactasRival.setBounds(860, 80, 70, 16);

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
        labelNavesDestruidasRival.setBounds(860, 50, 80, 16);

        labelNavesDañadasRival.setText("0");
        add(labelNavesDañadasRival);
        labelNavesDañadasRival.setBounds(860, 20, 80, 16);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Yellow Rectangle.png"))); // NOI18N
        jLabel9.setText("jLabel1");
        add(jLabel9);
        jLabel9.setBounds(830, 20, 20, 20);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondoArena.png"))); // NOI18N
        add(jLabel10);
        jLabel10.setBounds(0, 620, 1440, 190);

        jugador1lbl.setText("Jugador Uno");
        add(jugador1lbl);
        jugador1lbl.setBounds(180, 120, 160, 30);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jugador1lbl;
    private javax.swing.JLabel jugador2lbl;
    private javax.swing.JLabel labelAbandonar;
    private javax.swing.JLabel labelNavesDañadas;
    private javax.swing.JLabel labelNavesDañadasRival;
    private javax.swing.JLabel labelNavesDestruidas;
    private javax.swing.JLabel labelNavesDestruidasRival;
    private javax.swing.JLabel labelNavesIntactas;
    private javax.swing.JLabel labelNavesIntactasRival;
    private javax.swing.JLabel labelTurno;
    // End of variables declaration//GEN-END:variables

}
