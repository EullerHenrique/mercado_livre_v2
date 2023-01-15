package euller.mercado_livre.server.admin.service;

import com.google.gson.Gson;
import euller.mercado_livre.server.admin.*;
import euller.mercado_livre.server.admin.model.Produto;
import euller.mercado_livre.server.admin.repository.ProdutoRepository;
import euller.mercado_livre.server.admin.service.mosquitto.MosquittoService;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

public class ProdutoServiceImpl extends ProdutoServiceGrpc.ProdutoServiceImplBase {

    private final ProdutoRepository produtoRepository = new ProdutoRepository();

    public ProdutoServiceImpl() {
        try {
            MosquittoService mosquittoService = new MosquittoService();
            mosquittoService.subscribeProduto("server/cliente/produto/buscar", "server/admin/produto/buscar", produtoRepository);
            mosquittoService.subscribeProduto("server/admin/produto/criar", "", produtoRepository);
            mosquittoService.subscribeProduto("server/admin/produto/modificar", "", produtoRepository);
            mosquittoService.subscribeProduto("server/cliente/produto/modificar", "", produtoRepository);
            mosquittoService.subscribeProduto("server/admin/produto/apagar", "", produtoRepository);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void criarProduto(CriarProdutoRequest req, StreamObserver<CriarProdutoResponse> responseObserver) {
        Gson gson = new Gson();
        Produto produto = gson.fromJson(req.getDados(), Produto.class);
        produto.setPID(UUID.randomUUID().toString());
        String produtoJson = produtoRepository.criarProduto(produto);
        if(produtoJson == null){
            produtoJson = "Produto já existe";
        }
        CriarProdutoResponse reply = CriarProdutoResponse.newBuilder().setMessage(produtoJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarProduto(ModificarProdutoRequest req, StreamObserver<ModificarProdutoResponse> responseObserver) {
        Gson gson = new Gson();
        Produto produto = gson.fromJson(req.getDados(), Produto.class);
        String produtoJson = produtoRepository.modificarProduto(produto);
        if(produtoJson == null) {
         produtoJson = "Produto não encontrado";
        }
        ModificarProdutoResponse reply = ModificarProdutoResponse.newBuilder().setMessage(produtoJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void buscarProduto(BuscarProdutoRequest req, StreamObserver<BuscarProdutoResponse> responseObserver) {
        String produtoJson = produtoRepository.buscarProduto(req.getPID());
        if(produtoJson == null) {
            produtoJson = "Produto não encontrado";
        }
        BuscarProdutoResponse reply =   BuscarProdutoResponse.newBuilder().setMessage(produtoJson).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarProduto(ApagarProdutoRequest req, StreamObserver<ApagarProdutoResponse> responseObserver) {
        String msg = produtoRepository.apagarProduto(req.getPID(), false);
        if(msg == null) {
            msg = "Produto não encontrado";
        }
        ApagarProdutoResponse reply = ApagarProdutoResponse.newBuilder().setMessage(msg).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}

