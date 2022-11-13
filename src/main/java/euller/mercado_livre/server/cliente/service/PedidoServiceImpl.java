package euller.mercado_livre.server.cliente.service;

import euller.mercado_livre.server.cliente.*;
import euller.mercado_livre.server.cliente.respository.PedidoRepository;
import io.grpc.stub.StreamObserver;

import java.util.Hashtable;
import java.util.List;
import java.util.UUID;


public class PedidoServiceImpl extends PedidoServiceGrpc.PedidoServiceImplBase {
    PedidoRepository pedidoRepository = new PedidoRepository();

    @Override
    public void criarPedido(CriarPedidoRequest req, StreamObserver<CriarPedidoResponse> responseObserver) {
        String OID = UUID.randomUUID().toString();
        String pedido = pedidoRepository.criarPedido(req.getCID(), OID, req.getDados());
        CriarPedidoResponse reply = CriarPedidoResponse.newBuilder().setMessage("Response: CID: " + req.getCID() + " OID: " + OID + " Pedido: " + pedido).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarPedido(ModificarPedidoRequest req, StreamObserver<ModificarPedidoResponse> responseObserver) {
        String pedido = pedidoRepository.modificarPedido(req.getCID(), req.getOID(), req.getDados());
        ModificarPedidoResponse reply = ModificarPedidoResponse.newBuilder().setMessage("Response: CID: " + req.getCID() + " OID: "+  req.getOID() + " Pedido: " + pedido).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void listarPedido(ListarPedidoRequest req, StreamObserver<ListarPedidoResponse> responseObserver) {
        String pedido = pedidoRepository.listarPedido(req.getCID() , req.getOID());
        ListarPedidoResponse reply = ListarPedidoResponse.newBuilder().setMessage("Response:  CID: " + req.getCID() + " OID: " + req.getOID() + " Pedido: "+ pedido).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void listarPedidos(ListarPedidosRequest req, StreamObserver<ListarPedidosResponse> responseObserver) {
        List<Hashtable<String, String>> pedidos = pedidoRepository.listarPedidos(req.getCID());
        ListarPedidosResponse reply = ListarPedidosResponse.newBuilder().setMessage("Response: CID: " + req.getCID() + pedidos).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarPedido(ApagarPedidoRequest req, StreamObserver<ApagarPedidoResponse> responseObserver) {
        String pedido = pedidoRepository.apagarPedido(req.getCID(), req.getOID());
        ApagarPedidoResponse reply = ApagarPedidoResponse.newBuilder().setMessage("Response: CID: " + req.getCID() + pedido).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }



}
