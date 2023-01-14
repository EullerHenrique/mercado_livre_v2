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

class TesteCriarPedido {
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- List->Operação -- List->Deseja Adiconar Mais um produto -- List->Quantidade do produto -- CID e PID
        ClienteClient.startServiceTeste.start(9091, new int[]{1},new int[]{2}, new int[]{1}, new String[]{"550c5d88-c7ec-4870-b5a1-31b1779f5203", "c7adb45f-8f1c-4d84-8282-4ef651b05f6c"});
    }
}
//550c5d88-c7ec-4870-b5a1-31b1779f5203
//c7adb45f-8f1c-4d84-8282-4ef651b05f6c
class TesteBuscarPedido{
    public static void buscarPedido(String cid, String oid) throws InterruptedException {
        //Porta -- List->Operação -- empty -- empty -- CID e PID
        ClienteClient.startServiceTeste.start(9091, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9092, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9093, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9094, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9095, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
        ClienteClient.startServiceTeste.start(9096, new int[]{3}, new int[]{}, new int[]{}, new String[]{cid, oid});
    }
    public static void main(String[] args) throws InterruptedException{;
        buscarPedido("550c5d88-c7ec-4870-b5a1-31b1779f5203", "fdb0b9d1-bd46-41a0-b0c6-2f987bbba97e");
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
        buscarPedidos("550c5d88-c7ec-4870-b5a1-31b1779f5203");
    }
}

class TesteModificarPedido{
    public static void main(String[] args) throws InterruptedException {;
        //Porta -- List->Operação -- empty -- List->Quantidade do produto -- CID, OID e PID
        ClienteClient.startServiceTeste.start(9091, new int[]{2},new int[]{}, new int[]{500}, new String[]{"1aa3a480-eb9b-45e3-b2a2-6d42b53a68af", "e31b7694-93f5-4938-8691-910e87739049", ""});
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
        apagarPedido("550c5d88-c7ec-4870-b5a1-31b1779f5203", "fdb0b9d1-bd46-41a0-b0c6-2f987bbba97e");
    }
}

















