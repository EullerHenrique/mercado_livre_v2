package euller.mercado_livre.server.cliente.service;

import euller.mercado_livre.server.cliente.*;
import euller.mercado_livre.server.cliente.respository.PedidoRepository;
import io.grpc.stub.StreamObserver;


public class PedidoServiceImpl extends PedidoServiceGrpc.PedidoServiceImplBase {
    PedidoRepository pedidoRepository = new PedidoRepository();
    @Override
    public void criarPedido(CriarPedidoRequest req, StreamObserver<CriarPedidoResponse> responseObserver) {
        CriarPedidoResponse reply = CriarPedidoResponse.newBuilder().setMessage("Inserção do pedidooooooioi: " + req.getCID()).build();
        pedidoRepository.criarPedido(req.getCID(), req.getDados());
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarPedido(ModificarPedidoRequest req, StreamObserver<ModificarPedidoResponse> responseObserver) {
        ModificarPedidoResponse reply = ModificarPedidoResponse.newBuilder().setMessage("Modificação do pedido: " + req.getCID()).build();
        pedidoRepository.modificarPedido(req.getCID(), req.getDados());
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void listarPedido(ListarPedidoRequest req, StreamObserver<ListarPedidoResponse> responseObserver) {
        String pedido = pedidoRepository.listarPedido("", req.getOID());
        ListarPedidoResponse reply = ListarPedidoResponse.newBuilder().setMessage("Listagem do pedido: " + req.getCID() + pedido).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void listarPedidos(ListarPedidosRequest req, StreamObserver<ListarPedidosResponse> responseObserver) {
        ListarPedidosResponse reply = ListarPedidosResponse.newBuilder().setMessage("Listagem do pedido: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarPedido(ApagarPedidoRequest req, StreamObserver<ApagarPedidoResponse> responseObserver) {
        ApagarPedidoResponse reply = ApagarPedidoResponse.newBuilder().setMessage("Exclusao do pedido: " + req.getCID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
