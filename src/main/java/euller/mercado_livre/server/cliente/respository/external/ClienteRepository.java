package euller.mercado_livre.server.cliente.respository.external;

import euller.mercado_livre.server.cliente.config.ratis.ClienteRatis;
import euller.mercado_livre.server.cliente.service.mosquitto.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ClienteRepository {

    private final MosquittoService mosquittoService = new MosquittoService();

    public String verificarSeClienteExiste(String CID) throws MqttException {
        return mosquittoService.verificarSeClienteExiste(CID);
    }

}
