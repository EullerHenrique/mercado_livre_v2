package euller.mercado_livre.client.admin.service;

import com.google.gson.Gson;
import euller.mercado_livre.client.admin.domain.model.Produto;
import euller.mercado_livre.server.admin.*;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProdutoService {

    private final Logger logger = Logger.getLogger(ProdutoService.class.getName());
    private final ProdutoServiceGrpc.ProdutoServiceBlockingStub blockingStubProduto;


    public ProdutoService(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStubProduto = ProdutoServiceGrpc.newBlockingStub(channel);
    }

    public void criarProduto(Produto produto) {
        Gson gson = new Gson();
        String produtoJson = gson.toJson(produto);
        logger.info("Request: Insira o produto " + produtoJson);
        CriarProdutoRequest request = CriarProdutoRequest.newBuilder().setDados(produtoJson).build();
        CriarProdutoResponse response;
        try {
            response = blockingStubProduto.criarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info(response.getMessage());
    }

    public void modificarProduto(String PID, Produto produto) {
        Gson gson = new Gson();
        String produtoJson = gson.toJson(produto);
        logger.info("Request: Modifique o produto com o PID: " + PID +" para " + produtoJson);
        ModificarProdutoRequest request = ModificarProdutoRequest.newBuilder().setPID(PID).setDados(produtoJson).build();
        ModificarProdutoResponse response;
        try {
            response = blockingStubProduto.modificarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
    }

    public void buscarProduto(String PID) {
        logger.info("Request: Busque o produto com o PID: " + PID);
        BuscarProdutoRequest request = BuscarProdutoRequest.newBuilder().setPID(PID).build();
        BuscarProdutoResponse response;
        try {
            response = blockingStubProduto.buscarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
    }

    public void apagarProduto(String PID) {
        logger.info("Request: Exclua o cliente com o PID: " + PID);
        ApagarProdutoRequest request = ApagarProdutoRequest.newBuilder().setPID(PID).build();
        ApagarProdutoResponse response;
        try {
            response = blockingStubProduto.apagarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
    }

}
