package org.itson.arquitectura.battleshiptransporte.DTOs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshiptransporte.eventos.Evento;

/**
 *
 * @author victo
 */
public class EventoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Evento evento;          
    private Map<String, Object> datos;  
    private String idJugador;        

    public EventoDTO(Evento evento, Map<String, Object> datos) {
        this.evento = evento;
        this.datos = datos;
    }

    public Evento getEvento() {
        return evento;
    }

    public Map<String, Object> getDatos() {
        return datos;
    }

    public void agregarDato(String clave, Object valor) {
        this.datos.put(clave, valor);
    }

    public String getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(String idJugador) {
        this.idJugador = idJugador;
    }
}
