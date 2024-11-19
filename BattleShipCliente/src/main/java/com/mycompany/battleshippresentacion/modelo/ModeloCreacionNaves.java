
package com.mycompany.battleshippresentacion.modelo;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.itson.arquitectura.battleshipcliente.comunicacion.SocketCliente;
import org.itson.arquitectura.battleshipeventos.DTOs.EventoDTO;
import static org.itson.arquitectura.battleshipeventos.eventos.Evento.CREAR_NAVES;

/**
 *
 * @author JoseH
 */
public class ModeloCreacionNaves implements SocketCliente.EventoListener {

    List<ClienteNave> listaClntNvs;

    public List<ClienteNave> crearNaves() throws Exception {
        try {
            listaClntNvs = new ArrayList<>();

            EventoDTO event = new EventoDTO(CREAR_NAVES, new HashMap<>());

            SocketCliente socketCliente = SocketCliente.getInstance();
            socketCliente.setEventoListener(this);

            if (!socketCliente.conectar("localhost")) {
                throw new Exception("No se pudo conectar al servidor");
            }

            socketCliente.enviarEvento(event);

            long timeout = System.currentTimeMillis() + 5000; // 5 segundos
            while (listaClntNvs.isEmpty() && System.currentTimeMillis() < timeout) {
                Thread.sleep(100);
            }

            if (listaClntNvs.isEmpty()) {
                throw new Exception("Timeout al crear las naves");
            }

            return listaClntNvs;

        } catch (InterruptedException e) {
            throw new Exception("Error al crear las naves", e);
        }
    }

    @Override
    public void onEventoRecibido(EventoDTO evento) {
        try {
            Map<String, Object> datos = evento.getDatos();
            if (datos != null && datos.containsKey("naves")) {
                List<Map<String, Object>> navesList = (List<Map<String, Object>>) datos.get("naves");
                listaClntNvs = new ArrayList<>();

                for (Map<String, Object> naveMap : navesList) {
                    String nombre = (String) naveMap.get("nombre");
                    Integer tamano = (Integer) naveMap.get("tamano");
                    if (nombre != null && tamano != null) {
                        listaClntNvs.add(new ClienteNave(nombre, tamano));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al procesar evento de naves: " + e.getMessage());
        }
    }
}
