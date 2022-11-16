package euller.mercado_livre.client.cliente.service;

import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.model.Pedido;
import euller.mercado_livre.client.cliente.model.Produto;
import euller.mercado_livre.client.cliente.service.external.ClienteService;
import euller.mercado_livre.client.cliente.service.external.ProdutoService;

import java.util.Objects;
import java.util.Scanner;


public class InputsService {

    private ClienteService clienteService;
    private ProdutoService produtoService;
    public InputsService(ClienteService clienteService, ProdutoService produtoService) {
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

    public Produto lerIdDoProduto() {
        Scanner s = new Scanner(System.in);
        String pid;
        Produto produto;
        while(true) {
            System.out.println("\nDigite o id do produto:             ");
            if (s.hasNextLine()) {
                pid = s.nextLine();
                if (pid != null && !pid.isEmpty()) {
                    String produtoJson = produtoService.buscarProduto(pid);
                    if (!Objects.equals(produtoJson, "false")) {
                        Gson gson = new Gson();
                        produto = gson.fromJson(produtoJson, Produto.class);
                        break;
                    }
                }
            }
        }
        produto.setPID(pid);
        return produto;
    }

    public Pedido lerPedido(Produto produto){
        Pedido pedido = new Pedido();
        String nomeProduto = produto.getProduto();
        int precoProduto =  produto.getPreco();
        int quantidadeProduto = produto.getQuantidade();
        Scanner s = new Scanner(System.in);
        System.out.println("---------------Produto-----------------");
        System.out.println("\nNome: " + nomeProduto);
        System.out.println("\nQuantidade Disponível " + quantidadeProduto);
        System.out.println("\nPreço: " + produto.getPreco());
        System.out.println("---------------------------------------");
        pedido.setPID(produto.getPID());
        pedido.setProduto(nomeProduto);
        int quantidadeProdutoPedido;
        while(true) {
            System.out.println("\nDigite a quantidade:                    ");
            if (s.hasNextInt()) {
                quantidadeProdutoPedido = s.nextInt();
                if (quantidadeProdutoPedido > 0 && quantidadeProdutoPedido <= quantidadeProduto) {
                    pedido.setQuantidade(quantidadeProdutoPedido);
                    break;
                }
            }
        }
        int preco = precoProduto * quantidadeProdutoPedido;
        pedido.setPreco(preco);
        return pedido;
    }

    public Pedido lerPedidoAtualizado(Pedido pedidoAntigo, Produto produto){
        Gson gson = new Gson();
        Pedido pedidoNovo = gson.fromJson(gson.toJson(pedidoAntigo), Pedido.class);
        String nomeProduto = produto.getProduto();
        int precoProduto =  produto.getPreco();
        int quantidadeProduto = produto.getQuantidade();
        Scanner s = new Scanner(System.in);
        System.out.println("---------------Produto-----------------");
        System.out.println("\nNome: " + nomeProduto);
        System.out.println("\nQuantidade Disponível " + quantidadeProduto);
        System.out.println("\nQuantidade Presente No Pedido: " + pedidoAntigo.getQuantidade());
        System.out.println("\nPreço: " + produto.getPreco());
        System.out.println("---------------------------------------");
        int quantidadeProdutoPedido;
        while(true) {
            System.out.println("\nDigite a nova quantidade:                    ");
            if (s.hasNextInt()) {
                quantidadeProdutoPedido = s.nextInt();
                if (quantidadeProdutoPedido > 0 && quantidadeProdutoPedido <= quantidadeProduto+pedidoAntigo.getQuantidade()) {
                        pedidoNovo.setQuantidade(quantidadeProdutoPedido);
                    break;
                }
            }
        }
        pedidoNovo.setPreco(precoProduto * quantidadeProdutoPedido);
        return pedidoNovo;
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
