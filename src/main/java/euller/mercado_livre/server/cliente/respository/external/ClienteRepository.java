package euller.mercado_livre.server.cliente.respository.external;

import euller.mercado_livre.server.cliente.service.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ClienteRepository {

    private final MosquittoService mosquittoService = new MosquittoService();

    public String verificarSeClienteExiste(String CID) throws MqttException {
        return mosquittoService.verificarSeClienteExiste(CID);
    }

}
