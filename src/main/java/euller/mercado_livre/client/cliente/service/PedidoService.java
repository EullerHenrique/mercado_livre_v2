package euller.mercado_livre.client.cliente.service;

import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.domain.dto.PedidoDTO;
import euller.mercado_livre.client.cliente.domain.dto.ProdutoDTO;
import euller.mercado_livre.client.cliente.service.external.ProdutoService;
import euller.mercado_livre.server.cliente.*;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

import java.util.List;
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

    public void criarPedido(List<ProdutoDTO> produtosDTO, PedidoDTO pedidoDTO) {
        String CID = pedidoDTO.getCID();
        logger.info("Request: Insira o pedido " + pedidoDTO + " com o CID " + CID+"\n");
        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedidoDTO);
        CriarPedidoRequest request = CriarPedidoRequest.newBuilder().setCID(CID).setDados(pedidoJson).build();
        CriarPedidoResponse response;
        try {
            response = blockingStubPedido.criarPedido(request);
            List<ProdutoDTO> produtosPedidoDTO = pedidoDTO.getProdutos();
            for(int i=0; i < produtosPedidoDTO.size(); i++){
                int quantidadeProdutoAtualizada = produtosDTO.get(i).getQuantidade() - produtosPedidoDTO.get(i).getQuantidade();
                produtosDTO.get(i).setQuantidade(quantidadeProdutoAtualizada);
                produtoService.modificarProduto(produtosDTO.get(i));
            }
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }

    public void modificarPedido(PedidoDTO pedidoDTOAntigo, PedidoDTO pedidoDTONovo, ProdutoDTO produtoDTO) {
        String CID = pedidoDTOAntigo.getCID();
        String OID = pedidoDTOAntigo.getOID();
        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedidoDTONovo);
        logger.info("Request: Modifique o pedido com o CID: " + CID + " e o OID: " + OID + " para " + pedidoJson+"\n");
        ModificarPedidoRequest request = ModificarPedidoRequest.newBuilder().setCID(CID).setOID(OID).setDados(pedidoJson).build();
        ModificarPedidoResponse response;
        try {
            response = blockingStubPedido.modificarPedido(request);
            ProdutoDTO produtoDTOPedidoAntigo = pedidoDTOAntigo.getProdutos().stream().filter(p -> p.getPID().equals(produtoDTO.getPID())).findFirst().get();
            ProdutoDTO produtoDTOPedidoNovo = pedidoDTONovo.getProdutos().stream().filter(p -> p.getPID().equals(produtoDTO.getPID())).findFirst().get();

            int quantidadeProdutoAtualizada;
            if(produtoDTOPedidoAntigo.getQuantidade() > produtoDTOPedidoNovo.getQuantidade()) {
                quantidadeProdutoAtualizada = produtoDTO.getQuantidade() + (produtoDTOPedidoAntigo.getQuantidade() - produtoDTOPedidoNovo.getQuantidade());
            } else {
                quantidadeProdutoAtualizada = produtoDTO.getQuantidade() - (produtoDTOPedidoNovo.getQuantidade() - produtoDTOPedidoAntigo.getQuantidade());
            }
            produtoDTO.setQuantidade(quantidadeProdutoAtualizada);
            produtoService.modificarProduto(produtoDTO);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }

    public String buscarPedido(String CID, String OID) {
        logger.info("Request: Busque o pedido com o CID: " + CID+ " e o OID: " + OID+"\n");
        BuscarPedidoRequest request = BuscarPedidoRequest.newBuilder().setCID(CID).setOID(OID).build();
        BuscarPedidoResponse response;
        try {
            response = blockingStubPedido.buscarPedido(request);
            logger.info("Response: "+response.getMessage());
            return response.getMessage();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage()+"\n");
        }
        return null;
    }
    public void buscarPedidos(String CID) {
        logger.info("Request: Busque os pedido com o CID: " + CID+"\n");
        BuscarPedidosRequest request = BuscarPedidosRequest.newBuilder().setCID(CID).build();
        BuscarPedidosResponse response;
        try {
            response = blockingStubPedido.buscarPedidos(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }
    public void excluirPedido(String CID, String OID) {
        logger.info("Request: Exclua o pedido com o CID: " + CID+ " e o OID: " + OID+"\n");
        ApagarPedidoRequest request = ApagarPedidoRequest.newBuilder().setCID(CID).setOID(OID).build();
        ApagarPedidoResponse response;
        try {
            response = blockingStubPedido.apagarPedido(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }
}
