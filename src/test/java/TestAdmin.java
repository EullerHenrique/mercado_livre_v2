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
    public static void main(String[] args) throws InterruptedException {
        for(int i = 1; i <=25;i++) {
            //Porta -- Operação -- Nome -- Email -- Telefone
            AdminClient.startServiceTeste.start(8081, new int[]{1}, new String[]{"Euller Henrique Bandeira Oliveira "+i, "eullerhenrique@outlook.com "+i, "(34) 996915315"+i});
        }
        for(int i = 26; i <=50;i++) {
            //Porta -- Operação -- Nome -- Email -- Telefone
            AdminClient.startServiceTeste.start(8086, new int[]{1}, new String[]{"Euller Henrique Bandeira Oliveira "+i, "eullerhenrique@outlook.com "+i, "(34) 996915315"+i});
        }
    }
}


class TesteCriarProduto{
    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 25; i++) {
            //Porta -- Operação -- Nome -- Quantidade -- Preço
            AdminClient.startServiceTeste.start(8082, new int[]{5}, new String[]{"PS " + i, String.valueOf(i * 400), String.valueOf(i * 100)});
        }
        for (int i = 26; i <= 50; i++) {
            //   //Porta -- Operação -- Nome -- Quantidade -- Preço
            AdminClient.startServiceTeste.start(8086, new int[]{5}, new String[]{"PS " + i, String.valueOf(i * 40), String.valueOf(i * 100)});
        }
    }
}

class TestBuscarCliente{
    public static void start(String cid) throws InterruptedException {
        //Porta -- Operação -- CID
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
        //Porta -- Operação -- PID
        AdminClient.startServiceTeste.start(8081, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8082, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8083, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8084, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8085, new int[]{7}, new String[]{pid});
        AdminClient.startServiceTeste.start(8086, new int[]{7}, new String[]{pid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("bd791389-4cc9-488f-b326-494c7e5997be");
    }
}



class TesteModificarCliente{
    public static void main(String[] args) throws InterruptedException{
        //Porta -- Operação -- CID -- Nome -- Email -- Telefone
        AdminClient.startServiceTeste.start(8081, new int[]{2}, new String[]{"e3d28c17-79ac-460e-ab7e-8e5de87434b2",
                    "O euller vai tirar 30", "30@outlook.com", "(30) 303030303030"});
    }
}

class TesteModificarProduto{
    //Porta -- Operação -- CID -- Nome -- Quantidade -- Preço
    public static void main(String[] args) throws InterruptedException {
        AdminClient.startServiceTeste.start(8082, new int[]{6}, new String[]{"b2ca3256-0775-43e9-9340-33e59e3c2679",
                    "ADASD XVCX X", "200", "8000"});
    }
}


class TesteApagarCliente{

    public static void start(String cid) throws InterruptedException {
        //Porta -- Operação -- CID
        AdminClient.startServiceTeste.start(8081, new int[]{4}, new String[]{cid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("e3d28c17-79ac-460e-ab7e-8e5de87434b2");
    }
}

class TesteApagarProduto{
    public static void start(String pid) throws InterruptedException {
        //Porta -- Operação -- PID
        AdminClient.startServiceTeste.start(8082, new int[]{8}, new String[]{pid});
    }
    public static void main(String[] args) throws InterruptedException{;
        start("b2ca3256-0775-43e9-9340-33e59e3c2679");
    }
}

















