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
    public String subscribeClient(String topic) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        AtomicReference<String> cliente = new AtomicReference<>();
        client.subscribe(topic, (topic1, message) -> {
            System.out.println("TOPIC: "+topic1);
            System.out.println("MSG: "+ new String(message.getPayload()));
            cliente.set(new String(message.getPayload()));
        });
        while(cliente.get() == null){}
        return cliente.get();
    }
    public boolean verifyIfCIDExistis(String content) throws MqttException {
        publish("portal/client/CID", content);
        return Boolean.parseBoolean(subscribeClient("portal/admin/CID"));
    }
    public String subscribeProduto(String topic) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        AtomicReference<String> produto = new AtomicReference<>();
        client.subscribe(topic, (topic1, message) -> {
            System.out.println("TOPIC: "+topic1);
            System.out.println("MSG: "+ new String(message.getPayload()));
            produto.set(new String(message.getPayload()));
        });
        while(produto.get() == null){}
        return produto.get();
    }
    public String buscarProduto(String content) throws MqttException {
        publish("portal/client/PID/1", content);
        return subscribeProduto("portal/admin/PID/1");
    }
    public String modificarProduto(String content) throws MqttException {
        publish("portal/client/PID/2", content);
        return subscribeProduto("portal/admin/PID/2");
    }

}

