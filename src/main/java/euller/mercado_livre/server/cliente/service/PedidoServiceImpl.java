package euller.mercado_livre.server.cliente.service;

import com.google.gson.Gson;
import euller.mercado_livre.server.cliente.model.Pedido;
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
        String CID = req.getCID();
        String OID = UUID.randomUUID().toString();
        Gson gson = new Gson();
        Pedido pedido = gson.fromJson(req.getDados(), Pedido.class);
        pedido.setCID(CID);
        pedido.setOID(OID);
        String pedidoJson = pedidoRepository.criarPedido(pedido);
        CriarPedidoResponse reply = CriarPedidoResponse.newBuilder().setMessage(pedidoJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarPedido(ModificarPedidoRequest req, StreamObserver<ModificarPedidoResponse> responseObserver) {

        String CID = req.getCID();
        String OID = req.getOID();
        Gson gson = new Gson();
        Pedido pedido = gson.fromJson(req.getDados(), Pedido.class);
        pedido.setCID(CID);
        pedido.setOID(OID);

        String pedidoJson = pedidoRepository.modificarPedido(pedido);
        ModificarPedidoResponse reply = ModificarPedidoResponse.newBuilder().setMessage(pedidoJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void buscarPedido(BuscarPedidoRequest req, StreamObserver<BuscarPedidoResponse> responseObserver) {
        String pedido = pedidoRepository.listarPedido(req.getCID() , req.getOID());
        BuscarPedidoResponse reply = BuscarPedidoResponse.newBuilder().setMessage(pedido).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void buscarPedidos(BuscarPedidosRequest req, StreamObserver<BuscarPedidosResponse> responseObserver) {
        List<Hashtable<String, String>> pedidos = pedidoRepository.listarPedidos(req.getCID());
        BuscarPedidosResponse reply = BuscarPedidosResponse.newBuilder().setMessage("Response: CID: " + req.getCID() + pedidos).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarPedido(ApagarPedidoRequest req, StreamObserver<ApagarPedidoResponse> responseObserver) {
        String pedido = pedidoRepository.apagarPedido(req.getCID(), req.getOID());
        ApagarPedidoResponse reply = ApagarPedidoResponse.newBuilder().setMessage(pedido).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }



}
