package euller.mercado_livre.server.admin.service;

import euller.mercado_livre.server.admin.*;
import io.grpc.stub.StreamObserver;

public class ClienteServiceImpl extends ClienteServiceGrpc.ClienteServiceImplBase {

    @Override
    public void inserirCliente(InserirClienteRequest req, StreamObserver<InserirClienteResponse> responseObserver) {
        InserirClienteResponse reply = InserirClienteResponse.newBuilder().setMessage("Inserção do cliente: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarCliente(ModificarClienteRequest req, StreamObserver<ModificarClienteResponse> responseObserver) {
        ModificarClienteResponse reply = ModificarClienteResponse.newBuilder().setMessage("Inserção do cliente: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void recuperarCliente(RecuperarClienteRequest req, StreamObserver<RecuperarClienteResponse> responseObserver) {
        RecuperarClienteResponse reply = RecuperarClienteResponse.newBuilder().setMessage("Inserção do cliente: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarCliente(ApagarClienteRequest req, StreamObserver<ApagarClienteResponse> responseObserver) {
        ApagarClienteResponse reply = ApagarClienteResponse.newBuilder().setMessage("Inserção do cliente: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
