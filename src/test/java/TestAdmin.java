import euller.mercado_livre.client.admin.AdminClient;
import euller.mercado_livre.ratis.StartReplication;
import euller.mercado_livre.server.admin.AdminServer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


class IniciarServidorAdmin{
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        StartReplication.main(args);
        AdminServer.startService.start(8081);
        AdminServer.startService.start(8082);
        AdminServer.startService.start(8083);
        AdminServer.startService.start(8084);
        AdminServer.startService.start(8085);
        AdminServer.startService.start(8086);
    }
}

class TesteCriarCliente {
    public static void main(String[] args) throws InterruptedException {;
        AdminClient.startServiceTeste.start(8081, new int[]{1}, new String[]{"Euller Henrique Bandeira Oliveira", "eullerhenrique@outlook.com", "(34) 996915315"});
    }
}
class TesteCriarProduto{
    public static void main(String[] args) throws InterruptedException {;
        AdminClient.startServiceTeste.start(8081, new int[]{5}, new String[]{"PlayStation 5", "500", "5000"});
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
    public static void main(String[] args) throws InterruptedException{;
        start("0bd89a6c-7cdf-4128-ba3b-3c6e34444f06");
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
        start("e7307ecc-3971-4b37-af5e-51f15b0cac58");
    }
}


class TesteModificarProduto{
    public static void main(String[] args) throws InterruptedException {;
        AdminClient.startService.start(8081);
    }
}
class TesteModificarCliente{
    public static void main(String[] args) throws InterruptedException{;
        AdminClient.startService.start(8081);
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
        start("1af83338-a2c7-4645-8a9b-14cb5f555d5b");
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
        start("34036b9e-2433-4bf2-8d91-a5f384932b92");
    }
}

















