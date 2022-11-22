package euller.mercado_livre.client.cliente.config;
import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.domain.dto.PedidoDTO;
import euller.mercado_livre.client.cliente.domain.dto.ProdutoDTO;
import euller.mercado_livre.client.cliente.service.PedidoService;
import euller.mercado_livre.client.cliente.service.external.ClienteService;
import euller.mercado_livre.client.cliente.service.external.ProdutoService;
import euller.mercado_livre.client.cliente.view.InputsView;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Start {

    private final Logger logger = Logger.getLogger(Start.class.getName());

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
        logger.info("Client started, listening on " + port+"\n");
        // Access a service running on the local machine on port 50051
        String target = "localhost:"+port;
        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        ClienteService clienteService = new ClienteService(channel);
        ProdutoService produtoService = new ProdutoService(channel);
        PedidoService pedidoService = new PedidoService(produtoService, channel);
        InputsView inputsView = new InputsView(clienteService, produtoService);
        try {
            inputsView.exibePortal();
            while(true) {
                Scanner scanner = new Scanner(System.in);
                inputsView.exibeOpcoes();
                if(scanner.hasNextInt()) {
                    int opcao = scanner.nextInt();

                    if (opcao == 1) {
                        PedidoDTO pedidoDTO = new PedidoDTO();
                        String cid = inputsView.lerIdDoCliente();
                        pedidoDTO.setCID(cid);

                        List<ProdutoDTO> produtosDTO = new ArrayList<>();
                        int opcaoAdicionarProduto = 1;
                        while (true) {
                            if (opcaoAdicionarProduto == 1) {
                                ProdutoDTO produtoDTO = inputsView.lerIdDoProduto();
                                produtosDTO.add(produtoDTO);
                                if (produtoDTO.getQuantidade() > 0) {
                                    pedidoDTO = inputsView.lerPedido(pedidoDTO, produtoDTO);
                                    System.out.println("\nVocê deseja adicionar mais um produto? (1 - Sim, 2 - Não): ");
                                    scanner = new Scanner(System.in);
                                    if (scanner.hasNextInt()) {
                                        opcaoAdicionarProduto = scanner.nextInt();
                                    }
                                } else {
                                    System.out.println("\nProduto indisponível no momento. Tente novamente mais tarde.");
                                    System.out.println("\nVocê deseja tentar adicionar outro produto? (1 - Sim, 2 - Não): ");
                                    scanner = new Scanner(System.in);
                                    if (scanner.hasNextInt()) {
                                        opcaoAdicionarProduto = scanner.nextInt();
                                    }
                                }
                            } else if (opcaoAdicionarProduto == 2) {
                                break;
                            }
                        }
                        pedidoService.criarPedido(produtosDTO, pedidoDTO);
                    } else if (opcao == 2) {
                        Gson gson = new Gson();
                        String cid = inputsView.lerIdDoCliente();
                        String oid = inputsView.lerIdDoPedido();

                        String pedidoAntigoJson = null;
                        while (pedidoAntigoJson == null) {
                            pedidoAntigoJson = pedidoService.buscarPedido(cid, oid);
                            if ("Pedido não encontrado".equals(pedidoAntigoJson)) {
                                System.out.println("\nPedido não encontrado. Tente novamente.");
                                pedidoAntigoJson = null;
                                oid = inputsView.lerIdDoPedido();
                            }
                        }

                        ProdutoDTO produtoDTO = inputsView.lerIdDoProduto();
                        PedidoDTO pedidoDTOAntigo;
                        System.out.println("\nPedido Antigo: ");
                        System.out.println(pedidoAntigoJson);
                        pedidoDTOAntigo = gson.fromJson(pedidoAntigoJson, PedidoDTO.class);
                        PedidoDTO pedidoDTONovo = inputsView.lerPedidoAtualizado(pedidoDTOAntigo, produtoDTO);
                        System.out.println("\nPedido Atualizado: ");
                        System.out.println(gson.toJson(pedidoDTONovo));
                        pedidoService.modificarPedido(pedidoDTOAntigo, pedidoDTONovo, produtoDTO);

                    } else if (opcao == 3) {
                        String cid = inputsView.lerIdDoCliente();
                        String oid = inputsView.lerIdDoPedido();
                        pedidoService.buscarPedido(cid, oid);
                    } else if (opcao == 4) {
                        String cid = inputsView.lerIdDoCliente();
                        pedidoService.buscarPedidos(cid);
                    } else if (opcao == 5) {
                        String cid = inputsView.lerIdDoCliente();
                        String oid = inputsView.lerIdDoPedido();
                        pedidoService.excluirPedido(cid, oid);
                    }
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
