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

//056d36a3-146a-4d1e-b365-890c67aa9d54
class TesteCriarCliente {
    public static void main(String[] args) throws InterruptedException {
        for(int i = 1; i <=50;i++) {
            AdminClient.startServiceTeste.start(8081, new int[]{1}, new String[]{"Euller Henrique Bandeira Oliveira "+i, "eullerhenrique@outlook.com "+i, "(34) 996915315"+i});
        }
    }
}
//15620115-b4ad-4039-8dcf-3b658362b753

//05d13ccc-01e0-4d89-8df3-c6cbe253de01
//53362219-72f9-4ffc-9060-08445130b391
//bb99fc40-20b8-41be-b381-b3af359f25c4
//c0e04967-1496-46ea-ad85-740a95a21885
class TesteCriarProduto{
    public static void main(String[] args) throws InterruptedException {
        for(int i = 1; i <= 50;i++) {
            //Porta -- Operação -- Nome -- Quantidade -- Preço
            AdminClient.startServiceTeste.start(8082, new int[]{5}, new String[]{"PS "+i, String.valueOf(i*10), String.valueOf(i*100)});
        }
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
        start("e3d28c17-79ac-460e-ab7e-8e5de87434b2");
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
        start("53362219-72f9-4ffc-9060-08445130b391");
    }
}



class TesteModificarCliente{
    public static void main(String[] args) throws InterruptedException{
        AdminClient.startServiceTeste.start(8081, new int[]{2}, new String[]{"e3d28c17-79ac-460e-ab7e-8e5de87434b2",
                    "Carolayne Bandeirinha Oliveira", "henrique@outlook.com", "(34) 996915315"});
    }
}

class TesteModificarProduto{
    public static void main(String[] args) throws InterruptedException {
        AdminClient.startServiceTeste.start(8082, new int[]{6}, new String[]{"b2ca3256-0775-43e9-9340-33e59e3c2679",
                    "ADASD XVCX X", "200", "8000"});
    }
}


class TesteApagarCliente{
    public static void start(String cid) throws InterruptedException {
        AdminClient.startServiceTeste.start(8081, new int[]{4}, new String[]{cid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("e3d28c17-79ac-460e-ab7e-8e5de87434b2");
    }
}

class TesteApagarProduto{
    public static void start(String pid) throws InterruptedException {
        AdminClient.startServiceTeste.start(8082, new int[]{8}, new String[]{pid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("b2ca3256-0775-43e9-9340-33e59e3c2679");
    }
}

















