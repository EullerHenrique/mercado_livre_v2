package euller.mercado_livre.client.admin.service;

import com.google.gson.Gson;
import euller.mercado_livre.client.admin.AdminClient;
import euller.mercado_livre.client.admin.domain.model.Cliente;
import euller.mercado_livre.server.admin.*;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteService {

    private final Logger logger = Logger.getLogger(ClienteService.class.getName());

    private final ClienteServiceGrpc.ClienteServiceBlockingStub blockingStubCliente;

    public ClienteService(Channel channel) {
        blockingStubCliente = ClienteServiceGrpc.newBlockingStub(channel);
    }

    public void criarCliente(Cliente cliente) {
        Gson gson = new Gson();
        String clienteJson = gson.toJson(cliente);
        logger.info("Request: Insira o cliente " + clienteJson);
        String CID = UUID.randomUUID().toString();
        CriarClienteRequest request = CriarClienteRequest.newBuilder().setDados(clienteJson).build();
        CriarClienteResponse response;
        try {
            response = blockingStubCliente.criarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info(response.getMessage());
    }

    public void modificarCliente(String CID, Cliente cliente) {
        Gson gson = new Gson();
        String clienteJson = gson.toJson(cliente);
        logger.info("Request: Modifique o cliente com o CID: " + CID +" para " + cliente);
        ModificarClienteRequest request = ModificarClienteRequest.newBuilder().setCID(CID).setDados(clienteJson).build();
        ModificarClienteResponse response;
        try {
            response = blockingStubCliente.modificarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
    }

    public void buscarCliente(String CID) {
        logger.info("Request: Busque o cliente com o CID: " + CID);
        BuscarClienteRequest request = BuscarClienteRequest.newBuilder().setCID(CID).build();
        BuscarClienteResponse response;
        try {
            response = blockingStubCliente.buscarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
    }

    public void apagarCliente(String CID) {
        logger.info("Request: Exclua o cliente com o CID: " + CID);
        ApagarClienteRequest request = ApagarClienteRequest.newBuilder().setCID(CID).build();
        ApagarClienteResponse response;
        try {
            response = blockingStubCliente.apagarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
    }


}
