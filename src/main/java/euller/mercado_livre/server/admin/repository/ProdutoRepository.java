package euller.mercado_livre.server.admin.repository;

import com.google.gson.Gson;
import euller.mercado_livre.server.admin.model.Produto;
import euller.mercado_livre.server.admin.service.mosquitto.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Hashtable;
import java.util.logging.Logger;

public class ProdutoRepository {

    private final Logger logger = Logger.getLogger(ProdutoRepository.class.getName());
    private Hashtable<String, String> produtos = new Hashtable<>();

    private final MosquittoService mosquittoService = new MosquittoService();

    public String criarProduto(Produto produto, boolean otherServerUpdate) {
        logger.info("Criando produto: "+produto+"\n");
        String PID = produto.getPID();
        if(!produtos.containsKey(PID)) {
            Gson gson = new Gson();
            String produtoJson = gson.toJson(produto);
            produtos.put(PID, produtoJson);
            String produtoBD = buscarProduto(PID);
            if (!otherServerUpdate) {
                try {
                    mosquittoService.publish("server/admin/produto/criar",produtoBD);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
            return produtoBD;
        }
        return null;
    }

    public String modificarProduto(Produto produto, boolean otherServerUpdate) {
        logger.info("Modificando produto: "+produto+"\n");
        String PID = produto.getPID();
        Gson gson = new Gson();
        String produtoJson = gson.toJson(produto);
        if(produtos.containsKey(PID)){
            produtos.remove(PID);
            produtos.put(PID, produtoJson);
            String produtoBD = buscarProduto(PID);
            if(!otherServerUpdate) {
                try {
                    mosquittoService.publish("server/admin/produto/modificar", produtoBD);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
            return produtoBD;
        }
        return null;
    }

    public String buscarProduto(String PID){
        logger.info("Buscando produto: "+PID+"\n");
        if(produtos.containsKey(PID)) {
            return produtos.get(PID);
        }
        return null;
    }

    public String apagarProduto(String PID, boolean otherServerUpdate){
        logger.info("Apagando produto: "+PID+"\n");
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
