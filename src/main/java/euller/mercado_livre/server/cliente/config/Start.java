package euller.mercado_livre.server.cliente.config;

import euller.mercado_livre.server.cliente.service.PedidoServiceImpl;
import euller.mercado_livre.server.cliente.service.external.ClienteServiceImpl;
import euller.mercado_livre.server.cliente.service.external.ProdutoServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Start {

    private final Logger logger = Logger.getLogger(Start.class.getName());
    private Server server;

    public int lerPortaServidor() {
        int port;
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("\nDigite a porta desejada para a criacão do servidor:                    ");
            if (s.hasNextInt()) {
                port = s.nextInt();
                if (port > 0) {
                    break;
                }
            }
        }
        return port;
    }

    public void start(int port) throws IOException{
        if(port==0) {
            port = lerPortaServidor();
        }
        server = ServerBuilder.forPort(port)
                .addService(new ClienteServiceImpl())
                .addService(new ProdutoServiceImpl())
                .addService(new PedidoServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port+"\n");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
               this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
