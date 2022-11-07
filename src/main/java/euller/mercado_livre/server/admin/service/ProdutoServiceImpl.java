package euller.mercado_livre.server.admin.service;

import euller.mercado_livre.server.admin.*;
import euller.mercado_livre.server.admin.repository.ProdutoRepository;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ProdutoServiceImpl extends ProdutoServiceGrpc.ProdutoServiceImplBase {

    private final ProdutoRepository produtoRepository = new ProdutoRepository();

    public ProdutoServiceImpl() {
        try {
            MosquittoService mosquittoService = new MosquittoService();
            mosquittoService.subscribeProduto("portal/client/PID/1", "portal/admin/PID/1", produtoRepository, 1);
            mosquittoService.subscribeProduto("portal/client/PID/2", "portal/admin/PID/2", produtoRepository, 2);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void criarProduto(CriarProdutoRequest req, StreamObserver<CriarProdutoResponse> responseObserver) {
        String cliente = produtoRepository.criarProduto(req.getDados());
        CriarProdutoResponse reply = CriarProdutoResponse.newBuilder().setMessage(cliente).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarProduto(ModificarProdutoRequest req, StreamObserver<ModificarProdutoResponse> responseObserver) {
        String dados = produtoRepository.modificarProduto(req.getPID(), req.getDados());
        ModificarProdutoResponse reply = ModificarProdutoResponse.newBuilder().setMessage("PID: " + req.getPID() + " Produto: "+dados).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void buscarProduto(BuscarProdutoRequest req, StreamObserver<BuscarProdutoResponse> responseObserver) {
        String dados = produtoRepository.buscarProduto(req.getPID());
        BuscarProdutoResponse reply =   BuscarProdutoResponse.newBuilder().setMessage("PID: " + req.getPID() + " Produto: "+dados).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarProduto(ApagarProdutoRequest req, StreamObserver<ApagarProdutoResponse> responseObserver) {
        String msg = produtoRepository.apagarProduto(req.getPID());
        ApagarProdutoResponse reply = ApagarProdutoResponse.newBuilder().setMessage("PID: " + req.getPID() + " MSG:: "+msg).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}

