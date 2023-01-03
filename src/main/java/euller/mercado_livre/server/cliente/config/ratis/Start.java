package euller.mercado_livre.server.cliente.config.ratis;
import java.io.IOException;

public class Start {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Criando servidor Ratis Cliente");
        Servidor servidor = new Servidor("p1");
        servidor = new Servidor("p2");
        servidor = new Servidor("p3");
    }
}

