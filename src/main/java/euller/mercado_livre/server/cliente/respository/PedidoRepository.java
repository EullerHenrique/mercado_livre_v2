package euller.mercado_livre.server.cliente.respository;

import com.google.gson.Gson;
import euller.mercado_livre.server.admin.model.Produto;
import euller.mercado_livre.server.admin.repository.ProdutoRepository;
import euller.mercado_livre.server.cliente.model.Pedido;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class PedidoRepository {
    private final Hashtable<String, List<Hashtable<String, String>>> pedidos = new Hashtable<>();

    public String criarPedido(Pedido pedidoModel) {
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
        return listarPedido(CID, OID);
    }

    public String modificarPedido(Pedido pedidoNovo) {
        String CID = pedidoNovo.getCID();
        String OID = pedidoNovo.getOID();
        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedidoNovo);
        if (pedidos.containsKey(CID) && pedidos.get(CID).size() > 0) {
            for (Hashtable<String, String> pedido : pedidos.get(CID)) {
                if (pedido.containsKey(OID)) {
                    pedido.put(OID, pedidoJson);
                    return listarPedido(CID, OID);
                }
            }
        }
        return null;
    }

    public String listarPedido(String CID, String OID) {
        for (Hashtable<String, String> pedido : pedidos.get(CID)) {
            if (pedido.containsKey(OID)) {
                return pedido.get(OID);
            }
        }
        return null;
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
        return null;
    }
}
