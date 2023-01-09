package euller.mercado_livre.server.cliente.config.ratis;
import java.io.IOException;

public class ReplicationClienteServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Criando servidor Ratis Client");
        ServidorRatis servidorRatis = new ServidorRatis("p1");
        servidorRatis = new ServidorRatis("p2");
        servidorRatis = new ServidorRatis("p3");
    }
}

