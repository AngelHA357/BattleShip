package com.mycompany.battleshippresentacion.modelo;

/**
 * Modelo que mantiene el estado del juego y los disparos
 */
public class ModeloDisparo {

    private int navesIntactasPropias;
    private int navesDanadasPropias;
    private int navesDestruidasPropias;

    private int navesIntactasRival;
    private int navesDanadasRival;
    private int navesDestruidasRival;

    private boolean turnoPropio;
    private boolean juegoTerminado;
    private String jugadorGanador;

    private String[][] tableroPropio;
    private String[][] tableroDisparos;
    private ClienteTablero clienteTablero;

    public ModeloDisparo() {
        this.navesIntactasPropias = 11;
        this.navesDanadasPropias = 0;
        this.navesDestruidasPropias = 0;

        this.navesIntactasRival = 11;
        this.navesDanadasRival = 0;
        this.navesDestruidasRival = 0;

        this.turnoPropio = false;
        this.juegoTerminado = false;

        this.tableroPropio = new String[10][10];
        this.tableroDisparos = new String[10][10];
        inicializarTableros();
    }

    private void inicializarTableros() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tableroPropio[i][j] = "";
                tableroDisparos[i][j] = "";
            }
        }
    }

    public void actualizarNavesPropio(int intactas, int danadas, int destruidas) {
        this.navesIntactasPropias = intactas;
        this.navesDanadasPropias = danadas;
        this.navesDestruidasPropias = destruidas;
    }

    public void actualizarNavesRival(int intactas, int danadas, int destruidas) {
        this.navesIntactasRival = intactas;
        this.navesDanadasRival = danadas;
        this.navesDestruidasRival = destruidas;
    }

    public void registrarDisparo(int fila, int columna, String resultado) {
        tableroDisparos[fila][columna] = resultado;
    }

    public void registrarImpactoRecibido(int fila, int columna, String resultado) {
        tableroPropio[fila][columna] = resultado;
    }

    public int getNavesIntactasPropias() {
        return navesIntactasPropias;
    }

    public int getNavesDanadasPropias() {
        return navesDanadasPropias;
    }

    public int getNavesDestruidasPropias() {
        return navesDestruidasPropias;
    }

    public int getNavesIntactasRival() {
        return navesIntactasRival;
    }

    public int getNavesDanadasRival() {
        return navesDanadasRival;
    }

    public int getNavesDestruidasRival() {
        return navesDestruidasRival;
    }

    public boolean isTurnoPropio() {
        return turnoPropio;
    }

    public void setTurnoPropio(boolean turnoPropio) {
        this.turnoPropio = turnoPropio;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void setJuegoTerminado(boolean juegoTerminado) {
        this.juegoTerminado = juegoTerminado;
    }

    public String getJugadorGanador() {
        return jugadorGanador;
    }

    public void setJugadorGanador(String jugadorGanador) {
        this.jugadorGanador = jugadorGanador;
    }

    public String getEstadoCasilla(int fila, int columna, boolean esTableroPropio) {
        return esTableroPropio ? tableroPropio[fila][columna] : tableroDisparos[fila][columna];
    }
    
    public void setClienteTablero(ClienteTablero clienteTablero) {
        this.clienteTablero = clienteTablero;
    }

    public ClienteTablero getClienteTablero() {
        return clienteTablero;
    }
}
