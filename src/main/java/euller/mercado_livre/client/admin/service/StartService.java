package euller.mercado_livre.client.admin.service;

import euller.mercado_livre.client.admin.domain.model.Cliente;
import euller.mercado_livre.client.admin.domain.model.Produto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static euller.mercado_livre.client.admin.service.InputsService.*;
import static euller.mercado_livre.client.admin.service.InputsService.lerIdDoProduto;

public class StartService {

    public int lerPortaServidor() {
        int port;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite a porta desejada para a conexão com o servidor:                    ");
            if (s.hasNextInt()) {
                port = s.nextInt();
                if (port > 0) {
                    break;
                }
            }
        }
        return port;
    }

    public void start() throws InterruptedException {
        int port = lerPortaServidor();
        String target = "localhost:"+port;
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
            ClienteService clienteService = new ClienteService(channel);
            ProdutoService produtoService = new ProdutoService(channel);
            Scanner scanner = new Scanner(System.in);
            while(true) {
                exibeOpcoes();
                int opcao = scanner.nextInt();
                if(opcao == 1){
                    Cliente cliente = lerCliente();
                    clienteService.criarCliente(cliente);
                }else if(opcao == 2){
                    String cid = lerIdDoCliente();
                    Cliente cliente = lerCliente();
                    clienteService.modificarCliente(cid, cliente);
                }else if(opcao == 3){
                    String cid = lerIdDoCliente();
                    clienteService.buscarCliente(cid);
                }else if(opcao == 4) {
                    String cid = lerIdDoCliente();
                    clienteService.apagarCliente(cid);
                }else if(opcao == 5) {
                    Produto produto = lerProduto();
                    produtoService.criarProduto(produto);
                }else if(opcao == 6) {
                    String pid = lerIdDoProduto();
                    Produto produto = lerProduto();
                    produtoService.modificarProduto(pid, produto);
                }else if(opcao == 7) {
                    String pid = lerIdDoProduto();
                    produtoService.buscarProduto(pid);
                }else if(opcao == 8) {
                    String pid = lerIdDoProduto();
                    produtoService.apagarProduto(pid);
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
