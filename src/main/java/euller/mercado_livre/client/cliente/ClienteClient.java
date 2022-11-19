package euller.mercado_livre.client.cliente;
import euller.mercado_livre.client.cliente.config.Start;

public class ClienteClient {
  private static final Start initService = new Start();

  public static void main(String[] args) throws InterruptedException {
    initService.start();
  }

}


