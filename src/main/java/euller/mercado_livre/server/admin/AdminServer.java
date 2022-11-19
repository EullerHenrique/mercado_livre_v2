package euller.mercado_livre.server.admin;

import euller.mercado_livre.server.admin.config.Start;
import java.io.IOException;

public class AdminServer {
    private static final Start startService  = new Start();

    public static void main(String[] args) throws IOException, InterruptedException {
        startService.start();
        startService.blockUntilShutdown();
    }

}
