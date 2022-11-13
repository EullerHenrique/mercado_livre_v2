package euller.mercado_livre.client.cliente;
import euller.mercado_livre.client.cliente.service.StartService;

public class ClienteClient {
  private static final StartService initService = new StartService();

  public static void main(String[] args) throws InterruptedException {
    initService.start(50051);
  }

}


