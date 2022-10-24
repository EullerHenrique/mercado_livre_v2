package euller.mercado_livre.client.cliente;

import euller.mercado_livre.server.cliente.*;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteClient {
  private static final Logger logger = Logger.getLogger(ClienteClient.class.getName());

  private final PedidoServiceGrpc.PedidoServiceBlockingStub blockingStub;

  /**
   * Construct cliente for accessing HelloWorld server using the existing channel.
   */
  public ClienteClient(Channel channel) {
    // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
    // shut it down.

    // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
    blockingStub = PedidoServiceGrpc.newBlockingStub(channel);
  }

  /**
   * Greet server. If provided, the first element of {@code args} is the name to use in the
   * greeting. The second argument is the target server.
   */
  public static void main(String[] args) throws Exception {
    // Access a service running on the local machine on port 50051
    String target = "localhost:50051";
    // Create a communication channel to the server, known as a Channel. Channels are thread-safe
    // and reusable. It is common to create channels at the beginning of your application and reuse
    // them until the application shuts down.
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
            // needing certificates.
            .usePlaintext()
            .build();
    try {
      ClienteClient clienteClient = new ClienteClient(channel);
      clienteClient.criarPedido("CID12345", "PS1");
      clienteClient.criarPedido("CID12346", "PS2");
      clienteClient.criarPedido("CID12347", "PS3");
      clienteClient.criarPedido("CID12348", "PS4");
      clienteClient.criarPedido("CID12349", "PS5");
      clienteClient.listaPedido("CID12347");
    } finally {
      // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
      // resources the channel should be shut down when it will no longer be used. If it may be used
      // again leave it running.
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }

  public void criarPedido(String CID, String pedido) {
    logger.info("Insira o pedido" + CID + " " + pedido + " ...");
    CriarPedidoRequest request = CriarPedidoRequest.newBuilder().setCID(CID).setDados(pedido).build();
    CriarPedidoResponse response;
    try {
      response = blockingStub.criarPedido(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info("O pedido foi inserido " + response.getMessage());
  }

  public void listaPedido(String OID) {
    logger.info("Liste o pedido" + OID + " "+" ...");
    ListarPedidoRequest request = ListarPedidoRequest.newBuilder().setOID(OID).build();
    ListarPedidoResponse response;
    try {
      response = blockingStub.listarPedido(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info(response.getMessage());
  }

}

