package euller.mercado_livre.server.cliente.respository.external;

import euller.mercado_livre.server.cliente.model.Produto;
import euller.mercado_livre.server.cliente.service.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ProdutoRepository {

    private final MosquittoService mosquittoService = new MosquittoService();

    public String buscarProduto(String PID) throws MqttException {
        return mosquittoService.buscarProduto(PID);
    }

    public void modificarProduto(Produto produto) throws MqttException {
        mosquittoService.modificarProduto(produto);
    }
}
