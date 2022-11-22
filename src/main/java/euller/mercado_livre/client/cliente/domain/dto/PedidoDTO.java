package euller.mercado_livre.client.cliente.domain.dto;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {
    private String CID;
    private String OID;
    private List<ProdutoDTO> produtos = new ArrayList<>();

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }
}
