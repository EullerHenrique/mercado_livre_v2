package euller.mercado_livre.server.admin.service;

import com.google.gson.Gson;
import euller.mercado_livre.server.admin.*;
import euller.mercado_livre.server.admin.model.Cliente;
import euller.mercado_livre.server.admin.model.Produto;
import euller.mercado_livre.server.admin.repository.ProdutoRepository;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

public class ProdutoServiceImpl extends ProdutoServiceGrpc.ProdutoServiceImplBase {

    private final ProdutoRepository produtoRepository = new ProdutoRepository();

    public ProdutoServiceImpl() {
        try {
            MosquittoService mosquittoService = new MosquittoService();
            mosquittoService.subscribeProduto("portal/client/PID/1", "portal/admin/PID/1", produtoRepository);
            mosquittoService.subscribeProduto("portal/client/PID/2", "", produtoRepository);
            mosquittoService.subscribeProduto("portal/admin/produto/criar", "", produtoRepository);
            mosquittoService.subscribeProduto("portal/admin/produto/modificar", "", produtoRepository);
            mosquittoService.subscribeProduto("portal/admin/produto/apagar", "", produtoRepository);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void criarProduto(CriarProdutoRequest req, StreamObserver<CriarProdutoResponse> responseObserver) {
        Gson gson = new Gson();
        Produto produto = gson.fromJson(req.getDados(), Produto.class);
        produto.setPID(UUID.randomUUID().toString());
        String cliente = produtoRepository.criarProduto(produto, false);
        CriarProdutoResponse reply = CriarProdutoResponse.newBuilder().setMessage(cliente).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarProduto(ModificarProdutoRequest req, StreamObserver<ModificarProdutoResponse> responseObserver) {
        Gson gson = new Gson();
        Produto produto = gson.fromJson(req.getDados(), Produto.class);
        String produtoJson = produtoRepository.modificarProduto(produto, false);
        ModificarProdutoResponse reply = ModificarProdutoResponse.newBuilder().setMessage("PID: " + req.getPID() + " Produto: "+produtoJson).build();
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
        String msg = produtoRepository.apagarProduto(req.getPID(), false);
        ApagarProdutoResponse reply = ApagarProdutoResponse.newBuilder().setMessage("PID: " + req.getPID() + " MSG:: "+msg).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}

