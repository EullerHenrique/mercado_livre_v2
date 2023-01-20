package euller.mercado_livre.server.cliente.respository.external;

import com.google.gson.Gson;
import euller.mercado_livre.server.cliente.model.Produto;
import euller.mercado_livre.server.cliente.service.mosquitto.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ProdutoRepository {

    private final MosquittoService mosquittoService = new MosquittoService();

    public String buscarProduto(String PID) throws MqttException {
        String produto = mosquittoService.buscarProduto(PID);
        Gson gson = new Gson();
        Produto produtoModel = gson.fromJson(produto, Produto.class);
        while(!produtoModel.getPID().equals(PID)){
            produto = mosquittoService.buscarProduto(PID);
            produtoModel = gson.fromJson(produto, Produto.class);
        }
        return produto;
    }

    public void modificarProduto(Produto produto) throws MqttException {
        mosquittoService.modificarProduto(produto);
    }
}
