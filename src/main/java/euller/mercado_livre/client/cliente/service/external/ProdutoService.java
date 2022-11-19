package euller.mercado_livre.client.cliente.service.external;

import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.model.ProdutoDTO;
import euller.mercado_livre.server.cliente.*;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProdutoService {

    private final Logger logger = Logger.getLogger(ProdutoService.class.getName());

    private ProdutoServiceGrpc. ProdutoServiceBlockingStub blockingStubProduto;

    public ProdutoService(Channel channel) {
        blockingStubProduto = ProdutoServiceGrpc.newBlockingStub(channel);
    }

    public String buscarProduto(String pid) {
        logger.info("Request: Busque o produto com o PID: " + pid);
        BuscarProdutoRequest request = BuscarProdutoRequest.newBuilder().setPID(pid).build();
        BuscarProdutoResponse response;
        try {
            response = blockingStubProduto.buscarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null;
        }
        logger.info("Response: "+response.getMessage());
        return response.getMessage();
    }

    public void modificarProduto(ProdutoDTO produtoDTO) {
        logger.info("Request: Modifique o produto com o PID: " + produtoDTO.getPID());
        Gson gson = new Gson();
        String produtoJson = gson.toJson(produtoDTO);
        ModificarProdutoRequest request = ModificarProdutoRequest.newBuilder().setPID(produtoDTO.getPID()).setDados(produtoJson).build();
        ModificarProdutoResponse response;
        try {
            response = blockingStubProduto.modificarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Response: "+response.getMessage());
    }
}
