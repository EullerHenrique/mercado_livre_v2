package euller.mercado_livre.server.cliente.respository;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class PedidoRepository {
    private final Hashtable<String, List<Hashtable<String, String>>> pedidos = new Hashtable<>();

    public String criarPedido(String CID, String OID, String dados) {
        Hashtable<String, String> pedido = new Hashtable<>();
        pedido.put(OID, dados);
        if(!pedidos.containsKey(CID)) {
            List<Hashtable<String, String>> pedidos = new ArrayList<>();
            pedidos.add(pedido);
            this.pedidos.put(CID, pedidos);
        }else{
            this.pedidos.get(CID).add(pedido);
        }
        return listarPedido(CID, OID);
    }

    public String modificarPedido(String CID, String OID, String dados) {
        if (pedidos.containsKey(CID) && pedidos.get(CID).size() > 0) {
            for (Hashtable<String, String> pedido : pedidos.get(CID)) {
                if (pedido.containsKey(OID)) {
                    pedido.remove(OID);
                    pedido.put(OID, dados);
                    return listarPedido(CID, OID);
                }
            }
        }
        return " Pedido não encontrado";
    }

    public String listarPedido(String CID, String OID) {
        for (Hashtable<String, String> pedido : pedidos.get(CID)) {
            if (pedido.containsKey(OID)) {
                return pedido.get(OID);
            }
        }
        return " Pedido não encontrado";
    }

    public List<Hashtable<String, String>> listarPedidos(String CID) {
        if (pedidos.containsKey(CID)) {
            return pedidos.get(CID);
        }
        return null;
    }

    public String apagarPedido(String CID, String OID) {
        if (pedidos.containsKey(CID)) {
            for (Hashtable<String, String> pedido : pedidos.get(CID)) {
                if (pedido.containsKey(OID)) {
                    pedido.remove(OID);
                    return " Pedido apagado";
                }
            }
        }
        return " Pedido não encontrado";
    }
}
