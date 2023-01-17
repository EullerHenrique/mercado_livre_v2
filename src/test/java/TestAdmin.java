import euller.mercado_livre.client.admin.AdminClient;
import euller.mercado_livre.ratis.StartReplication;
import euller.mercado_livre.server.admin.AdminServer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

class IniciarServidorRatis{
    public static void main(String[] args) throws IOException, InterruptedException {
        StartReplication.main(args);
    }
}

class IniciarServidorAdmin{
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        AdminServer.startService.start(8081);
        AdminServer.startService.start(8082);
        AdminServer.startService.start(8083);
        AdminServer.startService.start(8084);
        AdminServer.startService.start(8085);
        AdminServer.startService.start(8086);
    }
}
//7cf84abb-087d-4ea8-87a7-78e6a0dbc1fd
//d1a399b7-a317-4229-8de3-589a226871f0
class TesteCriarCliente {
    public static void main(String[] args) throws InterruptedException {;
        //for(int i = 0; i<=100;i++) {
            AdminClient.startServiceTeste.start(8081, new int[]{1}, new String[]{"Euller Henrique Bandeira Oliveira", "eullerhenrique@outlook.com", "(34) 996915315"});
        //}
    }
}

class TesteCriarProduto{
    public static void main(String[] args) throws InterruptedException {
        //for(int i = 0; i<=100;i++) {
            AdminClient.startServiceTeste.start(8081, new int[]{5}, new String[]{"XBOX 5", "500", "5000"});
        //}
    }
}

class TestBuscarCliente{
    public static void start(String cid) throws InterruptedException {
        AdminClient.startServiceTeste.start(8081, new int[]{3}, new String[]{cid});
        AdminClient.startServiceTeste.start(8082, new int[]{3}, new String[]{cid});
        AdminClient.startServiceTeste.start(8083, new int[]{3}, new String[]{cid});
        AdminClient.startServiceTeste.start(8084, new int[]{3}, new String[]{cid});
        AdminClient.startServiceTeste.start(8085, new int[]{3}, new String[]{cid});
        AdminClient.startServiceTeste.start(8086, new int[]{3}, new String[]{cid});
    }
    public static void main(String[] args) throws InterruptedException{
        start("aa676681-92dc-4247-9cb0-d6dd0f277c84");
    }
}

class TesteBuscarProduto{
    public static void start(String pid) throws InterruptedException {
        AdminClient.startServiceTeste.start(8081, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8082, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8083, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8084, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8085, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8086, new int[]{7}, new String[]{pid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("98026031-9448-481b-9ea9-bc8a06cc0518");
    }
}


class TesteModificarCliente{
    public static void main(String[] args) throws InterruptedException{
        //for(int i = 0; i<=100;i++) {
            AdminClient.startServiceTeste.start(8082, new int[]{2}, new String[]{"aa676681-92dc-4247-9cb0-d6dd0f277c84",
                    "Carolayne Bandeirinha Oliveira", "henrique@outlook.com", "(34) 996915315"});
        //}
    }
}

class TesteModificarProduto{
    public static void main(String[] args) throws InterruptedException {
        //for(int i = 0; i<=100;i++) {
            AdminClient.startServiceTeste.start(8084, new int[]{6}, new String[]{"98026031-9448-481b-9ea9-bc8a06cc0518",
                    "nintendo Series X", "200", "8000"});
        //}
    }
}


class TesteApagarCliente{
    public static void start(String cid) throws InterruptedException {
        AdminClient.startServiceTeste.start(8081, new int[]{4}, new String[]{cid});
        AdminClient.startServiceTeste.start(8082, new int[]{4}, new String[]{cid});
        AdminClient.startServiceTeste.start(8083, new int[]{4}, new String[]{cid});
        AdminClient.startServiceTeste.start(8084, new int[]{4}, new String[]{cid});
        AdminClient.startServiceTeste.start(8085, new int[]{4}, new String[]{cid});
        AdminClient.startServiceTeste.start(8086, new int[]{4}, new String[]{cid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("aa676681-92dc-4247-9cb0-d6dd0f277c84");
    }
}
class TesteApagarProduto{
    public static void start(String pid) throws InterruptedException {
        AdminClient.startServiceTeste.start(8081, new int[]{8}, new String[]{pid});
        AdminClient.startServiceTeste.start(8082, new int[]{8}, new String[]{pid});
        AdminClient.startServiceTeste.start(8083, new int[]{8}, new String[]{pid});
        AdminClient.startServiceTeste.start(8084, new int[]{8}, new String[]{pid});
        AdminClient.startServiceTeste.start(8085, new int[]{8}, new String[]{pid});
        AdminClient.startServiceTeste.start(8086, new int[]{8}, new String[]{pid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("98026031-9448-481b-9ea9-bc8a06cc0518");
    }
}

















