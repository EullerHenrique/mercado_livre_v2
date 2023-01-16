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
//7c83c3f1-0a66-4182-8f2c-e3fa127971ae
//216ddcfd-d993-424a-aa20-6384d9a64006

class TesteCriarPedido {
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- List->Operação -- List->Deseja Adiconar Mais um produto -- List->Quantidade do produto -- CID e PID
        ClienteClient.startServiceTeste.start(9091, new int[]{1},new int[]{2}, new int[]{1},
                new String[]{"7c83c3f1-0a66-4182-8f2c-e3fa127971ae", "216ddcfd-d993-424a-aa20-6384d9a64006"});
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
        buscarPedido("03dc3924-b084-4aac-ad0b-3569d8227656", "3ef46a2a-a7a7-4545-a422-3451bad8787b");
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
        buscarPedidos("7a7a909b-1f8a-4fd9-a44e-7b37da3da702");
    }
}

class TesteModificarPedido{
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- List->Operação -- empty -- List->Quantidade do produto -- CID, OID e PID
        ClienteClient.startServiceTeste.start(9091, new int[]{2},new int[]{}, new int[]{500},
                new String[]{"7a7a909b-1f8a-4fd9-a44e-7b37da3da702", "60cb874c-82db-431f-9aa6-618de9997952",
                        "da1ce4ed-2cb6-4ebc-a2e4-2061a609f16e"});
    }
}
class TesteApagarPedido{
    public static void apagarPedido(String cid, String oid) throws InterruptedException {
        //Porta -- List->Operação -- empty -- empty -- CID e OID
        ClienteClient.startServiceTeste.start(9091, new int[]{5}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9092, new int[]{5}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9093, new int[]{5}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9094, new int[]{5}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9095, new int[]{5}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9096, new int[]{5}, new int[]{}, new int[]{}, new String[]{cid, oid});
    }
    public static void main(String[] args) throws InterruptedException{;
        apagarPedido("7c83c3f1-0a66-4182-8f2c-e3fa127971ae", "c94581cb-5b39-47e6-a2f4-8cbb75277545");
    }
}

















