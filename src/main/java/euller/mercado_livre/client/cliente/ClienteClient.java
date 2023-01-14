package euller.mercado_livre.client.cliente;
import euller.mercado_livre.client.cliente.config.Start;
import euller.mercado_livre.client.cliente.config.StartTest;

public class ClienteClient {
  public static final Start startService = new Start();
  public static final StartTest startServiceTeste = new StartTest();

  public static void main(String[] args) throws InterruptedException {
    startService.start(0);
  }

}


