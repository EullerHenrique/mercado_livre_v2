package euller.mercado_livre.client.cliente.service.start;

import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.model.PedidoDTO;
import euller.mercado_livre.client.cliente.model.ProdutoDTO;
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

    public PedidoDTO lerPedido(ProdutoDTO produtoDTO){
        PedidoDTO pedidoDTO = new PedidoDTO();
        String nomeProduto = produtoDTO.getProduto();
        int precoProduto =  produtoDTO.getPreco();
        int quantidadeProduto = produtoDTO.getQuantidade();
        Scanner s = new Scanner(System.in);
        System.out.println("---------------Produto-----------------");
        System.out.println("\nNome: " + nomeProduto);
        System.out.println("\nQuantidade Disponível " + quantidadeProduto);
        System.out.println("\nPreço: " + produtoDTO.getPreco());
        System.out.println("---------------------------------------");
        pedidoDTO.setPID(produtoDTO.getPID());
        pedidoDTO.setProduto(nomeProduto);
        int quantidadeProdutoPedido;
        while(true) {
            System.out.println("\nDigite a quantidade:                    ");
            if (s.hasNextInt()) {
                quantidadeProdutoPedido = s.nextInt();
                if (quantidadeProdutoPedido > 0 && quantidadeProdutoPedido <= quantidadeProduto) {
                    pedidoDTO.setQuantidade(quantidadeProdutoPedido);
                    break;
                }
            }
        }
        int preco = precoProduto * quantidadeProdutoPedido;
        pedidoDTO.setPreco(preco);
        return pedidoDTO;
    }

    public PedidoDTO lerPedidoAtualizado(PedidoDTO pedidoDTOAntigo, ProdutoDTO produtoDTO){
        Gson gson = new Gson();
        PedidoDTO pedidoDTONovo = gson.fromJson(gson.toJson(pedidoDTOAntigo), PedidoDTO.class);
        String nomeProduto = produtoDTO.getProduto();
        int precoProduto =  produtoDTO.getPreco();
        int quantidadeProduto = produtoDTO.getQuantidade();
        Scanner s = new Scanner(System.in);
        System.out.println("---------------Produto-----------------");
        System.out.println("\nNome: " + nomeProduto);
        System.out.println("\nQuantidade Disponível " + quantidadeProduto);
        System.out.println("\nQuantidade Presente No Pedido: " + pedidoDTOAntigo.getQuantidade());
        System.out.println("\nPreço: " + produtoDTO.getPreco());
        System.out.println("---------------------------------------");
        int quantidadeProdutoPedido;
        while(true) {
            System.out.println("\nDigite a nova quantidade:                    ");
            if (s.hasNextInt()) {
                quantidadeProdutoPedido = s.nextInt();
                if (quantidadeProdutoPedido > 0 && quantidadeProdutoPedido <= quantidadeProduto+ pedidoDTOAntigo.getQuantidade()) {
                        pedidoDTONovo.setQuantidade(quantidadeProdutoPedido);
                    break;
                }
            }
        }
        pedidoDTONovo.setPreco(precoProduto * quantidadeProdutoPedido);
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
