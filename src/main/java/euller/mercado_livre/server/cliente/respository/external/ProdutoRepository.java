package euller.mercado_livre.server.cliente.respository.external;

import com.google.gson.Gson;
import euller.mercado_livre.server.cliente.model.Produto;
import euller.mercado_livre.server.cliente.service.mosquitto.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ProdutoRepository {

    private final MosquittoService mosquittoService = new MosquittoService();

    public String buscarProduto(String PID) throws MqttException {
        Gson gson = new Gson();

        String produtoJson;
        Produto produtoModel;
        for (int i = 0 ; i <= 100; i++){
            produtoJson = mosquittoService.buscarProduto(PID);
            if(!produtoJson.equals("false")) {
                produtoModel = gson.fromJson(produtoJson, Produto.class);
                if(produtoModel.getPID().equals(PID)){
                    return produtoJson;
                }
            }
        }
        return "false";

    }

    public void modificarProduto(Produto produto) throws MqttException {
        mosquittoService.modificarProduto(produto);
    }
}
