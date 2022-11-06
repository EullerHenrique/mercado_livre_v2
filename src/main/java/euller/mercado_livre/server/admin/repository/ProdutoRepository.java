package euller.mercado_livre.server.admin.repository;


import java.util.Hashtable;
import java.util.UUID;

public class ProdutoRepository {
    private Hashtable<String, String> produtos = new Hashtable<>();

    public String criarProduto(String dados) {
        String PID = UUID.randomUUID().toString();
        produtos.put(PID, dados);
        return "PID: " + PID + " Produto: "+ buscarProduto(PID);
    }

    public String modificarProduto(String PID, String dados){
        if(produtos.containsKey(PID)){
            produtos.remove(PID);
            produtos.put(PID, dados);
            return buscarProduto(PID);
        }
        return " Produto não encontrado";
    }

    public String buscarProduto(String PID){
        if(produtos.containsKey(PID)) {
            return produtos.get(PID);
        }
        return " Produto não encontrado";
    }

    public String isProduto(String PID){
        if(produtos.containsKey(PID)) {
            return "true";
        }
        return "false";
    }

    public String apagarProduto(String PID){
        if (produtos.containsKey(PID)) {
            produtos.remove(PID);
            return " Produto apagado";
        }
        return " Produto não encontrado";
    }
}
