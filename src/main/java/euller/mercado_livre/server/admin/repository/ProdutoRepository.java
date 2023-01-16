package euller.mercado_livre.server.admin.repository;

import com.google.gson.Gson;
import euller.mercado_livre.ratis.ClientRatis;
import euller.mercado_livre.server.admin.model.Produto;
import euller.mercado_livre.server.admin.service.mosquitto.MosquittoService;
import java.util.Hashtable;
import java.util.logging.Logger;

public class ProdutoRepository {

    private final Logger logger = Logger.getLogger(ProdutoRepository.class.getName());
    private final Hashtable<String, String> produtos = new Hashtable<>();
    private final ClientRatis clientRatis = new ClientRatis();

    private final MosquittoService mosquittoService = new MosquittoService();

    public String criarProduto(Produto produto) {
        logger.info("Criando produto: "+produto+"\n");
        String PID = produto.getPID();
        try {
            if (buscarProduto(PID) == null) {
                Gson gson = new Gson();
                String produtoJson = gson.toJson(produto);
                clientRatis.exec("addProduto", PID, produtoJson);
                System.out.println("Produto salvo no database");
                return produtoJson;
            }
        } catch (Exception e) {
            logger.info("Erro ao buscar o produto no database: "+e.getMessage()+"\n");
        }
        return null;
    }

    public String modificarProduto(Produto produto) {
        logger.info("Modificando produto: "+produto+"\n");
        String PID = produto.getPID();
        if(buscarProduto(PID) != null){
            if(apagarProduto(PID, false) != null){
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
        //Get Of Cache
        if(produtos.containsKey(PID)) {
            System.out.println("Produto encontrado no cache");
            return produtos.get(PID);
        }else {
            System.out.println("Produto não encontrado no cache");
            //Get Of Database
            try {
                String produtoJson = clientRatis.exec("getProduto", PID, null);
                if(produtoJson != null){
                    System.out.println("Produto encontrado no database");
                    //Save On Cache
                    produtos.put(PID, produtoJson);
                    System.out.println("Produto salvo no cache");
                }else {
                    System.out.println("Produto não encontrado no database");
                }
                return produtoJson;
            } catch (Exception e) {
                logger.info("Erro ao buscar o produto no database: "+e.getMessage()+"\n");
                return null;
            }
        }
    }

    public String apagarProduto(String PID, boolean otherServerUpdate) {
        logger.info("Apagando produto: "+PID+"\n");

        boolean isDeleteCache = false;
        boolean isDeleteDatabase = false;

        //Delete Of Cache
        if(produtos.containsKey(PID)) {
            produtos.remove(PID);
            System.out.println("Produto apagado do cache");
            isDeleteCache = true;
        }

        if(!otherServerUpdate) {
            //Delete of Database
            try {
                if (clientRatis.exec("delProduto", PID, null) != null) {
                    System.out.println("Produto apagado do database");
                    isDeleteDatabase = true;

                    //Send Message for the others servers: Delete of Cache
                    try {
                        mosquittoService.publish("server/admin/produto/apagar", PID);
                        System.out.println("Mensagem 'Delete produto of cache' enviada para os outros servidores");
                    } catch (Exception e) {
                        logger.info("Erro ao solicitar que os outros servidores apaguem o produto do cache " + e.getMessage() + "\n");
                    }
                }
            } catch (Exception e) {
                logger.info("Erro ao apagar o produto do database: " + e.getMessage() + "\n");
            }
        }else{
            if(isDeleteCache) {
                System.out.println("O servidor recebeu a mensagem e apagou o produto do cache");
            }
        }

        if(isDeleteCache || isDeleteDatabase) {
            return "Produto apagado";
        }

        return null;
    }

}
