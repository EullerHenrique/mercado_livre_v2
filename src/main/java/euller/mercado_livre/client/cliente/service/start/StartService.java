package euller.mercado_livre.client.cliente.service.start;
import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.model.PedidoDTO;
import euller.mercado_livre.client.cliente.model.ProdutoDTO;
import euller.mercado_livre.client.cliente.service.PedidoService;
import euller.mercado_livre.client.cliente.service.external.ClienteService;
import euller.mercado_livre.client.cliente.service.external.ProdutoService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StartService {

    private final Logger logger = Logger.getLogger(StartService.class.getName());

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
        InputsService inputsService = new InputsService(clienteService, produtoService);
        try {
            inputsService.exibePortal();
            Scanner scanner = new Scanner(System.in);
            while(true) {
                inputsService.exibeOpcoes();
                int opcao = scanner.nextInt();
                if(opcao == 1){
                    String cid = inputsService.lerIdDoCliente();
                    ProdutoDTO produtoDTO = inputsService.lerIdDoProduto();
                    PedidoDTO pedidoDTO = inputsService.lerPedido(produtoDTO);
                    pedidoDTO.setCID(cid);
                    pedidoService.criarPedido(produtoDTO, pedidoDTO);
                }else if(opcao == 2){
                    Gson gson = new Gson();
                    String cid = inputsService.lerIdDoCliente();
                    String oid = inputsService.lerIdDoPedido();
                    String pedidoAntigoJson = pedidoService.buscarPedido(cid, oid);
                    String produtoJson;
                    PedidoDTO pedidoDTOAntigo;
                    ProdutoDTO produtoDTO;
                    if(pedidoAntigoJson != null){
                        pedidoDTOAntigo = gson.fromJson(pedidoAntigoJson, PedidoDTO.class);
                        produtoJson = produtoService.buscarProduto(pedidoDTOAntigo.getPID());
                        if(produtoJson != null){
                            produtoDTO = gson.fromJson(produtoJson, ProdutoDTO.class);
                        }else{
                            System.out.println("Produto não encontrado");
                            continue;
                        }
                    }else{
                        System.out.println("Pedido não encontrado");
                        continue;
                    }
                    PedidoDTO pedidoDTONovo = inputsService.lerPedidoAtualizado(pedidoDTOAntigo, produtoDTO);
                    pedidoService.modificarPedido(pedidoDTOAntigo, pedidoDTONovo, produtoDTO);
                }else if(opcao == 3){
                    String cid =  inputsService.lerIdDoCliente();
                    String oid = inputsService.lerIdDoPedido();
                    pedidoService.buscarPedido(cid, oid);
                }else if(opcao == 4) {
                    String cid = inputsService.lerIdDoCliente();
                    pedidoService.buscarPedidos(cid);
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
