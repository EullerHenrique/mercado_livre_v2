package euller.mercado_livre.client.admin.service;

import com.google.gson.Gson;
import euller.mercado_livre.client.admin.domain.dto.ClienteDTO;
import euller.mercado_livre.server.admin.*;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteService {

    private final Logger logger = Logger.getLogger(ClienteService.class.getName());

    private final ClienteServiceGrpc.ClienteServiceBlockingStub blockingStubCliente;

    public ClienteService(Channel channel) {
        blockingStubCliente = ClienteServiceGrpc.newBlockingStub(channel);
    }

    public void criarCliente(ClienteDTO clienteDTO) {
        Gson gson = new Gson();
        String clienteJson = gson.toJson(clienteDTO);
        logger.info("Request: Insira o cliente " + clienteJson+"\n");
        CriarClienteRequest request = CriarClienteRequest.newBuilder().setDados(clienteJson).build();
        CriarClienteResponse response;
        try {
            response = blockingStubCliente.criarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }

    public void modificarCliente(ClienteDTO clienteDTO) {
        String CID = clienteDTO.getCID();
        Gson gson = new Gson();
        String clienteJson = gson.toJson(clienteDTO);
        logger.info("Request: Modifique o cliente com o CID: " + CID +" para " + clienteJson+"\n");
        ModificarClienteRequest request = ModificarClienteRequest.newBuilder().setCID(CID).setDados(clienteJson).build();
        ModificarClienteResponse response;
        try {
            response = blockingStubCliente.modificarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }

    public void buscarCliente(String CID) {
        logger.info("Request: Busque o cliente com o CID: " + CID+"\n");
        BuscarClienteRequest request = BuscarClienteRequest.newBuilder().setCID(CID).build();
        BuscarClienteResponse response;
        try {
            response = blockingStubCliente.buscarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }

    public void apagarCliente(String CID) {
        logger.info("Request: Exclua o cliente com o CID: " + CID+"\n");
        ApagarClienteRequest request = ApagarClienteRequest.newBuilder().setCID(CID).build();
        ApagarClienteResponse response;
        try {
            response = blockingStubCliente.apagarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Response: "+response.getMessage()+"\n");
    }


}
