package euller.mercado_livre.client.cliente.view;

import com.google.gson.Gson;
import euller.mercado_livre.client.cliente.domain.dto.PedidoDTO;
import euller.mercado_livre.client.cliente.domain.dto.ProdutoDTO;
import euller.mercado_livre.client.cliente.service.external.ClienteService;
import euller.mercado_livre.client.cliente.service.external.ProdutoService;

import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;


public class InputsTestView {

    private ClienteService clienteService;
    private ProdutoService produtoService;
    public InputsTestView(ClienteService clienteService, ProdutoService produtoService) {
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

    public String lerIdDoCliente(String cid)  {
        System.out.println("\nDigite o id do cliente:             ");
        System.out.println(cid);
        if (cid == null || cid.isEmpty()) {
            System.out.println("Id do cliente inválida");
            return null;
        }else if(!clienteService.verificarSeClienteExiste(cid)){
            System.out.println("Cliente não encontrado");
            return null;
        }
        return cid;
    }
    public ProdutoDTO lerIdDoProduto(String pid) {
        System.out.println("\nDigite o id do produto:             ");
        System.out.println(pid);
        ProdutoDTO produtoDTO;
        if (pid != null && !pid.isEmpty()) {
            String produtoJson = produtoService.buscarProduto(pid);
            if (!Objects.equals(produtoJson, "false")) {
                Gson gson = new Gson();
                produtoDTO = gson.fromJson(produtoJson, ProdutoDTO.class);
                produtoDTO.setPID(pid);
                return produtoDTO;
            }else{
                System.out.println("Produto não encontrado");
            }
        }else{
            System.out.println("Id do produto inválida");
        }
        return null;
    }
    public PedidoDTO lerPedido(PedidoDTO pedidoDTO, ProdutoDTO produtoDTO, int quantidadeProdutoPedido) {
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
        System.out.println("\nDigite a quantidade:                    ");
        System.out.println(quantidadeProdutoPedido);
        if (quantidadeProdutoPedido > 0 && quantidadeProdutoPedido <= quantidadeProduto) {
            produtoDTOPedido.setQuantidade(quantidadeProdutoPedido);
        }else{
            System.out.println("\nA quantidade deve ser maior que 0 e menor ou igual a quantidade disponível");
            return null;
        }
        int preco = precoProduto * quantidadeProdutoPedido;
        produtoDTOPedido.setPreco(preco);
        pedidoDTO.getProdutos().add(produtoDTOPedido);
        return pedidoDTO;
    }
    public String lerIdDoPedido(String oid) {
        System.out.println("\nDigite o id do pedido:             ");
        System.out.println(oid);
        if (oid == null || oid.isEmpty()) {
            System.out.println("Id do pedido inválida");
            return null;
        }
        return oid;
    }
    public PedidoDTO lerPedidoAtualizado(PedidoDTO pedidoDTOAntigo, ProdutoDTO produtoDTO, int quantidadeProdutoPedido) {
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

        System.out.println("\nDigite a nova quantidade:                    ");
        if (quantidadeProdutoPedido > 0 && quantidadeProdutoPedido <= quantidadeProduto+produtoDTOPedidoAntigo.getQuantidade()) {
            produtoDTOPedidoNovo.setQuantidade(quantidadeProdutoPedido);
        }else{
            System.out.println("\nA quantidade deve ser maior que 0 e menor ou igual a quantidade presente no pedido + quantidade disponível");
            return null;
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


}
