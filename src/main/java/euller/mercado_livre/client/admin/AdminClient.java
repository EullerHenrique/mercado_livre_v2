package euller.mercado_livre.client.admin;

import euller.mercado_livre.client.admin.service.start.StartService;
public class AdminClient {
    private static final StartService initService = new StartService();

    public static void main(String[] args) throws Exception {
        initService.start();
    }

}


