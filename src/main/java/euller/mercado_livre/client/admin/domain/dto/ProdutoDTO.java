package euller.mercado_livre.client.admin.domain.dto;
import euller.mercado_livre.client.cliente.domain.model.Produto;
public class ProdutoDTO {

    public ProdutoDTO(Produto produto) {
        this.produto = produto.getProduto();
        this.quantidade = produto.getQuantidade();
        this.preco = produto.getPreco();
    }

    private String produto;
    private int quantidade;
    private int preco;

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }
}
