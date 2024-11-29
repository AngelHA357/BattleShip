package com.mycompany.battleshippresentacion.presentador;

import com.mycompany.battleshippresentacion.ivista.IVistaJugarPartida;
import com.mycompany.battleshippresentacion.modelo.ClienteTablero;
import com.mycompany.battleshippresentacion.modelo.ModeloDisparo;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshiptransporte.DTOs.EventoDTO;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

public class PresentadorDisparo implements SocketCliente.EventoListener {

    private final IVistaJugarPartida vista;
    private final ModeloDisparo modelo;
    private final SocketCliente socketCliente;
    private volatile boolean esperandoRespuesta = false;
    private final Object lock = new Object();
    private volatile Exception errorConexion = null;
    private String idJugador;

    public PresentadorDisparo(IVistaJugarPartida vista) {
        this.vista = vista;
        this.modelo = new ModeloDisparo();
        this.socketCliente = SocketCliente.getInstance();
        this.socketCliente.setEventoListener(this);
    }

    public void setIdJugador(String idJugador) {
        this.idJugador = idJugador;
    }

    public boolean enviarDisparo(int fila, int columna) throws Exception {
        if (!modelo.isTurnoPropio()) {
            vista.mostrarError("No es tu turno");
            return false;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("coordenadaX", columna);
        data.put("coordenadaY", fila);

        EventoDTO eventoDTO = new EventoDTO(Evento.DISPARAR, data);

        synchronized (lock) {
            esperandoRespuesta = true;
            socketCliente.enviarEvento(eventoDTO);

            try {
                lock.wait(5000);
            } catch (InterruptedException e) {
                throw new Exception("Interrupción mientras se esperaba respuesta del servidor", e);
            }

            if (errorConexion != null) {
                Exception error = errorConexion;
                errorConexion = null;
                throw error;
            }

            if (esperandoRespuesta) {
                throw new Exception("Timeout al esperar respuesta del servidor");
            }
        }

        return true;
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
        if (evento.getEvento().equals(Evento.DISPARAR)) {
            try {
                Map<String, Object> datos = evento.getDatos();
                if (datos == null) {
                    errorConexion = new Exception("Datos del evento son null");
                    return;
                }

                procesarRespuestaDisparo(datos);

                synchronized (lock) {
                    esperandoRespuesta = false;
                    lock.notify();
                }

            } catch (Exception e) {
                synchronized (lock) {
                    errorConexion = e;
                    esperandoRespuesta = false;
                    lock.notify();
                }
            }
        }
    }

    private void procesarRespuestaDisparo(Map<String, Object> datos) {
        String resultado = (String) datos.get("resultado");
        int fila = (int) datos.get("coordenadaY");
        int columna = (int) datos.get("coordenadaX");
        String jugadorActual = (String) datos.get("jugadorActual");

        modelo.registrarDisparo(fila, columna, resultado);

        if (datos.containsKey("navesIntactasPropias")) {
            modelo.actualizarNavesPropio(
                    (int) datos.get("navesIntactasPropias"),
                    (int) datos.get("navesDanadasPropias"),
                    (int) datos.get("navesDestruidasPropias")
            );
        }

        if (datos.containsKey("navesIntactasRival")) {
            modelo.actualizarNavesRival(
                    (int) datos.get("navesIntactasRival"),
                    (int) datos.get("navesDanadasRival"),
                    (int) datos.get("navesDestruidasRival")
            );
        }

        boolean esTurnoPropio = jugadorActual.equals(idJugador);
        modelo.setTurnoPropio(esTurnoPropio);
        inicializarTurno(esTurnoPropio);

        if (datos.containsKey("finJuego")) {
            modelo.setJuegoTerminado((boolean) datos.get("finJuego"));
            modelo.setJugadorGanador((String) datos.get("ganador"));
        }

        actualizarVista(fila, columna, resultado);
    }

    private void actualizarVista(int fila, int columna, String resultado) {
        vista.actualizarCasillaDisparo(fila, columna, resultado);

        vista.actualizarContadoresNavesPropio(
                modelo.getNavesIntactasPropias(),
                modelo.getNavesDanadasPropias(),
                modelo.getNavesDestruidasPropias()
        );

        vista.actualizarContadoresNavesRival(
                modelo.getNavesIntactasRival(),
                modelo.getNavesDanadasRival(),
                modelo.getNavesDestruidasRival()
        );

        vista.actualizarTurno(modelo.isTurnoPropio());
        vista.habilitarTableroDisparos(modelo.isTurnoPropio());

        if (modelo.isJuegoTerminado()) {
            vista.mostrarFinJuego(modelo.getJugadorGanador());
        }
    }

    public void inicializarTurno(boolean esTurnoPropio) {
        modelo.setTurnoPropio(esTurnoPropio);
        vista.actualizarTurno(esTurnoPropio);
        vista.habilitarTableroDisparos(esTurnoPropio);
    }
    
    public ClienteTablero getClienteTablero() {
        return modelo.getClienteTablero(); 
    }
    
    public void setClienteTablero(ClienteTablero clienteTablero){
        modelo.setClienteTablero(clienteTablero);
    }
    
}
