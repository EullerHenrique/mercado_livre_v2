package euller.mercado_livre.server.admin.service;

import euller.mercado_livre.server.admin.*;
import io.grpc.stub.StreamObserver;

public class ProdutoServiceImpl extends ProdutoServiceGrpc.ProdutoServiceImplBase {

    @Override
    public void inserirProduto(InserirProdutoRequest req, StreamObserver<InserirProdutoResponse> responseObserver) {
        InserirProdutoResponse reply = InserirProdutoResponse.newBuilder().setMessage("Inserção do clientsdfe: " + req.getPID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void modificarProduto(ModificarProdutoRequest req, StreamObserver<ModificarProdutoResponse> responseObserver) {
        ModificarProdutoResponse reply = ModificarProdutoResponse.newBuilder().setMessage("Inserção do cliesdfnte: " + req.getPID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void recuperarProduto(RecuperarProdutoRequest req, StreamObserver<RecuperarProdutoResponse> responseObserver) {
        RecuperarProdutoResponse reply = RecuperarProdutoResponse.newBuilder().setMessage("Inserção do cliesdfnte: " + req.getPID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
    @Override
    public void apagarProduto(ApagarProdutoRequest req, StreamObserver<ApagarProdutoResponse> responseObserver) {
        ApagarProdutoResponse reply = ApagarProdutoResponse.newBuilder().setMessage("Inserção do clisfdente: " + req.getPID()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}

