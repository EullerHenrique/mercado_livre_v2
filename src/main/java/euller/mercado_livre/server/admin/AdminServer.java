package euller.mercado_livre.server.admin;

import euller.mercado_livre.server.admin.service.StartService;
import java.io.IOException;

public class AdminServer {
    private static final StartService startService  = new StartService();

    public static void main(String[] args) throws IOException, InterruptedException {
        startService.start();
        startService.blockUntilShutdown();
    }

}
