package euller.mercado_livre.client.admin;

import euller.mercado_livre.server.admin.*;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminClient {
    private static final Logger logger = Logger.getLogger(AdminClient.class.getName());

    private final ClienteServiceGrpc.ClienteServiceBlockingStub blockingStub;
    private final ProdutoServiceGrpc.ProdutoServiceBlockingStub blockingStubProduto;


    /**
     * Construct cliente for accessing HelloWorld server using the existing channel.
     */
    public AdminClient(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = ClienteServiceGrpc.newBlockingStub(channel);
        blockingStubProduto = ProdutoServiceGrpc.newBlockingStub(channel);

    }

    private static void exibePortal() {
        System.out.println("----------------------------------------");
        System.out.println("--------------Mercado Livre-------------");
        System.out.println("--------------Portal Admin--------------");
        System.out.println("----------------------------------------");
    }

    private static void exibeOpcoes() {
        System.out.println("\n----------------Opções----------------");
        System.out.println("1 - Criar Cliente                       ");
        System.out.println("2 - Modificar Cliente                   ");
        System.out.println("3 - Buscar Cliente                      ");
        System.out.println("5 - Criar Produto                       ");
        System.out.println("6 - Modificar Produto                   ");
        System.out.println("7 - Buscar Produto                      ");
        System.out.println("8 - Excluir Produto                     ");

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

    private static String lerCliente() {
        String cliente;
        String nome;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o nome do cliente:                    ");
            if (s.hasNextLine()) {
                nome = s.nextLine();
                if (nome != null && !nome.isEmpty()) {
                    cliente = "{ 'nome':" + "'"+nome + "'" +", ";
                    break;
                }
            }
        }
        String email;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o email do cliente:                    ");
            if (s.hasNextLine()) {
                email = s.nextLine();
                if (email != null && !email.isEmpty()) {
                    cliente += "'email':" + "'"+email + "'"+", ";;
                    break;
                }
            }
            s.next();
        }
        String telefone;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o telefone do cliente:                ");
            if (s.hasNextLine()) {
                telefone = s.nextLine();
                if (telefone != null && !telefone.isEmpty()) {
                    cliente += "'telefone':" + "'"+telefone +"'"+" }";;
                    break;
                }
            }
            s.next();
        }
        return cliente;
    }

    private static String lerIdDoCliente() throws MqttException {
        String cid;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o id do cliente:             ");
            if(s.hasNextLine()) {
                cid = s.nextLine();
                if (cid != null && !cid.isEmpty()) {
                    break;
                }else {
                    System.out.println("\nCliente não existe");
                }
            }
        }
        return cid;
    }

    private static String lerIdDoProduto() {
        Scanner s;
        String pid;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o id do produto:             ");
            if (s.hasNextLine()) {
                pid = s.nextLine();
                if (pid != null && !pid.isEmpty()) {
                    break;
                }
            }
        }
        return pid;
    }

    private static String lerProduto() {
        String pedido;
        String nomeProduto;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o nome do produto:                    ");
            if (s.hasNextLine()) {
                nomeProduto = s.nextLine();
                if (nomeProduto != null && !nomeProduto.isEmpty()) {
                    pedido = "{ 'produto':" + "'"+nomeProduto + "'" +", ";
                    break;
                }
            }
        }
        int quantidadeProduto;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite a quantidade:                    ");
            if (s.hasNextInt()) {
                quantidadeProduto = s.nextInt();
                if (quantidadeProduto > 0) {
                    pedido += "'quantidade':" + "'"+quantidadeProduto + "'"+", ";;
                    break;
                }
            }
            s.next();
        }
        int precoProduto;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o preço:                ");
            if (s.hasNextInt()) {
                precoProduto = s.nextInt();
                if (precoProduto > 0) {
                    pedido += "'preco':" + "'"+precoProduto +"'"+" }";;
                    break;
                }
            }
            s.next();
        }
        return pedido;
    }


    public void criarCliente(String cliente) {
        logger.info("Request: Insira o cliente " + cliente);
        String CID = UUID.randomUUID().toString();
        CriarClienteRequest request = CriarClienteRequest.newBuilder().setDados(cliente).build();
        CriarClienteResponse response;
        try {
            response = blockingStub.criarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info(response.getMessage());
    }

    private void modificarCliente(String CID, String cliente) {
        logger.info("Request: Modifique o cliente com o CID: " + CID +" para " + cliente);
        ModificarClienteRequest request = ModificarClienteRequest.newBuilder().setCID(CID).setDados(cliente).build();
        ModificarClienteResponse response;
        try {
            response = blockingStub.modificarCliente(request);
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
            response = blockingStub.buscarCliente(request);
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
            response = blockingStub.apagarCliente(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            System.out.println(e.getMessage());
            return;
        }
        logger.info(response.getMessage());
    }


    public void criarProduto(String produto) {
        logger.info("Request: Insira o produto " + produto);
        String CID = UUID.randomUUID().toString();
        CriarProdutoRequest request = CriarProdutoRequest.newBuilder().setDados(produto).build();
        CriarProdutoResponse response;
        try {
            response = blockingStubProduto.criarProduto(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info(response.getMessage());
    }

    private void modificarProduto(String PID, String produto) {
        logger.info("Request: Modifique o produto com o PID: " + PID +" para " + produto);
        ModificarProdutoRequest request = ModificarProdutoRequest.newBuilder().setPID(PID).setDados(produto).build();
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

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting. The second argument is the target server.
     */
    public static void main(String[] args) throws Exception {
        // Access a service running on the local machine on port 50051
        String target = "localhost:50052";
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
            AdminClient adminClient = new AdminClient(channel);
            Scanner scanner = new Scanner(System.in);
            while(true) {
                exibeOpcoes();
                int opcao = scanner.nextInt();
                if(opcao == 1){
                    String cliente = lerCliente();
                    adminClient.criarCliente(cliente);
                }else if(opcao == 2){
                    String cid = lerIdDoCliente();
                    String cliente = lerCliente();
                    adminClient.modificarCliente(cid, cliente);
                }else if(opcao == 3){
                    String cid = lerIdDoCliente();
                    adminClient.buscarCliente(cid);
                }else if(opcao == 4) {
                    String cid = lerIdDoCliente();
                    adminClient.apagarCliente(cid);
                }else if(opcao == 5) {
                    String produto = lerProduto();
                    adminClient.criarProduto(produto);
                }else if(opcao == 6) {
                    String pid = lerIdDoProduto();
                    String produto = lerProduto();
                    adminClient.modificarProduto(pid, produto);
                }else if(opcao == 7) {
                    String pid = lerIdDoProduto();
                    adminClient.buscarProduto(pid);
                }else if(opcao == 8) {
                    String pid = lerIdDoProduto();
                    adminClient.apagarProduto(pid);
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


