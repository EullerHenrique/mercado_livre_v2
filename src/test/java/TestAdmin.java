import euller.mercado_livre.client.admin.AdminClient;
import euller.mercado_livre.ratis.StartReplication;
import euller.mercado_livre.server.admin.AdminServer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

//OK
class IniciarServidorRatis{
    public static void main(String[] args) throws IOException, InterruptedException {
        StartReplication.main(args);
    }
}

//OK
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

//100: 0K
//1-100: 7a2899de-4747-4439-8e73-8c0cb38a3a3a
class TesteCriarCliente {
    public static void main(String[] args) throws InterruptedException {;
        for(int i = 0; i<=100;i++) {
            AdminClient.startServiceTeste.start(8081, new int[]{1}, new String[]{"Euller Henrique Bandeira Oliveira"+i, "eullerhenrique@outlook.com", "(34) 996915315"});
        }
    }
}

//100: OK
//1-100: a6c4a3ba-e722-4512-b943-9daf0d54a008
class TesteCriarProduto{
    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i<=100;i++) {
            AdminClient.startServiceTeste.start(8081, new int[]{5}, new String[]{"XBOX "+i, "500", "5000"});
        }
    }
}

//1-100: OK
//CACHE: OK
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
        start("e3d28c17-79ac-460e-ab7e-8e5de87434b2");
    }
}

//1-100: OK
//CACHE: OK
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
        start("b2ca3256-0775-43e9-9340-33e59e3c2679");
    }
}


//1-100: OK
//CACHE: OK
class TesteModificarCliente{
    public static void main(String[] args) throws InterruptedException{
        AdminClient.startServiceTeste.start(8081, new int[]{2}, new String[]{"e3d28c17-79ac-460e-ab7e-8e5de87434b2",
                    "Carolayne Bandeirinha Oliveira", "henrique@outlook.com", "(34) 996915315"});
    }
}

//1-100: OK
//CACHE: OK
class TesteModificarProduto{
    public static void main(String[] args) throws InterruptedException {
        AdminClient.startServiceTeste.start(8081, new int[]{6}, new String[]{"b2ca3256-0775-43e9-9340-33e59e3c2679",
                    "ADASD XVCX X", "200", "8000"});
    }
}

//1-100: OK
//CACHE: OK
class TesteApagarCliente{
    public static void start(String cid) throws InterruptedException {
        AdminClient.startServiceTeste.start(8081, new int[]{4}, new String[]{cid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("e3d28c17-79ac-460e-ab7e-8e5de87434b2");
    }
}

//1-100: OK
//CACHE: OK
class TesteApagarProduto{
    public static void start(String pid) throws InterruptedException {
        AdminClient.startServiceTeste.start(8081, new int[]{8}, new String[]{pid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("b2ca3256-0775-43e9-9340-33e59e3c2679");
    }
}

















