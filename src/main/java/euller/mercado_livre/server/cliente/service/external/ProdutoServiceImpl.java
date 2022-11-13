package euller.mercado_livre.server.cliente.service.external;

import euller.mercado_livre.server.cliente.*;
import euller.mercado_livre.server.cliente.respository.external.ProdutoRepository;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ProdutoServiceImpl extends ProdutoServiceGrpc.ProdutoServiceImplBase{

    private final ProdutoRepository produtoRepository = new ProdutoRepository();

    @Override
    public void buscarProduto(BuscarProdutoRequest req, StreamObserver<BuscarProdutoResponse> responseObserver) {
        String produto;
        try {
            produto  = produtoRepository.buscarProduto(req.getPID());
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        BuscarProdutoResponse reply = BuscarProdutoResponse.newBuilder().setMessage(produto).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void modificarProduto(ModificarProdutoRequest req, StreamObserver<ModificarProdutoResponse> responseObserver) {
        try {
            produtoRepository.modificarProduto(req.getPID(), req.getDados());
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        ModificarProdutoResponse reply = ModificarProdutoResponse.newBuilder().setMessage("true").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
