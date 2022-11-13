package euller.mercado_livre.client.admin;

import com.google.gson.Gson;
import euller.mercado_livre.client.admin.domain.model.Cliente;
import euller.mercado_livre.client.admin.domain.model.Produto;
import euller.mercado_livre.server.admin.*;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static euller.mercado_livre.client.admin.service.InputsService.*;

public class AdminClient {
    private static final Logger logger = Logger.getLogger(AdminClient.class.getName());
    private final ClienteServiceGrpc.ClienteServiceBlockingStub blockingStubCliente;
    private final ProdutoServiceGrpc.ProdutoServiceBlockingStub blockingStubProduto;

    public AdminClient(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStubCliente = ClienteServiceGrpc.newBlockingStub(channel);
        blockingStubProduto = ProdutoServiceGrpc.newBlockingStub(channel);

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

    private void modificarProduto(String PID, Produto produto) {
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
                    Cliente cliente = lerCliente();
                    adminClient.criarCliente(cliente);
                }else if(opcao == 2){
                    String cid = lerIdDoCliente();
                    Cliente cliente = lerCliente();
                    adminClient.modificarCliente(cid, cliente);
                }else if(opcao == 3){
                    String cid = lerIdDoCliente();
                    adminClient.buscarCliente(cid);
                }else if(opcao == 4) {
                    String cid = lerIdDoCliente();
                    adminClient.apagarCliente(cid);
                }else if(opcao == 5) {
                    Produto produto = lerProduto();
                    adminClient.criarProduto(produto);
                }else if(opcao == 6) {
                    String pid = lerIdDoProduto();
                    Produto produto = lerProduto();
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


