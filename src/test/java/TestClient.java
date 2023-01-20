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
//056d36a3-146a-4d1e-b365-890c67aa9d54

//05d13ccc-01e0-4d89-8df3-c6cbe253de01
//53362219-72f9-4ffc-9060-08445130b391
//bb99fc40-20b8-41be-b381-b3af359f25c4
//c0e04967-1496-46ea-ad85-740a95a21885
//1: OK
class TesteCriarPedido {
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- Operação -- List->Deseja Adicionar Mais um produto? -- List->Quantidade do produto -- CID -- null -- List->PID
        ClienteClient.startServiceTeste.start(
                9091,
                new int[]{1},
                new int[]{1, 1, 1, 2},
                new int[]{1, 20, 300, 400},
                "7f2a1be5-ca81-4f08-8df7-2ffecd63037a",
                null,
                new String[]{"2db3d2d5-b1b6-4821-978e-0562581bbeeb",
                        "bcba4f4c-9287-478a-9970-545aa6144482",
                        "476a45a6-f0f9-4897-a7e3-01e606c4071e",
                        "8fadbf3c-a56f-47a5-8782-d2a93880c2d5"});
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
        buscarPedido("15620115-b4ad-4039-8dcf-3b658362b753", "c99ebb55-5f9a-49b8-bbdf-7b03a4d3360d");
    }
}

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
        buscarPedidos("ddb8ca2d-86c6-4944-8656-60f3125b3f55");
    }
}

//3e65a3a8-1e96-4bcf-bd0a-493d90b27269
//e3a60c0d-fee7-46c7-a48a-a2f166a0bd7d
class TesteModificarPedido{
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- Operação -- null -- List->Quantidade do produto -- CID -- OID -- PID
        ClienteClient.startServiceTeste.start(9093, new int[]{2}, null, new int[]{100},
                "0daecae7-e677-4386-81aa-02afce324edb", "f5628618-dfe2-41b3-b70a-26063b92bf51",
                        new String[]{"492d86e8-b5dc-4f77-9fae-6ab82636930a"});
    }
}
class TesteApagarPedido{
    public static void apagarPedido(String cid, String oid) throws InterruptedException {
        //Porta -- Operação -- null -- null -- CID -- OID -- null
        ClienteClient.startServiceTeste.start(9094, new int[]{5}, null, null, cid, oid, null);
    }
    public static void main(String[] args) throws InterruptedException{;
        apagarPedido("7a2899de-4747-4439-8e73-8c0cb38a3a3a", "5cac5580-7d0a-132132-a28f-13123");
        //apagarPedido("7a9a904b-8c6a-4239-940c-c646b664a7f9", "40205f54-a5a4-430a-a4cd-f2b943b57642");
    }
}

















