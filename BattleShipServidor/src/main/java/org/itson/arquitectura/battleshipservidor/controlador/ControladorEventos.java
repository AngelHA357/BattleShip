
package org.itson.arquitectura.battleshipservidor.controlador;


/**
 *
 * @author victo
 */
public class ControladorEventos {

//    
//    public List<Nave> crearNaves(){
//        BarcoFactory barcoFactory = new BarcoFactory();
//        PortaAvionesFactory portaAvionesFactory = new PortaAvionesFactory();
//        SubmarinoFactory submarinoFactory = new SubmarinoFactory();
//        CruceroFactory cruceroFactory =  new CruceroFactory();
//        
//        Barco barco = (Barco) barcoFactory.createNave();
//        PortaAviones portaAviones = (PortaAviones) portaAvionesFactory.createNave();
//        Submarino submarino = (Submarino) submarinoFactory.createNave();
//        Crucero crucero = (Crucero) cruceroFactory.createNave();
//        
//        List<Nave> naves = new ArrayList<>();
//        naves.add(barco);
//        naves.add(portaAviones);
//        naves.add(submarino);
//        naves.add(crucero);
//        
//        return naves;
//    }
//
//    public Tablero inicializarTablero() {
//        ConcreteBuilderTablero builder = new ConcreteBuilderTablero();
//        CasillaFlyweightFactory factory = new CasillaFlyweightFactory();
//        builder.setAlto(10).setAncho(10);
//        
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                Coordenada coordenada = new Coordenada(i, j);
//                CasillaFlyweight flyweight = factory.getFlyweight(EstadoCasilla.LIBRE);
//                Casilla casilla = new Casilla(coordenada, flyweight);
//                builder.addCasilla(casilla);
//            }
//        }
//
//        return builder.build();
//    }
}
