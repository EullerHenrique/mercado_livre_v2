package euller.mercado_livre.testes;

import euller.mercado_livre.client.cliente.ClienteClient;
import euller.mercado_livre.server.cliente.ClienteServer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class StartTestClient {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {;
        ClienteServer.startService.start(8081);
        ClienteClient.startService.start(8081);
    }
}
