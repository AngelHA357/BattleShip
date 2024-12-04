package com.mycompany.battleshippresentacion.ivista;

/**
 * Interfaz que define los métodos que debe implementar la vista de juego
 */
public interface IVistaJugarPartida {

    /**
     * Actualiza la vista de una casilla después de un disparo
     *
     * @param fila coordenada X del disparo
     * @param columna coordenada Y del disparo
     * @param resultado AGUA, IMPACTO o HUNDIDO
     */
    public void actualizarCasillaDisparo(int fila, int columna, String resultado);

    /**
     * Actualiza los contadores de estado de las naves propias
     *
     * @param navesIntactas número de naves sin daño
     * @param navesDanadas número de naves con algún impacto
     * @param navesDestruidas número de naves hundidas
     */
    public void actualizarContadoresNavesPropio(int navesIntactas, int navesDanadas, int navesDestruidas);

    /**
     * Actualiza los contadores de estado de las naves del rival
     *
     * @param navesIntactas número de naves sin daño
     * @param navesDanadas número de naves con algún impacto
     * @param navesDestruidas número de naves hundidas
     */
    public void actualizarContadoresNavesRival(int navesIntactas, int navesDanadas, int navesDestruidas);

    /**
     * Actualiza el estado de una casilla en el tablero propio
     *
     * @param fila coordenada X
     * @param columna coordenada Y
     * @param estado AGUA, IMPACTO o HUNDIDO
     */
    public void actualizarCasillaPropia(int fila, int columna, String estado);

    /**
     * Muestra el mensaje de fin de juego
     *
     * @param ganador identificador del jugador ganador
     */
    public void mostrarFinJuego(String ganador);

    /**
     * Muestra un mensaje de error
     *
     * @param mensaje texto del error
     */
    public void mostrarError(String mensaje);

    /**
     * Indica el turno actual
     *
     * @param esTurnoPropio true si es el turno del jugador actual
     */
    public void actualizarTurno(boolean esTurnoPropio);

    /**
     * Habilita o deshabilita el tablero de disparos según el turno
     *
     * @param habilitado true para habilitar, false para deshabilitar
     */
    public void habilitarTableroDisparos(boolean habilitado);

    /**
     * Muestra un diálogo de confirmación para abandonar la partida
     *
     * @return La opción seleccionada (YES_OPTION o NO_OPTION)
     */
    public int mostrarConfirmacionAbandono();

    /**
     * Muestra un mensaje indicando que el oponente abandonó la partida
     */
    public void mostrarMensajeAbandonoOponente();
}
