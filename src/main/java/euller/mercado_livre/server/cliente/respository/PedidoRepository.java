package euller.mercado_livre.server.cliente.respository;

import com.google.gson.Gson;
import euller.mercado_livre.server.cliente.config.ratis.ClienteRatis;
import euller.mercado_livre.server.cliente.model.Pedido;
import euller.mercado_livre.server.cliente.model.Produto;
import euller.mercado_livre.server.cliente.service.mosquitto.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class PedidoRepository {
    private final Logger logger = Logger.getLogger(PedidoRepository.class.getName());
    private final Hashtable<String, List<Hashtable<String, List<String>>>> pedidos = new Hashtable<>();
    private MosquittoService mosquittoService = new MosquittoService();

    private final ClienteRatis clienteRatis = new ClienteRatis();


    public String criarPedido(Pedido pedidoModel) {
        logger.info("\nCriando pedido: "+pedidoModel+"\n");
        String CID = pedidoModel.getCID();
        String OID = pedidoModel.getOID();
        Gson gson = new Gson();
        Hashtable<String, List<String>> pedido = new Hashtable<>();
        List<String> produtos = new ArrayList<>();
        for (Produto produto : pedidoModel.getProdutos()) {
            produto.setCID(CID);
            produto.setOID(OID);
            String produtoJson = gson.toJson(produto);
            produtos.add(produtoJson);
        }
        pedido.put(OID, produtos);
        if(!pedidos.containsKey(CID)) {
            List<Hashtable<String, List<String>>> pedidos = new ArrayList<>();
            pedidos.add(pedido);
            this.pedidos.put(CID, pedidos);
        }else{
            this.pedidos.get(CID).add(pedido);
        }
        String pedidoBD = buscarPedidoHashTable(CID, OID);
        try {
            clienteRatis.cliente("add", UUID.randomUUID().toString(), pedidoBD);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return pedidoBD;
    }


    public String modificarPedido(Pedido pedidoModel) {
        logger.info("Modificando pedido: "+pedidoModel+"\n");
        String CID = pedidoModel.getCID();
        String OID = pedidoModel.getOID();
        if (pedidos.containsKey(CID) && pedidos.get(CID).size() > 0) {
            for (Hashtable<String, List<String>> pedido : pedidos.get(CID)) {
                if (pedido.containsKey(OID)) {
                    List<String> produtos = new ArrayList<>();
                    for (Produto produto : pedidoModel.getProdutos()) {
                        Gson gson = new Gson();
                        String produtoJson = gson.toJson(produto);
                        if(produto.getQuantidade() > 0){
                            produtos.add(produtoJson);
                        }
                    }
                    pedido.put(OID, produtos);
                    pedidos.get(CID).remove(pedido);
                    pedidos.get(CID).add(pedido);
                    String pedidoBD = buscarPedidoHashTable(CID, OID);
                    /*
                    try {
                        apagarPedido(CID, OID);
                        clienteRatis.cliente("add", UUID.randomUUID().toString(), pedidoBD);
                    } catch (IOException | ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                     */
                    return pedidoBD;
                }
            }
        }
        return null;
    }

    public String buscarPedidoHashTable(String CID, String OID){
        if(pedidos.containsKey(CID)) {
            for(Hashtable<String, List<String>> pedido : pedidos.get(CID)) {
                if(pedido.containsKey(OID)) {
                    List<String> produtosJson = pedido.get(OID);
                    List<Produto> produtos =  new ArrayList<>();
                    for (String produtoJson : produtosJson) {
                        Produto produto = new Gson().fromJson(produtoJson, Produto.class);
                        produtos.add(produto);
                    }
                    Pedido pedidoModel = new Pedido();
                    pedidoModel.setCID(CID);
                    pedidoModel.setOID(OID);
                    pedidoModel.setProdutos(produtos);
                    Gson gson = new Gson();
                    return gson.toJson(pedidoModel);
                }
            }
        }
        return null;
    }

    public String buscarPedido(String CID, String OID) {
        logger.info("Buscando pedido: "+"CID: "+CID+"OID: "+OID+"\n");
        try {
            return clienteRatis.cliente("get", CID, OID);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Hashtable<String,Integer>> buscarPedidos(String CID) {
        logger.info("Buscando pedidos:  "+"CID: "+CID+"\n");
        if (pedidos.containsKey(CID)) {
            List<Hashtable<String, List<String>>> pedidos = this.pedidos.get(CID);
            List<Hashtable<String,Integer>> precosTotaisPedidos = new ArrayList<>();
            for (Hashtable<String, List<String>> pedido : pedidos) {
                for (String OID : pedido.keySet()) {
                    List<String> produtos = pedido.get(OID);
                    int precoTotalProduto = 0;
                    for (String produto : produtos) {
                        Gson gson = new Gson();
                        Produto produtoModel = gson.fromJson(produto, Produto.class);
                        precoTotalProduto += produtoModel.getPreco();
                    }
                    Hashtable<String,Integer>precoTotalPedido = new Hashtable<>();
                    precoTotalPedido.put(OID,precoTotalProduto);
                    precosTotaisPedidos.add(precoTotalPedido);
                }
            }
            return precosTotaisPedidos;
        }
        return null;
    }

    public String apagarPedido(String CID, String OID) {
        logger.info("Apagando pedido: "+"CID: "+CID+"OID: "+OID+"\n");
        if (pedidos.containsKey(CID)) {
            for (Hashtable<String, List<String>> pedido : pedidos.get(CID)) {
                if (pedido.containsKey(OID)) {
                    pedido.remove(OID);
                    try {
                        clienteRatis.cliente("del", CID, OID);
                    } catch (IOException | InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    return "Pedido apagado";
                }
            }
        }
        return null;
    }


}
