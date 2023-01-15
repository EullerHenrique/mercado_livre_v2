package euller.mercado_livre.server.cliente.respository;

import com.google.gson.Gson;
import euller.mercado_livre.ratis.ClientRatis;
import euller.mercado_livre.server.cliente.model.Pedido;
import euller.mercado_livre.server.cliente.model.Produto;
import euller.mercado_livre.server.cliente.service.mosquitto.MosquittoService;

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
    private final ClientRatis clientRatis = new ClientRatis();
    private final MosquittoService mosquittoService = new MosquittoService();


    public String criarPedido(Pedido pedidoModel) {
        try {
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
            Hashtable<String, List<Hashtable<String, List<String>>>> cidPedidos = new Hashtable<>();
            List<Hashtable<String, List<String>>> oidPedidos = new ArrayList<>();
            oidPedidos.add(pedido);
            cidPedidos.put(CID, oidPedidos);
            String pedidoBD = cidPedidosToJson(CID, OID, cidPedidos);
            clientRatis.exec("add", UUID.randomUUID().toString(), pedidoBD);
            System.out.println("Pedido salvo no database!");
            return pedidoBD;
        } catch (Exception e) {
            System.out.println("Erro ao salvar o pedido no database: " + e.getMessage());
            return null;
        }
    }


    public String modificarPedido(Pedido pedidoModel) {
        logger.info("Modificando pedido: " + pedidoModel + "\n");
        String pedidos = buscarPedidosPeloCliente(pedidoModel.getCID());
        if (pedidos != null) {
            String CID = pedidoModel.getCID();
            String OID = pedidoModel.getOID();

            //cidPedidos: {CID: [{OID: [{PID: [Produto]}]}]}
            Gson gson = new Gson();
            String[] pedidosSplit = pedidos.split(";");
            Hashtable<String, List<Hashtable<String, List<String>>>> cidPedidos = new Hashtable<>();
            cidPedidos.put(CID, new ArrayList<>());
            for (String pedidoString : pedidosSplit) {
                Pedido pM = gson.fromJson(pedidoString, Pedido.class);
                if(pM.getOID()!=null) {
                    Hashtable<String, List<String>> oidPedido = new Hashtable<>();
                    List<String> produtos = new ArrayList<>();
                    for (Produto produto : pM.getProdutos()) {
                        produtos.add(gson.toJson(produto));
                    }
                    oidPedido.put(pM.getOID(), produtos);
                    cidPedidos.get(pM.getCID()).add(oidPedido);
                }

            }
            //

            for (Hashtable<String, List<String>> pedido : cidPedidos.get(CID)) {
                if (pedido.containsKey(OID)) {
                    List<String> produtos = new ArrayList<>();
                    for (Produto produto : pedidoModel.getProdutos()) {
                        String produtoJson = gson.toJson(produto);
                        if (produto.getQuantidade() > 0) {
                            produtos.add(produtoJson);
                        }
                    }
                    pedido.put(OID, produtos);
                    cidPedidos.get(CID).remove(pedido);
                    cidPedidos.get(CID).add(pedido);

                    String pedidoBD = cidPedidosToJson(CID, OID, cidPedidos);
                    apagarPedido(CID, OID, false);
                    criarPedido(gson.fromJson(pedidoBD, Pedido.class));
                    return pedidoBD;
                }
            }
        }
        return null;
    }

    public String buscarPedido(String CID, String OID) {
        logger.info("Buscando pedido: " + "CID: " + CID + "OID: " + OID + "\n");

        //Get Of Cache
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
                    System.out.println("Pedido encontrado no cache");
                    return gson.toJson(pedidoModel);
                }else{
                    System.out.println("Pedido não encontrado no cache");
                }
            }
        }else {
            System.out.println("Pedido não encontrado no cache");
            //Get Of Database
            try {
                String pedidoJson = clientRatis.exec("getClient", CID, OID);
                if (pedidoJson != null) {
                    System.out.println("Pedido encontrado no database!");
                    //Save On Cache
                    Pedido pedidoModel = new Gson().fromJson(pedidoJson, Pedido.class);
                    Hashtable<String, List<String>> pedido = new Hashtable<>();
                    List<String> produtos = new ArrayList<>();
                    for (Produto produto : pedidoModel.getProdutos()) {
                        produtos.add(new Gson().toJson(produto));
                    }
                    pedido.put(OID, produtos);
                    if (!pedidos.containsKey(CID)) {
                        List<Hashtable<String, List<String>>> pedidos = new ArrayList<>();
                        pedidos.add(pedido);
                        this.pedidos.put(CID, pedidos);
                    } else {
                        this.pedidos.get(CID).add(pedido);
                    }
                    System.out.println("Pedido salvo no cache!");
                } else {
                    System.out.println("Pedido não encontrado no database!");
                }
                return pedidoJson;
            } catch (Exception e) {
                System.out.println("Erro o buscar o pedido no database: " + e.getMessage());
            }
        }
        return null;
    }


    public List<Hashtable<String,Integer>> buscarPedidos(String CID) {
        logger.info("Buscando pedidos:  " + "CID: " + CID + "\n");

        String pedidosString = buscarPedidosPeloCliente(CID);
        if (pedidosString != null) {

            //cidPedidos: {CID: [{OID: [{PID: [Produto]}]}]}
            Gson gson = new Gson();
            String[] pedidosSplit = pedidosString.split(";");
            Hashtable<String, List<Hashtable<String, List<String>>>> cidPedidos = new Hashtable<>();
            cidPedidos.put(CID, new ArrayList<>());
            for (String pedidoString : pedidosSplit) {
                Pedido pM = gson.fromJson(pedidoString, Pedido.class);
                if(pM.getOID()!=null) {
                    Hashtable<String, List<String>> oidPedido = new Hashtable<>();
                    List<String> produtos = new ArrayList<>();
                    for (Produto produto : pM.getProdutos()) {
                        produtos.add(gson.toJson(produto));
                    }
                    oidPedido.put(pM.getOID(), produtos);
                    cidPedidos.get(pM.getCID()).add(oidPedido);
                }
            }
            //

            List<Hashtable<String, List<String>>> pedidos = cidPedidos.get(CID);
            List<Hashtable<String,Integer>> precosTotaisPedidos = new ArrayList<>();
            for (Hashtable<String, List<String>> pedido : pedidos) {
                for (String OID : pedido.keySet()) {
                    List<String> produtos = pedido.get(OID);
                    int precoTotalProduto = 0;
                    for (String produto : produtos) {
                        Produto produtoModel = gson.fromJson(produto, Produto.class);
                        precoTotalProduto += produtoModel.getPreco();
                    }
                    Hashtable<String,Integer>precoTotalPedido = new Hashtable<>();
                    precoTotalPedido.put(OID,precoTotalProduto);
                    precosTotaisPedidos.add(precoTotalPedido);
                }
            }
            if(precosTotaisPedidos.isEmpty()) {
                return null;
            }
            return precosTotaisPedidos;
        }
        return null;
    }

    public String apagarPedido(String CID, String OID, boolean otherServerUpdate) {
        logger.info("Apagando pedido: " + "CID: " + CID + "OID: " + OID + "\n");

        boolean isDeleteCache = false;
        boolean isDeleteDatabase = false;

        //Delete Of Cache
        if (pedidos.containsKey(CID)) {
            for (Hashtable<String, List<String>> pedidoCache : pedidos.get(CID)) {
                if (pedidoCache.containsKey(OID)) {
                    pedidoCache.remove(OID);
                    isDeleteCache = true;
                    System.out.println("Pedido apagado do cache");
                }
            }
        }
        if (!otherServerUpdate) {
            //Delete Of Database
            try {
                if(clientRatis.exec("delClient", CID, OID) != null) {
                    System.out.println("Pedido apagado do database!");
                    isDeleteDatabase = true;

                    //Send Message for the others servers: Delete of Cache
                    try {
                        Pedido pedido = new Pedido();
                        pedido.setCID(CID);
                        pedido.setOID(OID);
                        Gson gson = new Gson();
                        String pedidoJson = gson.toJson(pedido);
                        mosquittoService.publish("server/cliente/pedido/apagar", pedidoJson);
                        System.out.println("Mensagem 'Delete pedido of cache' enviada para os outros servidores");
                    } catch (Exception e) {
                        logger.info("Erro ao solicitar que os outros servidores apaguem o pedido do cache " + e.getMessage() + "\n");
                    }
                }
            } catch (Exception e) {
                logger.info("Erro ao apagar o pedido do database: " + e.getMessage() + "\n");
            }
        }else{
            if(isDeleteCache) {
                System.out.println("O servidor recebeu a mensagem e apagou o pedido do cache");
            }
        }

        if(isDeleteCache || isDeleteDatabase) {
            return "Pedido apagado";
        }

        return null;
    }



    public String buscarPedidosPeloCliente(String CID) {
        try {
            return clientRatis.exec("getClient", CID, "cliente");
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    public String cidPedidosToJson(String CID, String OID,  Hashtable<String, List<Hashtable<String, List<String>>>> cidPedidos){
        if(cidPedidos.containsKey(CID)) {
            for(Hashtable<String, List<String>> pedido : cidPedidos.get(CID)) {
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



    //Criar Pedido -> Feito
    //Modificar Pedido -> Fazer buscar pedidos pelo cliente, Feito -> ?
    //Buscar Pedido -> Feito
    //Buscar Pedidos -> Fazer buscar pedidos pelo cliente -> Feito -> ?
    //Apagar Pedido -> Feito

}
