package euller.mercado_livre.server.cliente.respository;

import com.google.gson.Gson;
import euller.mercado_livre.server.cliente.model.Pedido;
import euller.mercado_livre.server.cliente.service.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

public class PedidoRepository {
    private final Logger logger = Logger.getLogger(PedidoRepository.class.getName());
    private final Hashtable<String, List<Hashtable<String, String>>> pedidos = new Hashtable<>();
    private MosquittoService mosquittoService = new MosquittoService();

    public String criarPedido(Pedido pedidoModel, boolean otherServerUpdate) {
        logger.info("\nCriando pedido: "+pedidoModel+"\n");
        String CID = pedidoModel.getCID();
        String OID = pedidoModel.getOID();
        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedidoModel);
        Hashtable<String, String> pedido = new Hashtable<>();
        pedido.put(OID, pedidoJson);
        if(!pedidos.containsKey(CID)) {
            List<Hashtable<String, String>> pedidos = new ArrayList<>();
            pedidos.add(pedido);
            this.pedidos.put(CID, pedidos);
        }else{
            this.pedidos.get(CID).add(pedido);
        }
        String pedidoBD = buscarPedido(CID, OID);
        if(!otherServerUpdate) {
            try {
                mosquittoService.publish("server/cliente/pedido/criar", pedidoBD);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
        return pedidoBD;
    }

    public String modificarPedido(Pedido pedidoModel, boolean otherServerUpdate) {
        logger.info("Modificando pedido: "+pedidoModel+"\n");
        String CID = pedidoModel.getCID();
        String OID = pedidoModel.getOID();
        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedidoModel);
        if (pedidos.containsKey(CID) && pedidos.get(CID).size() > 0) {
            for (Hashtable<String, String> pedido : pedidos.get(CID)) {
                if (pedido.containsKey(OID)) {
                    pedido.put(OID, pedidoJson);
                    String pedidoBD = buscarPedido(CID, OID);
                    if(!otherServerUpdate) {
                        try {
                            mosquittoService.publish("server/cliente/pedido/modificar",pedidoBD);
                        } catch (MqttException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return pedidoBD;
                }
            }
        }
        return null;
    }

    public String buscarPedido(String CID, String OID) {
        logger.info("Buscando pedido: "+"CID: "+CID+"OID: "+OID+"\n");
        if(pedidos.containsKey(CID)) {
            for(Hashtable<String, String> pedido : pedidos.get(CID)) {
                if(pedido.containsKey(OID)) {
                    return pedido.get(OID);
                }
            }
        }
        return null;
    }

    public List<Hashtable<String, String>> buscarPedidos(String CID) {
        logger.info("Buscando pedidos:  "+"CID: "+CID+"\n");
        if (pedidos.containsKey(CID)) {
            return pedidos.get(CID);
        }
        return null;
    }

    public String apagarPedido(String CID, String OID, boolean otherServerUpdate) {
        logger.info("Apagando pedido: "+"CID: "+CID+"OID: "+OID+"\n");
        if (pedidos.containsKey(CID)) {
            for (Hashtable<String, String> pedido : pedidos.get(CID)) {
                if (pedido.containsKey(OID)) {
                    pedido.remove(OID);
                    if(!otherServerUpdate) {
                        try {
                            mosquittoService.publish("server/cliente/pedido/apagar", buscarPedido(CID, OID));
                        } catch (MqttException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return "Pedido apagado";
                }
            }
        }
        return null;
    }
}
