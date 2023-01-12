package euller.mercado_livre.server.admin.repository;

import com.google.gson.Gson;
import euller.mercado_livre.ratis.ReplicationClient;
import euller.mercado_livre.server.admin.model.Produto;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ProdutoRepository {

    private final Logger logger = Logger.getLogger(ProdutoRepository.class.getName());

    private final ReplicationClient replicationClient = new ReplicationClient();

    public String criarProduto(Produto produto) {
        logger.info("Criando produto: "+produto+"\n");
        String PID = produto.getPID();
        try {
            if (buscarProduto(PID) == null) {
                Gson gson = new Gson();
                String produtoJson = gson.toJson(produto);
                replicationClient.exec("add", PID, produtoJson);
                return produtoJson;
            }
            return null;
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String modificarProduto(Produto produto) {
        logger.info("Modificando produto: "+produto+"\n");
        String PID = produto.getPID();
        if(buscarProduto(PID) !=null){
            if(apagarProduto(PID) != null){
                String produtoJson = criarProduto(produto);
                if(produtoJson != null){
                    return produtoJson;
                };
            };
        }
        return null;
    }

    public String buscarProduto(String PID){
        logger.info("Buscando produto: "+PID+"\n");
        try {
            return replicationClient.exec("getAdmin", PID, null);
        }catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String apagarProduto(String PID){
        logger.info("Apagando produto: "+PID+"\n");
        try {
            if (buscarProduto(PID) != null) {
                replicationClient.exec("delAdmin", PID, null);
                return "Produto apagado";
            }
            return null;
        }catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


}
