package euller.mercado_livre.client.cliente.service;

import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.model.Pedido;
import euller.mercado_livre.client.cliente.model.Produto;
import euller.mercado_livre.client.cliente.service.external.ProdutoService;
import euller.mercado_livre.server.cliente.*;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PedidoService {

    private final Logger logger = Logger.getLogger(ProdutoService.class.getName());
    private PedidoServiceGrpc.PedidoServiceBlockingStub blockingStubPedido;
    private ProdutoService produtoService;

    public PedidoService(ProdutoService produtoService, Channel channel) {
        this.produtoService = produtoService;
        blockingStubPedido = PedidoServiceGrpc.newBlockingStub(channel);
    }

    public void criarPedido(Produto produto, Pedido pedido) {
        String CID = pedido.getCID();
        logger.info("Request: Insira o pedido " + pedido + " com o CID " + CID);
        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedido);
        CriarPedidoRequest request = CriarPedidoRequest.newBuilder().setCID(CID).setDados(pedidoJson).build();
        CriarPedidoResponse response;
        try {
            response = blockingStubPedido.criarPedido(request);
            int quantidadeProdutoAtualizada =  produto.getQuantidade() - pedido.getQuantidade();
            produto.setQuantidade(quantidadeProdutoAtualizada);
            produtoService.modificarProduto(produto);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Response: "+response.getMessage());
    }
    public void modificarPedido(Pedido pedidoAntigo, Pedido pedidoNovo, Produto produto) {
        String CID = pedidoAntigo.getCID();
        String OID = pedidoAntigo.getOID();
        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedidoNovo);
        logger.info("Request: Modifique o pedido com o CID: " + CID + " e o OID: " + OID + " para " + pedidoJson);
        ModificarPedidoRequest request = ModificarPedidoRequest.newBuilder().setCID(CID).setOID(OID).setDados(pedidoJson).build();
        ModificarPedidoResponse response;
        try {
            response = blockingStubPedido.modificarPedido(request);
            int quantidadeProdutoAtualizada;
            if(pedidoAntigo.getQuantidade() > pedidoNovo.getQuantidade()) {
                quantidadeProdutoAtualizada = produto.getQuantidade() + (pedidoAntigo.getQuantidade() - pedidoNovo.getQuantidade());
            } else {
                quantidadeProdutoAtualizada = produto.getQuantidade() - (pedidoNovo.getQuantidade() - pedidoAntigo.getQuantidade());
            }
            produto.setQuantidade(quantidadeProdutoAtualizada);
            produtoService.modificarProduto(produto);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info("Response: "+response.getMessage());
    }
    public String buscarPedido(String CID, String OID) {
        logger.info("Request: Busque o pedido com o CID: " + CID+ " e o OID: " + OID);
        BuscarPedidoRequest request = BuscarPedidoRequest.newBuilder().setCID(CID).setOID(OID).build();
        BuscarPedidoResponse response;
        try {
            response = blockingStubPedido.buscarPedido(request);
            logger.info("Response: "+response.getMessage());
            return response.getMessage();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void buscarPedidos(String CID) {
        logger.info("Request: Busque os pedido com o CID: " + CID);
        BuscarPedidosRequest request = BuscarPedidosRequest.newBuilder().setCID(CID).build();
        BuscarPedidosResponse response;
        try {
            response = blockingStubPedido.buscarPedidos(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info("Response: "+response.getMessage());
    }
    public void excluirPedido(String CID, String OID) {
        logger.info("Request: Exclua o pedido com o CID: " + CID+ " e o OID: " + OID);
        ApagarPedidoRequest request = ApagarPedidoRequest.newBuilder().setCID(CID).setOID(OID).build();
        ApagarPedidoResponse response;
        try {
            response = blockingStubPedido.apagarPedido(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info("Response: "+response.getMessage());
    }
}
