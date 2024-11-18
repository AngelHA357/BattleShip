
package org.itson.arquitectura.battleshipservidor.controlador;

import java.util.ArrayList;
import java.util.List;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
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
}
