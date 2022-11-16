package euller.mercado_livre.server.admin.service;

import com.google.gson.Gson;
import euller.mercado_livre.server.admin.*;
import euller.mercado_livre.server.admin.model.Cliente;
import euller.mercado_livre.server.admin.repository.ClienteRepository;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

public class ClienteServiceImpl extends ClienteServiceGrpc.ClienteServiceImplBase {

    private final ClienteRepository clienteRepository = new ClienteRepository();

    public ClienteServiceImpl() {
        try {
            MosquittoService mosquittoService = new MosquittoService();
            mosquittoService.subscribeCliente("portal/client/CID", "portal/admin/CID", clienteRepository);
            mosquittoService.subscribeCliente("portal/admin/cliente/criar", "", clienteRepository);
            mosquittoService.subscribeCliente("portal/admin/cliente/modificar", "", clienteRepository);
            mosquittoService.subscribeCliente("portal/admin/cliente/apagar", "", clienteRepository);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void criarCliente(CriarClienteRequest req, StreamObserver<CriarClienteResponse> responseObserver) {
        Gson gson = new Gson();
        Cliente cliente = gson.fromJson(req.getDados(), Cliente.class);
        cliente.setCID(UUID.randomUUID().toString());
        String clienteJson = clienteRepository.criarCliente(cliente, false);
        CriarClienteResponse reply = CriarClienteResponse.newBuilder().setMessage(clienteJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarCliente(ModificarClienteRequest req, StreamObserver<ModificarClienteResponse> responseObserver) {
        Gson gson = new Gson();
        Cliente cliente = gson.fromJson(req.getDados(), Cliente.class);
        String clienteJson = clienteRepository.modificarCLiente(cliente, false);
        ModificarClienteResponse reply = ModificarClienteResponse.newBuilder().setMessage("CID: " + req.getCID() + " Cliente: "+clienteJson).build();
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
        String msg = clienteRepository.apagarCliente(req.getCID(), false);
        ApagarClienteResponse reply = ApagarClienteResponse.newBuilder().setMessage("CID: " + req.getCID() + " MSG:: "+msg).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
