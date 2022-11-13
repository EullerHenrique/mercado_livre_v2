package euller.mercado_livre.client.cliente.service;

import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.domain.model.Pedido;
import euller.mercado_livre.client.cliente.domain.model.Produto;
import euller.mercado_livre.client.cliente.service.external.ClienteService;
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

    public PedidoServiceGrpc.PedidoServiceBlockingStub getBlockingStubPedido() {
        return blockingStubPedido;
    }

    public void criarPedido(String CID, Produto produto, Pedido pedido) {
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
        logger.info(response.getMessage());
    }

    public void modificarPedido(String CID, String OID, Pedido pedido) {
        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedido);
        logger.info("Request: Modifique o pedido com o CID: " + CID + " e o OID: " + OID + " para " + pedidoJson);
        ModificarPedidoRequest request = ModificarPedidoRequest.newBuilder().setCID(CID).setOID(OID).setDados(pedidoJson).build();
        ModificarPedidoResponse response;
        try {
            response = blockingStubPedido.modificarPedido(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
    }

    public void listaPedido(String CID, String OID) {
        logger.info("Request: Busque o pedido com o CID: " + CID+ " e o OID: " + OID);
        ListarPedidoRequest request = ListarPedidoRequest.newBuilder().setCID(CID).setOID(OID).build();
        ListarPedidoResponse response;
        try {
            response = blockingStubPedido.listarPedido(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
    }

    public void listaPedidos(String CID) {
        logger.info("Request: Busque os pedido com o CID: " + CID);
        ListarPedidosRequest request = ListarPedidosRequest.newBuilder().setCID(CID).build();
        ListarPedidosResponse response;
        try {
            response = blockingStubPedido.listarPedidos(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
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
        logger.info(response.getMessage());
    }

}
