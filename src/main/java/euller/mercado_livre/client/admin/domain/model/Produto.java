package euller.mercado_livre.client.admin.domain.model;

public class Produto {
    private String produto;
    private int quantidade;
    private int preco;

    public String getProduto() {
        return produto;
    }
    public void setProduto(String produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public int getQuantidade() {
        return quantidade;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }
    public int getPreco() {
        return preco;
    }

}
