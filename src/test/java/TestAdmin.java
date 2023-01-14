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
//81a003a0-5761-4d69-967b-831f15280780
//6de31c92-0575-4ee2-9d43-36b3863a9284
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
        start("81a003a0-5761-4d69-967b-831f15280780");
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
        start("6de31c92-0575-4ee2-9d43-36b3863a9284");
    }
}


class TesteModificarCliente{
    public static void main(String[] args) throws InterruptedException{;
        AdminClient.startServiceTeste.start(8082, new int[]{2}, new String[]{"81a003a0-5761-4d69-967b-831f15280780",
                "Henrique Bandeira Oliveira", "henrique@outlook.com", "(34) 996915315"});
    }
}

class TesteModificarProduto{
    public static void main(String[] args) throws InterruptedException {;
        AdminClient.startServiceTeste.start(8084, new int[]{6}, new String[]{"6de31c92-0575-4ee2-9d43-36b3863a9284",
                "XBOX Series X", "400", "4000"});    }
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
        start("81a003a0-5761-4d69-967b-831f15280780");
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
        start("6de31c92-0575-4ee2-9d43-36b3863a9284");
    }
}

















