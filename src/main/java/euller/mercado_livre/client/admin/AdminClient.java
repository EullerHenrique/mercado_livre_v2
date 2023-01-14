package euller.mercado_livre.client.admin;

import euller.mercado_livre.client.admin.config.Start;
import euller.mercado_livre.client.admin.config.StartTest;

public class AdminClient {
    public static final Start startService = new Start();
    public static final StartTest startServiceTeste = new StartTest();

    public static void main(String[] args) throws Exception {
        startService.start(0);
    }

}


