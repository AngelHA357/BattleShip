
package org.itson.arquitectura.battleshipservidor.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import org.itson.arquitectura.battleshipservidor.dominio.Coordenada;
import org.itson.arquitectura.battleshipservidor.dominio.Tablero.ConcreteBuilderTablero;
import org.itson.arquitectura.battleshipservidor.dominio.Tablero.Tablero;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.Casilla;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.CasillaFlyweight;
import org.itson.arquitectura.battleshipservidor.dominio.casilla.CasillaFlyweightFactory;
import org.itson.arquitectura.battleshipservidor.dominio.enums.EstadoCasilla;
import org.itson.arquitectura.battleshipservidor.dominio.nave.Barco;
import org.itson.arquitectura.battleshipservidor.dominio.nave.BarcoFactory;
import org.itson.arquitectura.battleshipservidor.dominio.nave.Crucero;
import org.itson.arquitectura.battleshipservidor.dominio.nave.CruceroFactory;
import org.itson.arquitectura.battleshipservidor.dominio.nave.Nave;
import org.itson.arquitectura.battleshipservidor.dominio.nave.PortaAviones;
import org.itson.arquitectura.battleshipservidor.dominio.nave.PortaAvionesFactory;
import org.itson.arquitectura.battleshipservidor.dominio.nave.Submarino;
import org.itson.arquitectura.battleshipservidor.dominio.nave.SubmarinoFactory;


/**
 *
 * @author victo
 */
public class ControladorEventos {
    
    
    public EventoDTO crearPartida(EventoDTO evento){
    
        return evento;
    }
    public EventoDTO unirsePartida(EventoDTO evento){
    
        return evento;
    }
    public EventoDTO jugadorListo(EventoDTO evento){
    
        return evento;
    }
    public EventoDTO crearNaves(EventoDTO evento){
    
        return evento;
    }
    public EventoDTO colocarNaves(EventoDTO evento){
    
        return evento;
    }
    public EventoDTO inicializarTablero(EventoDTO evento){
    
        return evento;
    }
    public EventoDTO disparar(EventoDTO evento){
    
        return evento;
    }
    
    
    public List<Nave> crearNaves(){
        BarcoFactory barcoFactory = new BarcoFactory();
        PortaAvionesFactory portaAvionesFactory = new PortaAvionesFactory();
        SubmarinoFactory submarinoFactory = new SubmarinoFactory();
        CruceroFactory cruceroFactory =  new CruceroFactory();
        
        Barco barco = (Barco) barcoFactory.createNave();
        PortaAviones portaAviones = (PortaAviones) portaAvionesFactory.createNave();
        Submarino submarino = (Submarino) submarinoFactory.createNave();
        Crucero crucero = (Crucero) cruceroFactory.createNave();
        
        List<Nave> naves = new ArrayList<>();
        naves.add(barco);
        naves.add(portaAviones);
        naves.add(submarino);
        naves.add(crucero);
        
        return naves;
    }

    public Tablero inicializarTablero() {
        ConcreteBuilderTablero builder = new ConcreteBuilderTablero();
        CasillaFlyweightFactory factory = new CasillaFlyweightFactory();
        builder.setAlto(10).setAncho(10);
        
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Coordenada coordenada = new Coordenada(i, j);
                CasillaFlyweight flyweight = factory.getFlyweight(EstadoCasilla.LIBRE);
                Casilla casilla = new Casilla(coordenada, flyweight);
                builder.addCasilla(casilla);
            }
        }

        return builder.build();
    }
}
