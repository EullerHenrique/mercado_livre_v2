package euller.mercado_livre.client.admin.view;

import euller.mercado_livre.client.admin.domain.dto.ClienteDTO;
import euller.mercado_livre.client.admin.domain.dto.ProdutoDTO;

import java.util.Scanner;

public class InputsView {

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

    public static String lerIdDoCliente() {
        String cid;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o id do cliente:             ");
            if(s.hasNextLine()) {
                cid = s.nextLine();
                if (cid != null && !cid.isEmpty()) {
                    break;
                }else {
                    System.out.println("\nCliente não existe");
                }
            }
        }
        return cid;
    }

    public static ClienteDTO lerCliente() {
        ClienteDTO clienteDTO =  new ClienteDTO();
        String nome;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o nome do cliente:                    ");
            if (s.hasNextLine()) {
                nome = s.nextLine();
                if (nome != null && !nome.isEmpty()) {
                    clienteDTO.setNome(nome);
                    break;
                }
            }
        }
        String email;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o email do cliente:                    ");
            if (s.hasNextLine()) {
                email = s.nextLine();
                if (email != null && !email.isEmpty()) {
                    clienteDTO.setEmail(email);
                    break;
                }
            }
            s.next();
        }
        String telefone;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o telefone do cliente:                ");
            if (s.hasNextLine()) {
                telefone = s.nextLine();
                if (telefone != null && !telefone.isEmpty()) {
                    clienteDTO.setTelefone(telefone);
                    break;
                }
            }
            s.next();
        }
        return clienteDTO;
    }

    public static String lerIdDoProduto() {
        Scanner s;
        String pid;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o id do produto:             ");
            if (s.hasNextLine()) {
                pid = s.nextLine();
                if (pid != null && !pid.isEmpty()) {
                    break;
                }
            }
        }
        return pid;
    }

    public static ProdutoDTO lerProduto() {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        String nomeProduto;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o nome do produto:                    ");
            if (s.hasNextLine()) {
                nomeProduto = s.nextLine();
                if (nomeProduto != null && !nomeProduto.isEmpty()) {
                    produtoDTO.setProduto(nomeProduto);
                    break;
                }
            }
        }
        int quantidadeProduto;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite a quantidade:                    ");
            if (s.hasNextInt()) {
                quantidadeProduto = s.nextInt();
                if (quantidadeProduto > 0) {
                    produtoDTO.setQuantidade(quantidadeProduto);
                    break;
                }
            }
            s.next();
        }
        int precoProduto;
        s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o preço:                ");
            if (s.hasNextInt()) {
                precoProduto = s.nextInt();
                if (precoProduto > 0) {
                    produtoDTO.setPreco(precoProduto);
                    break;
                }
            }
            s.next();
        }
        return produtoDTO;
    }


}
