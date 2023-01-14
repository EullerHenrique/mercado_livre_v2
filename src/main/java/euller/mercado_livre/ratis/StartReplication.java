package euller.mercado_livre.ratis;

import java.io.IOException;

public class StartReplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Criando servidor Ratis");
        ServidorRatis servidorRatis = new ServidorRatis("p1");
        servidorRatis = new ServidorRatis("p2");
        servidorRatis = new ServidorRatis("p3");
    }
}

