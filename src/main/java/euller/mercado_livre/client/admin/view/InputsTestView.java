package euller.mercado_livre.client.admin.view;

import euller.mercado_livre.client.admin.domain.dto.ClienteDTO;
import euller.mercado_livre.client.admin.domain.dto.ProdutoDTO;

public class InputsTestView {

    public static void exibePortal() {
        System.out.println("----------------------------------------");
        System.out.println("--------------Mercado Livre-------------");
        System.out.println("--------------Portal Admin--------------");
        System.out.println("----------------------------------------");
    }

    public static void exibeOpcoes() {
        System.out.println("\n----------------Opções----------------");
        System.out.println("1 - Criar Cliente                       ");
        System.out.println("2 - Modificar Cliente                   ");
        System.out.println("3 - Buscar Cliente                      ");
        System.out.println("4 - Apagar Cliente                      ");
        System.out.println("5 - Criar Produto                       ");
        System.out.println("6 - Modificar Produto                   ");
        System.out.println("7 - Buscar Produto                      ");
        System.out.println("8 - Excluir Produto                     ");

        System.out.println("----------------------------------------");
        System.out.println("Digite a opção desejada:                ");
    }

    public static String lerIdDoCliente(String cid) {
        System.out.println("\nDigite o id do cliente:             ");
        System.out.println(cid);
        if (cid == null && cid.isEmpty()) {
            System.out.println("\nCliente invãlido");
            return null;
        }
        return cid;
    }

    public static ClienteDTO lerCliente(String[]valores) {
        ClienteDTO clienteDTO =  new ClienteDTO();

        String nome = valores[0];
        System.out.println("\nDigite o nome do cliente:                    ");
        System.out.println(nome);
        if (nome != null && !nome.isEmpty()) {
            clienteDTO.setNome(nome);
        }else{
            System.out.println("\nNome inválido");
            return null;
        }

        String email = valores[1];
        System.out.println("\nDigite o email do cliente:                    ");
        System.out.println(email);
        if (email != null && !email.isEmpty()) {
            clienteDTO.setEmail(email);
        }else{
            System.out.println("\nEmail inválido");
            return null;
        }

        String telefone = valores[2];
        System.out.println("\nDigite o telefone do cliente:                ");
        System.out.println(telefone);
        if (telefone != null && !telefone.isEmpty()) {
            clienteDTO.setTelefone(telefone);
        }else{
            System.out.println("\nTelefone inválido");
            return null;
        }

        return clienteDTO;
    }

    public static String lerIdDoProduto(String pid) {
        System.out.println("\nDigite o id do produto:             ");
        System.out.println(pid);
        if (pid == null && pid.isEmpty()) {
            System.out.println("\nProduto inválido");
            return null;
        }
        return pid;
    }

    public static ProdutoDTO lerProduto(String[] valores) {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        String nomeProduto = valores[0];
        System.out.println("\nDigite o nome do produto:                    ");
        System.out.println(nomeProduto);
        if (nomeProduto != null && !nomeProduto.isEmpty()) {
            produtoDTO.setProduto(nomeProduto);
        }else{
            System.out.println("\nNome do produto inválido");
            return null;
        }

        int quantidadeProduto = Integer.parseInt(valores[1]);
        System.out.println("\nDigite a quantidade:                    ");
        System.out.println(quantidadeProduto);
        if (quantidadeProduto > 0) {
            produtoDTO.setQuantidade(quantidadeProduto);
        }else{
            System.out.println("\nQuantidade deve ser maior que 0");
            return null;
        }

        int precoProduto = Integer.parseInt(valores[2]);
        System.out.println("\nDigite o preço:                ");
        System.out.println(precoProduto);
        if (precoProduto > 0) {
            produtoDTO.setPreco(precoProduto);
        }else{
            System.out.println("\nPreço deve ser maior que 0");
            return null;
        }

        return produtoDTO;
    }


}
