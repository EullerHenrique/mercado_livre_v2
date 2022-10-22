package euller.mercado_livre.server.cliente.service;

import euller.mercado_livre.server.cliente.*;
import io.grpc.stub.StreamObserver;

public class PedidoServiceImpl extends PedidoServiceGrpc.PedidoServiceImplBase {

    @Override
    public void criarPedido(CriarPedidoRequest req, StreamObserver<CriarPedidoResponse> responseObserver) {
        CriarPedidoResponse reply = CriarPedidoResponse.newBuilder().setMessage("Inserção do cliente: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarPedido(ModificarPedidoRequest req, StreamObserver<ModificarPedidoResponse> responseObserver) {
        ModificarPedidoResponse reply = ModificarPedidoResponse.newBuilder().setMessage("Inserção do cliente: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void listarPedido(ListarPedidoRequest req, StreamObserver<ListarPedidoResponse> responseObserver) {
        ListarPedidoResponse reply = ListarPedidoResponse.newBuilder().setMessage("Inserção do cliente: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void listarPedidos(ListarPedidosRequest req, StreamObserver<ListarPedidosResponse> responseObserver) {
        ListarPedidosResponse reply = ListarPedidosResponse.newBuilder().setMessage("Inserção do cliente: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarPedido(ApagarPedidoRequest req, StreamObserver<ApagarPedidoResponse> responseObserver) {
        ApagarPedidoResponse reply = ApagarPedidoResponse.newBuilder().setMessage("Inserção do cliente: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
