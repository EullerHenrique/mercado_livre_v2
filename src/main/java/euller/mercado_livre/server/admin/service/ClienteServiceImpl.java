package euller.mercado_livre.server.admin.service;

import com.google.gson.Gson;
import euller.mercado_livre.server.admin.*;
import euller.mercado_livre.server.admin.model.Cliente;
import euller.mercado_livre.server.admin.repository.ClienteRepository;
import euller.mercado_livre.server.admin.service.mosquitto.MosquittoService;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

public class ClienteServiceImpl extends ClienteServiceGrpc.ClienteServiceImplBase {

    private final ClienteRepository clienteRepository = new ClienteRepository();

    public ClienteServiceImpl() {
        try {
            MosquittoService mosquittoService = new MosquittoService();
            mosquittoService.subscribeCliente("server/admin/cliente/criar", "", clienteRepository);
            mosquittoService.subscribeCliente("server/cliente/cliente/verificar", "server/admin/cliente/verificar", clienteRepository);
            mosquittoService.subscribeCliente("server/admin/cliente/modificar", "", clienteRepository);
            mosquittoService.subscribeCliente("server/admin/cliente/apagar", "", clienteRepository);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void criarCliente(CriarClienteRequest req, StreamObserver<CriarClienteResponse> responseObserver) {
        Gson gson = new Gson();
        Cliente cliente = gson.fromJson(req.getDados(), Cliente.class);
        cliente.setCID(UUID.randomUUID().toString());
        String clienteJson = clienteRepository.criarCliente(cliente);
        if(clienteJson == null){
            clienteJson = "Cliente já existe";
        }
        CriarClienteResponse reply = CriarClienteResponse.newBuilder().setMessage(clienteJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarCliente(ModificarClienteRequest req, StreamObserver<ModificarClienteResponse> responseObserver) {
        Gson gson = new Gson();
        Cliente cliente = gson.fromJson(req.getDados(), Cliente.class);
        String clienteJson = clienteRepository.modificarCLiente(cliente);
        if(clienteJson == null){
            clienteJson = "Cliente não encontrado";
        }
        ModificarClienteResponse reply = ModificarClienteResponse.newBuilder().setMessage(clienteJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void buscarCliente(BuscarClienteRequest req, StreamObserver<  BuscarClienteResponse> responseObserver) {
        String clienteJson = clienteRepository.buscarCliente(req.getCID());
        if(clienteJson == null){
            clienteJson = "Cliente não encontrado";
        }
        BuscarClienteResponse reply =   BuscarClienteResponse.newBuilder().setMessage(clienteJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarCliente(ApagarClienteRequest req, StreamObserver<ApagarClienteResponse> responseObserver) {
        String msg = clienteRepository.apagarCliente(req.getCID());
        if(msg == null){
            msg = "Cliente não encontrado";
        }
        ApagarClienteResponse reply = ApagarClienteResponse.newBuilder().setMessage(msg).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
