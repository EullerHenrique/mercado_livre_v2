package euller.mercado_livre.server.cliente.service.external;

import euller.mercado_livre.server.cliente.ClienteServiceGrpc;
import euller.mercado_livre.server.cliente.VerificarSeClienteExisteRequest;
import euller.mercado_livre.server.cliente.VerificarSeClienteExisteResponse;
import euller.mercado_livre.server.cliente.respository.external.ClienteRepository;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ClienteServiceImpl extends ClienteServiceGrpc.ClienteServiceImplBase{

    private final ClienteRepository clienteRepository = new ClienteRepository();
    @Override
    public void verificarSeClienteExiste(VerificarSeClienteExisteRequest req, StreamObserver<VerificarSeClienteExisteResponse> responseObserver) {
        String status;
        try {
            status = clienteRepository.verificarSeClienteExiste(req.getCID());
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        VerificarSeClienteExisteResponse reply = VerificarSeClienteExisteResponse.newBuilder().setMessage(status).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
