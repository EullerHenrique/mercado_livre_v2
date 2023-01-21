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




//OK
class TesteCriarPedido {
    //a0d8e3bb-ee3b-471e-9207-4ae2cec72c58
    public static void main(String[] args) throws InterruptedException {
        //Porta -- Operação -- List->Deseja Adicionar Mais um produto? -- List->Quantidade do produto -- CID -- null -- List->PID
        ClienteClient.startServiceTeste.start(
                9091,
                new int[]{1},
                new int[]{1, 1, 1, 2},
                new int[]{1, 20, 30, 40},
                "2fedf459-6a5d-483d-a7ae-a2f01da8c7a1",
                null,
                new String[]{"821d4289-3e77-449d-8008-80cd3199c239",
                        "50914700-33dd-4fc8-bdf1-5ea74e45459b",
                        "ad716e01-fad7-449d-8aab-52089fecfb26",
                        "ff0b8a97-fcdd-4975-9500-5a72744c15f8"});
    }
}


//
class TesteBuscarPedido{
    public static void buscarPedido(String cid, String oid) throws InterruptedException {
        //Porta -- Operação -- null -- null -- CID -- OID -- null
        ClienteClient.startServiceTeste.start(9091, new int[]{3}, null,null, cid, oid, null);
        ClienteClient.startServiceTeste.start(9092, new int[]{3}, null,null, cid, oid, null);
        ClienteClient.startServiceTeste.start(9093, new int[]{3}, null,null, cid, oid, null);
        ClienteClient.startServiceTeste.start(9094, new int[]{3}, null,null, cid, oid, null);
        ClienteClient.startServiceTeste.start(9095, new int[]{3}, null,null, cid, oid, null);
        ClienteClient.startServiceTeste.start(9096, new int[]{3}, null,null, cid, oid, null);
    }
    public static void main(String[] args) throws InterruptedException{;
        buscarPedido("2fedf459-6a5d-483d-a7ae-a2f01da8c7a1", "f7daaf00-0be9-48fa-be2b-c7c79b6fd2a7");
    }
}

//OK
class TesteBuscarPedidos{
    public static void buscarPedidos(String cid) throws InterruptedException {
        //Porta -- Operação -- null -- null -- CID -- OID -- null
        ClienteClient.startServiceTeste.start(9091, new int[]{4}, null, null, cid, null, null);
        ClienteClient.startServiceTeste.start(9092, new int[]{4}, null, null, cid, null, null);
        ClienteClient.startServiceTeste.start(9093, new int[]{4}, null, null, cid, null, null);
        ClienteClient.startServiceTeste.start(9094, new int[]{4}, null, null, cid, null, null);
        ClienteClient.startServiceTeste.start(9095, new int[]{4}, null, null, cid, null, null);
        ClienteClient.startServiceTeste.start(9096, new int[]{4}, null, null, cid, null, null);
    }
    public static void main(String[] args) throws InterruptedException{;
        buscarPedidos("2fedf459-6a5d-483d-a7ae-a2f01da8c7a1");
    }
}


class TesteModificarPedido{
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- Operação -- null -- List->Quantidade do produto -- CID -- OID -- PID
        ClienteClient.startServiceTeste.start(9093, new int[]{2}, null, new int[]{55},
                "2fedf459-6a5d-483d-a7ae-a2f01da8c7a1", "f7daaf00-0be9-48fa-be2b-c7c79b6fd2a7",
                        new String[]{"ad716e01-fad7-449d-8aab-52089fecfb26"});
    }
}
class TesteApagarPedido{
    public static void main(String[] args) throws InterruptedException{
        //Porta -- Operação -- null -- null -- CID -- OID -- null
        ClienteClient.startServiceTeste.start(9094, new int[]{5}, null, null, "2fedf459-6a5d-483d-a7ae-a2f01da8c7a1", "f7daaf00-0be9-48fa-be2b-c7c79b6fd2a7", null);
    }
}

















