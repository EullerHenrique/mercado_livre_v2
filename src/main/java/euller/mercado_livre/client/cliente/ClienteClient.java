package euller.mercado_livre.client.cliente;

import euller.mercado_livre.server.cliente.*;
import euller.mercado_livre.server.cliente.service.MosquittoService;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteClient {
  private static final Logger logger = Logger.getLogger(ClienteClient.class.getName());

  private final PedidoServiceGrpc.PedidoServiceBlockingStub blockingStub;
  static MosquittoService mosquittoService =  new MosquittoService();

  /**
   * Construct cliente for accessing HelloWorld server using the existing channel.
   */
  public ClienteClient(Channel channel) {
    // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
    // shut it down.

    // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
    blockingStub = PedidoServiceGrpc.newBlockingStub(channel);

  }

  private static void exibePortal() {
    System.out.println("----------------------------------------");
    System.out.println("--------------Mercado Livre-------------");
    System.out.println("--------------Portal CLiente------------");
    System.out.println("----------------------------------------");
  }

  private static void exibeOpcoes() {
    System.out.println("\n----------------Opções----------------");
    System.out.println("1 - Criar Pedido                        ");
    System.out.println("2 - Modificar Pedido                    ");
    System.out.println("3 - Consultar Pedido                    ");
    System.out.println("4 - Consultar Pedidos                   ");
    System.out.println("5 - Excluir Pedido                      ");
    System.out.println("----------------------------------------");
    System.out.println("Digite a opção desejada:                ");
  }

  private static String lerIdDoPedido() {
    Scanner s;
    String oid;
    s = new Scanner(System.in);
    while(true) {
      System.out.println("\nDigite o id do pedido:             ");
      if (s.hasNextLine()) {
        oid = s.nextLine();
        if (oid != null && !oid.isEmpty()) {
          break;
        }
      }
    }
    return oid;
  }

  private static String lerIdDoProduto() throws MqttException {
    Scanner s = new Scanner(System.in);
    String pid;
    String produto;
    while(true) {
      System.out.println("\nDigite o id do produto:             ");
      if (s.hasNextLine()) {
        pid = s.nextLine();
        if (pid != null && !pid.isEmpty()) {
           produto = mosquittoService.buscarProduto(pid);
          if(!Objects.equals(produto, "false"))
            break;
          }
      }
    }

    return pid + " , " + produto;
  }

  private static String lerPedido(String produto){
    String pedido;
    String nomeProduto;
    String precoProduto;
    String quantidadeProduto;
    Scanner s = new Scanner(System.in);
    String[] produtoArray = produto.split(",");
    nomeProduto = produtoArray[0].split(":")[1].split("'")[1].split("'")[0];
    quantidadeProduto = produtoArray[1].split(":")[1].split("}")[0].split("'")[1].split("'")[0];
    precoProduto = produtoArray[2].split(":")[1].split("}")[0].split("'")[1].split("'")[0];
    System.out.println("\nNome do produto: " + nomeProduto);
    pedido = "{ 'produto':" + "'"+ nomeProduto + "'" +", ";
    int quantidadeProdutoPedido;
    while(true) {
      System.out.println("\nDigite a quantidade:                    ");
      if (s.hasNextInt()) {
        quantidadeProdutoPedido = s.nextInt();
        if (quantidadeProdutoPedido > 0 && quantidadeProdutoPedido <= Integer.parseInt(quantidadeProduto)) {
          pedido += "'quantidade':" + "'"+quantidadeProdutoPedido + "'"+", ";;
          break;
        }
      }
    }
    int precoTotal = Integer.parseInt(precoProduto) * quantidadeProdutoPedido;
    pedido += "'preco':" + "'"+ precoTotal +"'"+" }";;
    return pedido;
  }

  private static String lerIdDoCliente() throws MqttException {
    String cid;
    Scanner s = new Scanner(System.in);
    while(true) {
      System.out.println("\nDigite o id do cliente:             ");
      if(s.hasNextLine()) {
        cid = s.nextLine();
        if (cid!= null && !cid.isEmpty() &&  mosquittoService.verifyIfCIDExistis(cid)) {
          break;
        }else {
            System.out.println("\nCliente não existe");
        }
      }
    }
    return cid;
  }

  public void criarPedido(String PID, String CID, String pedido) {
    logger.info("Request: Insira o pedido " + pedido + " com o CID " + CID);
    CriarPedidoRequest request = CriarPedidoRequest.newBuilder().setCID(CID).setDados(pedido).build();
    CriarPedidoResponse response;
    try {
      response = blockingStub.criarPedido(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info(response.getMessage());
  }

  private void modificarPedido(String PID, String CID, String OID, String pedido) {
    logger.info("Request: Modifique o pedido com o CID: " + CID + " e o OID: " + OID + " para " + pedido);
    ModificarPedidoRequest request = ModificarPedidoRequest.newBuilder().setCID(CID).setOID(OID).setDados(pedido).build();
    ModificarPedidoResponse response;
    try {
      response = blockingStub.modificarPedido(request);
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
      response = blockingStub.listarPedido(request);
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
      response = blockingStub.listarPedidos(request);
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
      response = blockingStub.apagarPedido(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      System.out.println(e.getMessage());
      return;
    }
    logger.info(response.getMessage());
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
      exibePortal();
      ClienteClient clienteClient = new ClienteClient(channel);
      Scanner scanner = new Scanner(System.in);
      while(true) {
        exibeOpcoes();
        int opcao = scanner.nextInt();
        if(opcao == 1){
          String cid = lerIdDoCliente();
          String pidProduto = lerIdDoProduto();
          String pid = pidProduto.split(" , ")[0];
          String produto = pidProduto.split(" , ")[1];
          String pedido = lerPedido(produto);
          clienteClient.criarPedido(pid, cid, pedido);
        }else if(opcao == 2){
          String cid = lerIdDoCliente();
          String oid = lerIdDoPedido();
          String pidProduto = lerIdDoProduto();
          String pid = pidProduto.split(" , ")[0];
          String produto = pidProduto.split(" , ")[1];
          String pedido = lerPedido(produto);
          clienteClient.modificarPedido(pid, cid, oid, pedido);
        }else if(opcao == 3){
          String cid =  lerIdDoCliente();
          String oid = lerIdDoPedido();
          clienteClient.listaPedido(cid, oid);
        }else if(opcao == 4) {
          String cid = lerIdDoCliente();
          clienteClient.listaPedidos(cid);
        }else if (opcao == 5) {
          String cid = lerIdDoCliente();
          String oid = lerIdDoPedido();
          clienteClient.excluirPedido(cid, oid);
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


