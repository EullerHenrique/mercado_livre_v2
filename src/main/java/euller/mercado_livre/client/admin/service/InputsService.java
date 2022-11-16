package euller.mercado_livre.client.admin.service;

import euller.mercado_livre.client.admin.model.Cliente;
import euller.mercado_livre.client.admin.model.Produto;

import java.util.Scanner;

public class InputsService {

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

    public static Cliente lerCliente() {
        Cliente cliente =  new Cliente();
        String nome;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o nome do cliente:                    ");
            if (s.hasNextLine()) {
                nome = s.nextLine();
                if (nome != null && !nome.isEmpty()) {
                    cliente.setNome(nome);
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
                    cliente.setEmail(email);
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
                    cliente.setTelefone(telefone);
                    break;
                }
            }
            s.next();
        }
        return cliente;
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

    public static Produto lerProduto() {
        Produto produto = new Produto();
        String nomeProduto;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite o nome do produto:                    ");
            if (s.hasNextLine()) {
                nomeProduto = s.nextLine();
                if (nomeProduto != null && !nomeProduto.isEmpty()) {
                    produto.setProduto(nomeProduto);
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
                    produto.setQuantidade(quantidadeProduto);
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
                    produto.setPreco(precoProduto);
                    break;
                }
            }
            s.next();
        }
        return produto;
    }


}
