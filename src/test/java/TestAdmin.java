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
        AdminClient.startServiceTeste.start(8081, new int[]{5}, new String[]{"XBOX 5", "500", "5000"});
    }
}
//9993f4f8-b1d6-495a-8620-4d5899eb4748

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
        start("ce585b9b-2a12-4d45-8db9-5398883cf0c8");
    }
}
//766dafcf-4ebc-4a80-8fbc-1d42a59a074b
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
        start("ce585b9b-2a12-4d45-8db9-5398883cf0c8");
    }
}


class TesteModificarCliente{
    public static void main(String[] args) throws InterruptedException{;
        AdminClient.startServiceTeste.start(8082, new int[]{2}, new String[]{"025583c6-5b49-428f-85d0-c3512fad01ea",
                "Henrique Bandeira Oliveira", "henrique@outlook.com", "(34) 996915315"});
    }
}

class TesteModificarProduto{
    public static void main(String[] args) throws InterruptedException {;
        AdminClient.startServiceTeste.start(8084, new int[]{6}, new String[]{"ce585b9b-2a12-4d45-8db9-5398883cf0c8",
                "XBOXXXXXXX Series X", "200", "8000"});    }
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
        start("501296ef-02e9-43ee-9f3f-872dc63acfff");
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
        start("dbc0c56e-4441-49cb-be0a-6f0a120b3497");
    }
}

















