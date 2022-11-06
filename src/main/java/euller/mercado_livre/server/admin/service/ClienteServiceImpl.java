package euller.mercado_livre.server.admin.service;

import euller.mercado_livre.server.admin.*;
import euller.mercado_livre.server.admin.repository.ClienteRepository;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ClienteServiceImpl extends ClienteServiceGrpc.ClienteServiceImplBase {

    private final ClienteRepository clienteRepository = new ClienteRepository();

    public ClienteServiceImpl() {
        try {
            MosquittoService mosquittoService = new MosquittoService();
            mosquittoService.subscribeCliente("portal/client/CID", clienteRepository);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void criarCliente(CriarClienteRequest req, StreamObserver<CriarClienteResponse> responseObserver) {
        String cliente = clienteRepository.criarCliente(req.getDados());
        CriarClienteResponse reply = CriarClienteResponse.newBuilder().setMessage(cliente).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarCliente(ModificarClienteRequest req, StreamObserver<ModificarClienteResponse> responseObserver) {
        String dados = clienteRepository.modificarCLiente(req.getCID(), req.getDados());
        ModificarClienteResponse reply = ModificarClienteResponse.newBuilder().setMessage("CID: " + req.getCID() + " Cliente: "+dados).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void buscarCliente(BuscarClienteRequest req, StreamObserver<  BuscarClienteResponse> responseObserver) {
        String dados = clienteRepository.buscarCliente(req.getCID());
        BuscarClienteResponse reply =   BuscarClienteResponse.newBuilder().setMessage("CID: " + req.getCID() + " Cliente: "+dados).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarCliente(ApagarClienteRequest req, StreamObserver<ApagarClienteResponse> responseObserver) {
        String msg = clienteRepository.apagarCliente(req.getCID());
        ApagarClienteResponse reply = ApagarClienteResponse.newBuilder().setMessage("CID: " + req.getCID() + " MSG:: "+msg).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
