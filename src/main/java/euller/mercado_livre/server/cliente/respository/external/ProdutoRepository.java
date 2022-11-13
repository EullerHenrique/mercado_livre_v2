package euller.mercado_livre.server.cliente.respository.external;

import euller.mercado_livre.server.cliente.service.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ProdutoRepository {

    private final MosquittoService mosquittoService = new MosquittoService();

    public String buscarProduto(String PID) throws MqttException {
        return mosquittoService.buscarProduto(PID);
    }

    public void modificarProduto(String PID, String dados) throws MqttException {
        mosquittoService.modificarProduto(PID+ " , "+dados);
        mosquittoService.modificarProduto(PID+ " , "+dados);
    }
}
