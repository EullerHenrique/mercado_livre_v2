package euller.mercado_livre.server.cliente.service;
import org.eclipse.paho.client.mqttv3.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class MosquittoService {

    public void publish(String topic, String content) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(2);
        client.publish(topic, message);
        client.disconnect();
    }
    public String subscribe(String topic) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        AtomicReference<String> response = new AtomicReference<>();
        client.subscribe(topic, (topic1, message) -> {
            response.set(new String(message.getPayload()));
        });
        while(response.get() == null){}
        return response.get();
    }




    public String verificarSeClienteExiste(String cid) throws MqttException {
        publish("portal/client/CID", cid);
        return subscribe("portal/admin/CID");
    }
    public String buscarProduto(String content) throws MqttException {
        publish("portal/client/PID/1", content);
        return subscribe("portal/admin/PID/1");
    }
    public void modificarProduto(String content) throws MqttException {
        publish("portal/client/PID/2", content);
    }

}

