package euller.mercado_livre.server.cliente;



import euller.mercado_livre.server.cliente.service.StartService;
import java.io.IOException;


public class ClienteServer {

  private static StartService startService  = new StartService();

  public static void main(String[] args) throws IOException, InterruptedException {
    startService.start(50051);
    startService.blockUntilShutdown();
  }


}
