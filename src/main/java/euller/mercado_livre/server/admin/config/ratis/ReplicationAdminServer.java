package euller.mercado_livre.server.admin.config.ratis;

import java.io.IOException;

public class ReplicationAdminServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Criando servidor Ratis Admin");
        ServidorRatis servidorRatis = new ServidorRatis("p1");
        servidorRatis = new ServidorRatis("p2");
        servidorRatis = new ServidorRatis("p3");
    }
}

