package euller.mercado_livre.client.cliente.service.external;

import euller.mercado_livre.client.cliente.ClienteClient;
import euller.mercado_livre.server.cliente.ClienteServiceGrpc;
import euller.mercado_livre.server.cliente.VerificarSeClienteExisteRequest;
import euller.mercado_livre.server.cliente.VerificarSeClienteExisteResponse;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteService {
    private final Logger logger = Logger.getLogger(ClienteService.class.getName());

    private ClienteServiceGrpc.ClienteServiceBlockingStub blockingStubCliente;

    public ClienteService(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStubCliente = ClienteServiceGrpc.newBlockingStub(channel);
    }

    public ClienteServiceGrpc.ClienteServiceBlockingStub getBlockingStubCliente() {
        return blockingStubCliente;
    }

    public boolean verificarSeClienteExiste(String cid) {
        logger.info("Request: Verifique se o cliente com o CID existe: " + cid);
        VerificarSeClienteExisteRequest request = VerificarSeClienteExisteRequest.newBuilder().setCID(cid).build();
        VerificarSeClienteExisteResponse response;
        try {
            response = blockingStubCliente.verificarSeClienteExiste(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return false;
        }
        logger.info(response.getMessage());
        return Boolean.parseBoolean(response.getMessage());
    }

}
