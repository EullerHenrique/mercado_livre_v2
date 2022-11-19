package euller.mercado_livre.server.cliente;

import euller.mercado_livre.server.cliente.config.Start;
import java.io.IOException;

public class ClienteServer {

  private static final Start startService  = new Start();

  public static void main(String[] args) throws IOException, InterruptedException {
    startService.start();
    startService.blockUntilShutdown();
  }


}
