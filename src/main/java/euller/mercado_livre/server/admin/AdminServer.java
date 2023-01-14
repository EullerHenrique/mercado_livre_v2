package euller.mercado_livre.server.admin;

import euller.mercado_livre.server.admin.config.Start;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AdminServer {
    public static final Start startService  = new Start();

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        startService.start(0);
        startService.blockUntilShutdown();
    }

}
