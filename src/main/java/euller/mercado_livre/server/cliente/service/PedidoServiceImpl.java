package euller.mercado_livre.server.cliente.service;

import com.google.gson.Gson;
import euller.mercado_livre.server.cliente.model.Pedido;
import euller.mercado_livre.server.cliente.*;
import euller.mercado_livre.server.cliente.respository.PedidoRepository;
import euller.mercado_livre.server.cliente.service.mosquitto.MosquittoService;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Hashtable;
import java.util.List;
import java.util.UUID;


public class PedidoServiceImpl extends PedidoServiceGrpc.PedidoServiceImplBase {
    PedidoRepository pedidoRepository = new PedidoRepository();

    public PedidoServiceImpl() {
        try {
            MosquittoService mosquittoService = new MosquittoService();
            mosquittoService.subscribePedido("server/cliente/pedido/apagar", pedidoRepository);
        }catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

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
        if(pedidoJson == null){
            pedidoJson = "Pedido não encontrado";
        }
        ModificarPedidoResponse reply = ModificarPedidoResponse.newBuilder().setMessage(pedidoJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void buscarPedido(BuscarPedidoRequest req, StreamObserver<BuscarPedidoResponse> responseObserver) {
        String pedidoJson = pedidoRepository.buscarPedido(req.getCID(),req.getOID());
        if(pedidoJson == null){
            pedidoJson = "Pedido não encontrado";
        }
        BuscarPedidoResponse reply = BuscarPedidoResponse.newBuilder().setMessage(pedidoJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void buscarPedidos(BuscarPedidosRequest req, StreamObserver<BuscarPedidosResponse> responseObserver) {
        List<Hashtable<String, Integer>> pedidos = pedidoRepository.buscarPedidos(req.getCID());
        BuscarPedidosResponse reply;
        if(pedidos == null){
            reply = BuscarPedidosResponse.newBuilder().setMessage("O cliente"+req.getCID()+" não possui pedidos").build();
        }else{
            reply = BuscarPedidosResponse.newBuilder().setMessage(req.getCID()+":"+pedidos).build();
        }
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void apagarPedido(ApagarPedidoRequest req, StreamObserver<ApagarPedidoResponse> responseObserver) {
        String pedidoJson = pedidoRepository.apagarPedido(req.getCID(), req.getOID());
        if(pedidoJson == null){
            pedidoJson = "Pedido não encontrado";
        }
        ApagarPedidoResponse reply = ApagarPedidoResponse.newBuilder().setMessage(pedidoJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }


}
