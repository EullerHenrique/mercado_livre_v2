package euller.mercado_livre.server.admin.repository;


import com.google.gson.Gson;
import euller.mercado_livre.server.admin.model.Produto;
import euller.mercado_livre.server.admin.service.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Hashtable;

public class ProdutoRepository {
    private Hashtable<String, String> produtos = new Hashtable<>();

    private final MosquittoService mosquittoService = new MosquittoService();

    public String criarProduto(Produto produto, boolean otherServerUpdate) {
        String PID = produto.getPID();
        Gson gson = new Gson();
        String produtoJson = gson.toJson(produto);
        produtos.put(PID, produtoJson);
        if(!otherServerUpdate) {
            try {
                mosquittoService.publish("server/admin/produto/criar", PID + " , " + buscarProduto(PID));
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
        return "PID: " + PID + " Produto: "+ buscarProduto(PID);
    }

    public String modificarProduto(Produto produto, boolean otherServerUpdate) {
        String PID = produto.getPID();
        Gson gson = new Gson();
        String produtoJson = gson.toJson(produto);
        if(produtos.containsKey(PID)){
            produtos.remove(PID);
            produtos.put(PID, produtoJson);
            if(!otherServerUpdate) {
                try {
                    mosquittoService.publish("server/admin/produto/modificar", buscarProduto(PID));
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
            return buscarProduto(PID);
        }
        return null;
    }

    public String buscarProduto(String PID){
        if(produtos.containsKey(PID)) {
            return produtos.get(PID);
        }
        return null;
    }

    public String apagarProduto(String PID, boolean otherServerUpdate){
        if (produtos.containsKey(PID)) {
            produtos.remove(PID);
            if(!otherServerUpdate) {
                try {
                    mosquittoService.publish("server/admin/produto/apagar", PID);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
            return " Produto apagado";
        }
        return null;
    }
}
