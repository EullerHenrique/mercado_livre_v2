package euller.mercado_livre.client.cliente;
import euller.mercado_livre.client.cliente.domain.model.Pedido;
import euller.mercado_livre.client.cliente.domain.model.Produto;
import euller.mercado_livre.client.cliente.service.InputsService;
import euller.mercado_livre.client.cliente.service.PedidoService;
import euller.mercado_livre.client.cliente.service.external.ClienteService;
import euller.mercado_livre.client.cliente.service.external.ProdutoService;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClienteClient {
  private static InputsService inputsService;
  private static PedidoService pedidoService;

  public ClienteClient(Channel channel) {
    ClienteService clienteService = new ClienteService(channel);
    ProdutoService produtoService = new ProdutoService(channel);
    pedidoService = new PedidoService(produtoService, channel);
    inputsService = new InputsService(clienteService, produtoService);
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
      inputsService.exibePortal();
      Scanner scanner = new Scanner(System.in);
      while(true) {
        inputsService.exibeOpcoes();
        int opcao = scanner.nextInt();
        if(opcao == 1){
          String cid = inputsService.lerIdDoCliente();
          Produto produto = inputsService.lerIdDoProduto();
          Pedido pedido = inputsService.lerPedido(produto);
          pedidoService.criarPedido(cid, produto, pedido);
        }else if(opcao == 2){
          String cid = inputsService.lerIdDoCliente();
          String oid = inputsService.lerIdDoPedido();
          Produto produto = inputsService.lerIdDoProduto();
          Pedido pedido = inputsService.lerPedido(produto);
          pedidoService.modificarPedido(cid, oid, pedido);
        }else if(opcao == 3){
          String cid =  inputsService.lerIdDoCliente();
          String oid = inputsService.lerIdDoPedido();
          pedidoService.listaPedido(cid, oid);
        }else if(opcao == 4) {
          String cid = inputsService.lerIdDoCliente();
          pedidoService.listaPedidos(cid);
        }else if (opcao == 5) {
          String cid = inputsService.lerIdDoCliente();
          String oid = inputsService.lerIdDoPedido();
          pedidoService.excluirPedido(cid, oid);
        }
      }
    } finally {
      // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
      // resources the channel should be shut down when it will no longer be used. If it may be used
      // again leave it running.
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }

}


