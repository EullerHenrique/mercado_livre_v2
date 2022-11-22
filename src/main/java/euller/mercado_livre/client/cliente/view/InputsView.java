package euller.mercado_livre.client.cliente.view;

import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.domain.dto.PedidoDTO;
import euller.mercado_livre.client.cliente.domain.dto.ProdutoDTO;
import euller.mercado_livre.client.cliente.service.external.ClienteService;
import euller.mercado_livre.client.cliente.service.external.ProdutoService;
import euller.mercado_livre.server.cliente.model.Pedido;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;


public class InputsView {

    private ClienteService clienteService;
    private ProdutoService produtoService;
    public InputsView(ClienteService clienteService, ProdutoService produtoService) {
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }



    public void exibePortal() {
        System.out.println("----------------------------------------");
        System.out.println("--------------Mercado Livre-------------");
        System.out.println("--------------Portal CLiente------------");
        System.out.println("----------------------------------------");
    }

    public void exibeOpcoes() {
        System.out.println("\n----------------Opções----------------");
        System.out.println("1 - Criar Pedido                        ");
        System.out.println("2 - Modificar Pedido                    ");
        System.out.println("3 - Consultar Pedido                    ");
        System.out.println("4 - Consultar Pedidos                   ");
        System.out.println("5 - Excluir Pedido                      ");
        System.out.println("----------------------------------------");
        System.out.println("Digite a opção desejada:                ");
    }

    public String lerIdDoPedido() {
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

    public ProdutoDTO lerIdDoProduto() {
        Scanner s = new Scanner(System.in);
        String pid;
        ProdutoDTO produtoDTO;
        while(true) {
            System.out.println("\nDigite o id do produto:             ");
            if (s.hasNextLine()) {
                pid = s.nextLine();
                if (pid != null && !pid.isEmpty()) {
                    String produtoJson = produtoService.buscarProduto(pid);
                    if (!Objects.equals(produtoJson, "false")) {
                        Gson gson = new Gson();
                        produtoDTO = gson.fromJson(produtoJson, ProdutoDTO.class);
                        break;
                    }
                }
            }
        }
        produtoDTO.setPID(pid);
        return produtoDTO;
    }

    public PedidoDTO lerPedido(PedidoDTO pedidoDTO, ProdutoDTO produtoDTO){
        String nomeProduto = produtoDTO.getProduto();
        int precoProduto =  produtoDTO.getPreco();
        int quantidadeProduto = produtoDTO.getQuantidade();
        Scanner s = new Scanner(System.in);
        System.out.println("---------------Produto-----------------");
        System.out.println("\nNome: " + nomeProduto);
        System.out.println("\nQuantidade Disponível " + quantidadeProduto);
        System.out.println("\nPreço: " + produtoDTO.getPreco());
        System.out.println("---------------------------------------");
        ProdutoDTO produtoDTOPedido = new ProdutoDTO();
        produtoDTOPedido.setPID(produtoDTO.getPID());
        produtoDTOPedido.setProduto(nomeProduto);
        int quantidadeProdutoPedido;
        while(true) {
            System.out.println("\nDigite a quantidade:                    ");
            if (s.hasNextInt()) {
                quantidadeProdutoPedido = s.nextInt();
                if (quantidadeProdutoPedido > 0 && quantidadeProdutoPedido <= quantidadeProduto) {
                    produtoDTOPedido.setQuantidade(quantidadeProdutoPedido);
                    break;
                }
            }
        }
        int preco = precoProduto * quantidadeProdutoPedido;
        produtoDTOPedido.setPreco(preco);
        pedidoDTO.getProdutos().add(produtoDTOPedido);
        return pedidoDTO;
    }

    public PedidoDTO lerPedidoAtualizado(PedidoDTO pedidoDTOAntigo, ProdutoDTO produtoDTO){
        PedidoDTO pedidoDTONovo = new PedidoDTO();
        ProdutoDTO produtoDTOPedidoAntigo = pedidoDTOAntigo.getProdutos().stream().filter(p -> p.getPID().equals(produtoDTO.getPID())).findFirst().get();
        ProdutoDTO produtoDTOPedidoNovo = new ProdutoDTO();

        String nomeProduto = produtoDTO.getProduto();
        int precoProduto =  produtoDTO.getPreco();
        int quantidadeProduto = produtoDTO.getQuantidade();

        Scanner s = new Scanner(System.in);
        System.out.println("---------------Produto-----------------");
        System.out.println("\nNome: " + nomeProduto);
        System.out.println("\nQuantidade Disponível " + quantidadeProduto);
        System.out.println("\nPreço: " + produtoDTO.getPreco());
        System.out.println("\nQuantidade Presente No Pedido: " + produtoDTOPedidoAntigo.getQuantidade());
        System.out.println("\nPreço Total Presente No Pedido: " + produtoDTOPedidoAntigo.getPreco());
        System.out.println("---------------------------------------");
        int quantidadeProdutoPedido;
        while(true) {
            System.out.println("\nDigite a nova quantidade:                    ");
            if (s.hasNextInt()) {
                quantidadeProdutoPedido = s.nextInt();
                if (quantidadeProdutoPedido > 0 && quantidadeProdutoPedido <= quantidadeProduto+produtoDTOPedidoAntigo.getQuantidade()) {
                    produtoDTOPedidoNovo.setQuantidade(quantidadeProdutoPedido);
                    break;
                }
            }
        }

        produtoDTOPedidoNovo.setPID(produtoDTOPedidoAntigo.getPID());
        produtoDTOPedidoNovo.setProduto(nomeProduto);
        produtoDTOPedidoNovo.setPreco(precoProduto * quantidadeProdutoPedido);

        pedidoDTONovo.setCID(pedidoDTOAntigo.getCID());
        pedidoDTONovo.setOID(pedidoDTOAntigo.getOID());
        pedidoDTONovo.setProdutos(pedidoDTOAntigo.getProdutos().stream().filter(p -> !p.getPID().equals(produtoDTO.getPID())).collect(Collectors.toList()));
        pedidoDTONovo.getProdutos().add(produtoDTOPedidoNovo);
        return pedidoDTONovo;
    }


    public String lerIdDoCliente()  {
        String cid;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o id do cliente:             ");
            if(s.hasNextLine()) {
                cid = s.nextLine();
                if (cid!= null && !cid.isEmpty() && clienteService.verificarSeClienteExiste(cid)) {
                    break;
                }else {
                    System.out.println("\nCliente não existe");
                }
            }
        }
        return cid;
    }
}
