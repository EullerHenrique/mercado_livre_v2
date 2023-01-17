package euller.mercado_livre.server.cliente.respository;

import com.google.gson.Gson;
import euller.mercado_livre.ratis.ClientRatis;
import euller.mercado_livre.server.cliente.model.Pedido;
import euller.mercado_livre.server.cliente.model.Produto;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class PedidoRepository {
    private final Logger logger = Logger.getLogger(PedidoRepository.class.getName());
    private final Hashtable<String, List<Hashtable<String, List<String>>>> pedidos = new Hashtable<>();
    private final ClientRatis clientRatis = new ClientRatis();

    public void salvarPedidoNoCache(String CID, String OID, String pedidoJson){

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

        //Delete of cache after x seconds
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            System.out.println("A vida útil do cache se esgotou!");
            apagarPedido(CID, OID, false);
        };
        executor.schedule(task, 30, java.util.concurrent.TimeUnit.SECONDS);

    }

    public String criarPedido(Pedido pedidoModel) {
         logger.info("\nCriando pedido: "+pedidoModel+"\n");
         String CID = pedidoModel.getCID();
         String OID = pedidoModel.getOID();
         Gson gson = new Gson();

         Hashtable<String, List<Hashtable<String, List<String>>>> cidPedidos = new Hashtable<>();
         List<Hashtable<String, List<String>>> oidsPedidos = new ArrayList<>();
         Hashtable<String, List<String>> oidProdutos = new Hashtable<>();
         List<String> produtos = new ArrayList<>();

         for (Produto produto : pedidoModel.getProdutos()) {
             produto.setCID(CID);
             produto.setOID(OID);
             String produtoJson = gson.toJson(produto);
             produtos.add(produtoJson);
         }

         oidProdutos.put(OID, produtos);
         oidsPedidos.add(oidProdutos);
         cidPedidos.put(CID, oidsPedidos);

         String pedidoJson = cidPedidosToJson(CID, OID, cidPedidos);
         if(pedidoJson!=null) {
             try {
                 //Save on Database
                 clientRatis.exec("addPedido", UUID.randomUUID().toString(), pedidoJson);
                 System.out.println("Pedido salvo no database!");
             } catch (Exception e) {
                 System.out.println("Erro ao salvar o pedido no database: " + e.getMessage());
                 return null;
             }
         }
        return buscarPedido(CID, OID);
    }

    public String modificarPedido(Pedido pedidoModel) {
        logger.info("Modificando pedido: " + pedidoModel + "\n");

        String pedidosJson = buscarPedidosPeloCliente(pedidoModel.getCID());
        if (pedidosJson != null) {
            String CID = pedidoModel.getCID();
            String OID = pedidoModel.getOID();

            //cidPedidos: {CID: [{OID: [{PID: [Produto]}]}]}
            Gson gson = new Gson();
            String[] pedidosSplit = pedidosJson.split(";");

            Hashtable<String, List<Hashtable<String, List<String>>>> cidPedidos = new Hashtable<>();
            cidPedidos.put(CID, new ArrayList<>());

            for (String pedidoString : pedidosSplit) {
                Pedido pM = gson.fromJson(pedidoString, Pedido.class);
                Hashtable<String, List<String>> oidProdutos = new Hashtable<>();
                List<String> produtos = new ArrayList<>();
                for (Produto produto : pM.getProdutos()) {
                    produtos.add(gson.toJson(produto));
                }
                oidProdutos.put(pM.getOID(), produtos);
                cidPedidos.get(pM.getCID()).add(oidProdutos);
            }
            //

            List<Hashtable<String, List<String>>> oidsPedidos = cidPedidos.get(CID);
            for (Hashtable<String, List<String>> oidProdutos: oidsPedidos) {
                if (oidProdutos.containsKey(OID)) {
                    List<String> produtos = new ArrayList<>();
                    for (Produto produto : pedidoModel.getProdutos()) {
                        String produtoJson = gson.toJson(produto);
                        if (produto.getQuantidade() > 0) {
                            produtos.add(produtoJson);
                        }
                    }
                    oidProdutos.put(OID, produtos);
                    cidPedidos.get(CID).remove(oidProdutos);
                    cidPedidos.get(CID).add(oidProdutos);

                    String pedidoJson = cidPedidosToJson(CID, OID, cidPedidos);
                    if(pedidoJson != null) {
                        apagarPedido(CID, OID, true);
                        return criarPedido(gson.fromJson(pedidoJson, Pedido.class));
                    }
                }
            }
        }
        return null;
    }
    public String buscarPedido(String CID, String OID) {
        logger.info("Buscando pedido: " + "CID: " + CID + "OID: " + OID + "\n");

        //Get Of Cache
        if(pedidos.containsKey(CID) && !pedidos.get(CID).isEmpty()) {
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
                }
            }
        }
        System.out.println("Pedido não encontrado no cache");
        //Get Of Database
        try {
            String pedidoJson = clientRatis.exec("getPedido", CID, OID);
            if (pedidoJson != null) {
                System.out.println("Pedido encontrado no database!");

                //Save On Cache and delete after 1 minute
                salvarPedidoNoCache(CID, OID, pedidoJson);

            } else {
                System.out.println("Pedido não encontrado no database!");
            }
            return pedidoJson;
        } catch (Exception e) {
            System.out.println("Erro ao buscar o pedido no database: " + e.getMessage());
        }
        return null;
    }

    public List<Hashtable<String,Integer>> buscarPedidos(String CID) {
        logger.info("Buscando pedidos:  " + "CID: " + CID + "\n");

        //Get Of Cache
        if (pedidos.containsKey(CID) && !pedidos.get(CID).isEmpty()) {
            List<Hashtable<String, List<String>>> oidsPedidos = this.pedidos.get(CID);
            List<Hashtable<String,Integer>> precosTotaisPedidos = new ArrayList<>();
            for (Hashtable<String, List<String>> oidProdutos : oidsPedidos) {
                for (String OID : oidProdutos.keySet()) {
                    List<String> produtos = oidProdutos.get(OID);
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
            if (!precosTotaisPedidos.isEmpty()) {
                System.out.println("Pedidos encontrados no cache");
                return precosTotaisPedidos;
            }
        }
        System.out.println("Pedidos não encontrados no cache");
        //Get of Database
        String pedidosString = buscarPedidosPeloCliente(CID);
        if (pedidosString != null) {
            //cidPedidos: {CID: [{OID: [{PID: [Produto]}]}]}
            String[] pedidosSplit = pedidosString.split(";");
            Hashtable<String, List<Hashtable<String, List<String>>>> cidPedidos = new Hashtable<>();
            cidPedidos.put(CID, new ArrayList<>());
            Gson gson = new Gson();
            for (String pedidoString : pedidosSplit) {
                Pedido pM = gson.fromJson(pedidoString, Pedido.class);
                Hashtable<String, List<String>> oidProdutos = new Hashtable<>();
                List<String> produtos = new ArrayList<>();
                for (Produto produto : pM.getProdutos()) {
                    produtos.add(gson.toJson(produto));
                }
                oidProdutos.put(pM.getOID(), produtos);
                cidPedidos.get(pM.getCID()).add(oidProdutos);
            }
            //
            List<Hashtable<String, List<String>>> oidsPedidos = cidPedidos.get(CID);
            List<Hashtable<String, Integer>> precosTotaisPedidos = new ArrayList<>();
            if(oidsPedidos != null && !oidsPedidos.isEmpty()){
                for (Hashtable<String, List<String>> oidPedido : oidsPedidos) {
                    for (String OID : oidPedido.keySet()) {
                        List<String> produtos = oidPedido.get(OID);
                        int precoTotalProduto = 0;
                        for (String produto : produtos) {
                            Produto produtoModel = gson.fromJson(produto, Produto.class);
                            precoTotalProduto += produtoModel.getPreco();
                        }
                        Hashtable<String, Integer> precoTotalPedido = new Hashtable<>();
                        precoTotalPedido.put(OID, precoTotalProduto);
                        precosTotaisPedidos.add(precoTotalPedido);
                        String pedidoJson = cidPedidosToJson(CID, OID, cidPedidos);
                        if (pedidoJson != null) {
                            salvarPedidoNoCache(CID, OID, pedidoJson);
                        }
                    }
                }
            }
            if (!precosTotaisPedidos.isEmpty()) {
                System.out.println("Pedidos encontrados no database");
                return precosTotaisPedidos;
            }
        }
        return null;
    }

    public String apagarPedido(String CID, String OID, boolean deleteOfDatabase) {
        logger.info("Apagando pedido: " + "CID: " + CID + " OID: " + OID + "\n");

        boolean isDeleteCache = false;
        boolean isDeleteDatabase = false;

        //Delete Of Cache
        if (pedidos.containsKey(CID) && !pedidos.get(CID).isEmpty()) {
            for (Hashtable<String, List<String>> pedidoCache : pedidos.get(CID)) {
                if (pedidoCache.containsKey(OID)) {
                    pedidoCache.remove(OID);
                    isDeleteCache = true;
                    System.out.println("Pedido apagado do cache");
                }
            }
        }

        if (deleteOfDatabase) {
            //Delete Of Database
            try {
                if(clientRatis.exec("delPedido", CID, OID) != null) {
                    System.out.println("Pedido apagado do database!");
                    isDeleteDatabase = true;

                }
            } catch (Exception e) {
                logger.info("Erro ao apagar o pedido do database: " + e.getMessage() + "\n");
            }
        }

        if(isDeleteCache || isDeleteDatabase) {
            return "Pedido apagado";
        }

        return null;
    }


    public String buscarPedidosPeloCliente(String CID) {
        try {
            return clientRatis.exec("getPedido", CID, "cliente");
        } catch (Exception e) {
            System.out.println("Erro ao buscar os pedidos do database: " + e.getMessage() + "\n");
            return null;
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

}
