package euller.mercado_livre.client.admin.service;

import com.google.gson.Gson;
import euller.mercado_livre.client.admin.domain.dto.ProdutoDTO;
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

    public void criarProduto(ProdutoDTO produtoDTO) {
        Gson gson = new Gson();
        String produtoJson = gson.toJson(produtoDTO);
        logger.info("Request: Insira o produto " + produtoJson+"\n");
        CriarProdutoRequest request = CriarProdutoRequest.newBuilder().setDados(produtoJson).build();
        CriarProdutoResponse response;
        try {
            response = blockingStubProduto.criarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }

    public void modificarProduto(ProdutoDTO produtoDTO) {
        String PID = produtoDTO.getPID();
        Gson gson = new Gson();
        String produtoJson = gson.toJson(produtoDTO);
        logger.info("Request: Modifique o produto com o PID: " + PID +" para " + produtoJson+"\n");
        ModificarProdutoRequest request = ModificarProdutoRequest.newBuilder().setPID(PID).setDados(produtoJson).build();
        ModificarProdutoResponse response;
        try {
            response = blockingStubProduto.modificarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }

    public void buscarProduto(String PID) {
        logger.info("Request: Busque o produto com o PID: " + PID+"\n");
        BuscarProdutoRequest request = BuscarProdutoRequest.newBuilder().setPID(PID).build();
        BuscarProdutoResponse response;
        try {
            response = blockingStubProduto.buscarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }

    public void apagarProduto(String PID) {
        logger.info("Request: Exclua o cliente com o PID: " + PID+"\n");
        ApagarProdutoRequest request = ApagarProdutoRequest.newBuilder().setPID(PID).build();
        ApagarProdutoResponse response;
        try {
            response = blockingStubProduto.apagarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }

}
