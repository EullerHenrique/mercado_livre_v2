package euller.mercado_livre.server.cliente;

import euller.mercado_livre.server.cliente.config.Start;
import java.io.IOException;

public class ClienteServer {

  public static final Start startService  = new Start();

  public static void main(String[] args) throws IOException, InterruptedException {
    startService.start(0);
    startService.blockUntilShutdown();
  }

}
