package euller.mercado_livre.client.admin;

import euller.mercado_livre.client.admin.config.Start;
public class AdminClient {
    private static final Start initService = new Start();

    public static void main(String[] args) throws Exception {
        initService.start();
    }

}


