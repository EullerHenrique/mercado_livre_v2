import euller.mercado_livre.client.cliente.ClienteClient;
import euller.mercado_livre.server.cliente.ClienteServer;
import java.io.IOException;

class IniciarServidorCliente{
    public static void main(String[] args) throws IOException {
        ClienteServer.startService.start(9091);
        ClienteServer.startService.start(9092);
        ClienteServer.startService.start(9093);
        ClienteServer.startService.start(9094);
        ClienteServer.startService.start(9095);
        ClienteServer.startService.start(9096);
    }
}
//7a2899de-4747-4439-8e73-8c0cb38a3a3a
//a6c4a3ba-e722-4512-b943-9daf0d54a008

//1: OK
class TesteCriarPedido {
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- List->Operação -- List->Deseja Adiconar Mais um produto -- List->Quantidade do produto -- CID e PID
        ClienteClient.startServiceTeste.start(9091, new int[]{1},new int[]{2}, new int[]{9},
                new String[]{"7a2899de-4747-4439-8e73-8c0cb38a3a3a", "a6c4a3ba-e722-4512-b943-9daf0d54a008"});
    }
}

class TesteBuscarPedido{
    public static void buscarPedido(String cid, String oid) throws InterruptedException {
        //Porta -- List->Operação -- empty -- empty -- CID e OID
        ClienteClient.startServiceTeste.start(9091, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9092, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9093, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9094, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9095, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9096, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
    }
    public static void main(String[] args) throws InterruptedException{;
        buscarPedido("0daecae7-e677-4386-81aa-02afce324edb", "df05b0b0-cc2b-4394-8af7-1aef675c6a8f");
    }
}

class TesteBuscarPedidos{
    public static void buscarPedidos(String cid) throws InterruptedException {
        //Porta -- List->Operação -- empty -- empty -- CID
        ClienteClient.startServiceTeste.start(9091, new int[]{4}, new int[]{}, new int[]{}, new String[]{cid});
        ClienteClient.startServiceTeste.start(9092, new int[]{4}, new int[]{}, new int[]{}, new String[]{cid});
        ClienteClient.startServiceTeste.start(9093, new int[]{4}, new int[]{}, new int[]{}, new String[]{cid});
        ClienteClient.startServiceTeste.start(9094, new int[]{4}, new int[]{}, new int[]{}, new String[]{cid});
        ClienteClient.startServiceTeste.start(9095, new int[]{4}, new int[]{}, new int[]{}, new String[]{cid});
        ClienteClient.startServiceTeste.start(9096, new int[]{4}, new int[]{}, new int[]{}, new String[]{cid});
    }
    public static void main(String[] args) throws InterruptedException{;
        buscarPedidos("ddb8ca2d-86c6-4944-8656-60f3125b3f55");
    }
}

//3e65a3a8-1e96-4bcf-bd0a-493d90b27269
//e3a60c0d-fee7-46c7-a48a-a2f166a0bd7d
class TesteModificarPedido{
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- List->Operação -- empty -- List->Quantidade do produto -- CID, OID e PID
        ClienteClient.startServiceTeste.start(9091, new int[]{2},new int[]{}, new int[]{100},
                new String[]{"0daecae7-e677-4386-81aa-02afce324edb", "f5628618-dfe2-41b3-b70a-26063b92bf51",
                        "492d86e8-b5dc-4f77-9fae-6ab82636930a"});
    }
}
class TesteApagarPedido{
    public static void apagarPedido(String cid, String oid) throws InterruptedException {
        //Porta -- List->Operação -- empty -- empty -- CID e OID
        ClienteClient.startServiceTeste.start(9091, new int[]{5}, new int[]{}, new int[]{}, new String[]{cid, oid});
    }
    public static void main(String[] args) throws InterruptedException{;
        apagarPedido("7a2899de-4747-4439-8e73-8c0cb38a3a3a", "5cac5580-7d0a-132132-a28f-13123");
        //apagarPedido("7a9a904b-8c6a-4239-940c-c646b664a7f9", "40205f54-a5a4-430a-a4cd-f2b943b57642");
    }
}

















