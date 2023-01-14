package euller.mercado_livre.client.admin.config;

import euller.mercado_livre.client.admin.domain.dto.ClienteDTO;
import euller.mercado_livre.client.admin.domain.dto.ProdutoDTO;
import euller.mercado_livre.client.admin.service.ClienteService;
import euller.mercado_livre.client.admin.service.ProdutoService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static euller.mercado_livre.client.admin.view.InputsTestView.*;

public class StartTest {

    private final Logger logger = Logger.getLogger(Start.class.getName());

    public void start(int port, int[] opcoes, String[] valores) throws InterruptedException {
        System.out.println("\nDigite a porta desejada para a conexão com o servidor:                    ");
        System.out.println(port);
        logger.info("Client started, listening on " + port+"\n");
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
            for (int opcao : opcoes) {
                exibeOpcoes();
                System.out.println(opcao);
                if (opcao == 1) {
                    ClienteDTO clienteDTO = lerCliente(valores);
                    clienteService.criarCliente(clienteDTO);
                } else if (opcao == 2) {
                    String cid = lerIdDoCliente(valores[0]);
                    ClienteDTO clienteDTO = lerCliente(valores);
                    clienteDTO.setCID(cid);
                    clienteService.modificarCliente(clienteDTO);
                } else if (opcao == 3) {
                    String cid = lerIdDoCliente(valores[0]);
                    clienteService.buscarCliente(cid);
                } else if (opcao == 4) {
                    String cid = lerIdDoCliente(valores[0]);
                    clienteService.apagarCliente(cid);
                } else if (opcao == 5) {
                    ProdutoDTO produtoDTO = lerProduto(valores);
                    produtoService.criarProduto(produtoDTO);
                } else if (opcao == 6) {
                    String pid = lerIdDoProduto(valores[0]);
                    ProdutoDTO produtoDTO = lerProduto(valores);
                    produtoDTO.setPID(pid);
                    produtoService.modificarProduto(produtoDTO);
                } else if (opcao == 7) {
                    String pid = lerIdDoProduto(valores[0]);
                    produtoService.buscarProduto(pid);
                } else if (opcao == 8) {
                    String pid = lerIdDoProduto(valores[0]);
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
