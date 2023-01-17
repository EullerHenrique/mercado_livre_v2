package euller.mercado_livre.client.cliente.config;
import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.domain.dto.PedidoDTO;
import euller.mercado_livre.client.cliente.domain.dto.ProdutoDTO;
import euller.mercado_livre.client.cliente.service.PedidoService;
import euller.mercado_livre.client.cliente.service.external.ClienteService;
import euller.mercado_livre.client.cliente.service.external.ProdutoService;
import euller.mercado_livre.client.cliente.view.InputsTestView;
import euller.mercado_livre.client.cliente.view.InputsView;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StartTest {

    private final Logger logger = Logger.getLogger(Start.class.getName());

    public void start(int port, int[] opcoes, int[] opcoesAdicionarProduto, int[] quantidadeProdutos, String[] valores) throws InterruptedException {
        System.out.println("\nDigite a porta desejada para a conexão com o servidor:                    ");
        System.out.println(port);
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
        InputsTestView inputsView = new InputsTestView(clienteService, produtoService);
        try {
            inputsView.exibePortal();
            for (int opcao : opcoes) {
                inputsView.exibeOpcoes();
                    if (opcao == 1) {
                        PedidoDTO pedidoDTO = new PedidoDTO();
                        String cid = inputsView.lerIdDoCliente(valores[0]);
                        if(cid != null) {
                            pedidoDTO.setCID(cid);
                            List<ProdutoDTO> produtosDTO = new ArrayList<>();
                            int opcaoAdicionarProduto = 1;
                            int i = 0;
                            while (true) {
                                if (opcaoAdicionarProduto == 1) {
                                    ProdutoDTO produtoDTO = inputsView.lerIdDoProduto(valores[1]);
                                    if (produtoDTO != null) {
                                        if (produtoDTO.getQuantidade() > 0) {
                                            produtosDTO.add(produtoDTO);
                                            pedidoDTO = inputsView.lerPedido(pedidoDTO, produtoDTO, quantidadeProdutos[i]);
                                            if(pedidoDTO != null) {
                                                System.out.println("\nVocê deseja adicionar mais um produto? (1 - Sim, 2 - Não): ");
                                                System.out.println(opcoesAdicionarProduto[i]);
                                                opcaoAdicionarProduto = opcoesAdicionarProduto[i];
                                                i++;
                                                if (opcaoAdicionarProduto != 1 && opcaoAdicionarProduto != 2) {
                                                    System.out.println("Opção inválida!");
                                                }
                                            }else{
                                                break;
                                            }
                                        } else {
                                            System.out.println("\nProduto indisponível no momento. Tente novamente mais tarde.");
                                            System.out.println("\nVocê deseja tentar adicionar outro produto? (1 - Sim, 2 - Não): ");
                                            System.out.println(opcoesAdicionarProduto[i]);
                                            opcaoAdicionarProduto = opcoesAdicionarProduto[i];
                                            i++;
                                            if (opcaoAdicionarProduto != 1 && opcaoAdicionarProduto != 2) {
                                                System.out.println("Opção inválida!");
                                            }
                                        }
                                    }
                                } else if (opcaoAdicionarProduto == 2) {
                                    break;
                                }
                            }
                            if(pedidoDTO != null && produtosDTO.size() > 0) {
                                System.out.println("\nPedido realizado com sucesso!");
                                pedidoService.criarPedido(produtosDTO, pedidoDTO);
                            }
                        }
                    } else if (opcao == 2) {
                        Gson gson = new Gson();
                        String cid = inputsView.lerIdDoCliente(valores[0]);
                        String oid = inputsView.lerIdDoPedido(valores[1]);

                        if(cid != null && oid != null) {
                            String pedidoAntigoJson = pedidoService.buscarPedido(cid, oid);
                            if ("Pedido não encontrado".equals(pedidoAntigoJson)) {
                                System.out.println("\nPedido não encontrado.");
                            } else {
                                ProdutoDTO produtoDTO = inputsView.lerIdDoProduto(valores[2]);
                                if (produtoDTO != null) {
                                    PedidoDTO pedidoDTOAntigo;
                                    pedidoDTOAntigo = gson.fromJson(pedidoAntigoJson, PedidoDTO.class);
                                    PedidoDTO pedidoDTONovo = inputsView.lerPedidoAtualizado(pedidoDTOAntigo, produtoDTO, quantidadeProdutos[0]);
                                    if (pedidoDTONovo != null) {
                                        pedidoService.modificarPedido(pedidoDTOAntigo, pedidoDTONovo, produtoDTO);
                                    }
                                }
                            }
                        }
                    } else if (opcao == 3) {
                        String cid = inputsView.lerIdDoCliente(valores[0]);
                        if(cid != null) {
                            String oid = inputsView.lerIdDoPedido(valores[1]);
                            if(oid != null) {
                                pedidoService.buscarPedido(cid, oid);
                            }
                        }
                    } else if (opcao == 4) {
                        String cid = inputsView.lerIdDoCliente(valores[0]);
                        if(cid != null) {
                            pedidoService.buscarPedidos(cid);
                        }
                    } else if (opcao == 5) {
                        String cid = inputsView.lerIdDoCliente(valores[0]);
                        if(cid != null) {
                            String oid = inputsView.lerIdDoPedido(valores[1]);
                            if(oid != null) {
                                pedidoService.excluirPedido(cid, oid);
                            }
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
