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
//7cf84abb-087d-4ea8-87a7-78e6a0dbc1fd
//d1a399b7-a317-4229-8de3-589a226871f0

class TesteCriarPedido {
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- List->Operação -- List->Deseja Adiconar Mais um produto -- List->Quantidade do produto -- CID e PID
        ClienteClient.startServiceTeste.start(9091, new int[]{1},new int[]{2}, new int[]{250},
                new String[]{"7cf84abb-087d-4ea8-87a7-78e6a0dbc1fd", "d1a399b7-a317-4229-8de3-589a226871f0"});
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
        buscarPedido("2e07f24b-6bb5-4333-9d3d-a640872f4cc2", "e5fc507f-7a07-4880-9fb9-0fe63e399448");
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
        buscarPedidos("7cf84abb-087d-4ea8-87a7-78e6a0dbc1fd");
    }
}

//3e65a3a8-1e96-4bcf-bd0a-493d90b27269
//e3a60c0d-fee7-46c7-a48a-a2f166a0bd7d
class TesteModificarPedido{
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- List->Operação -- empty -- List->Quantidade do produto -- CID, OID e PID
        ClienteClient.startServiceTeste.start(9091, new int[]{2},new int[]{}, new int[]{379},
                new String[]{"7e40cfa8-eab5-4617-99c9-9216c0fb3176", "15f9ddf0-ddb9-4fb3-b334-5a6db813e989",
                        "387bd024-02c5-4d8c-85a8-6211a14b71c0"});
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
        apagarPedido("7e40cfa8-eab5-4617-99c9-9216c0fb3176", "15f9ddf0-ddb9-4fb3-b334-5a6db813e989");
    }
}

















