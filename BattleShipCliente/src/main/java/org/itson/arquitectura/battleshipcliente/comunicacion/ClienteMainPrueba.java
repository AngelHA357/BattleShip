package org.itson.arquitectura.battleshipcliente.comunicacion;

/**
 *
 * @author victo
 */
public class ClienteMainPrueba {

    public static void main(String[] args) {
        SocketCliente cliente = SocketCliente.getInstance();
        
        if (cliente.conectar("localhost")) {
            System.out.println("NO TE ENTIENDO");
        }
        

        
    }

}
