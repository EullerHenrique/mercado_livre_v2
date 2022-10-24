package euller.mercado_livre.server.cliente.respository;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class PedidoRepository {

    private Hashtable<String, String> pedidos = new Hashtable<String, String>();

    public void criarPedido(String OID, String dados) {
        pedidos.put(OID, dados);
    }

    public void modificarPedido(String OID, String dados) {
        pedidos.put(OID, dados);
    }

    public String listarPedido(String CID, String OID) {
        return pedidos.get(OID);
    }
}
