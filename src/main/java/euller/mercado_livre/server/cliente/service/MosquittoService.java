package euller.mercado_livre.server.cliente.service;
import org.eclipse.paho.client.mqttv3.*;
import java.util.UUID;

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

    public void subscribeClient(String topic) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        client.subscribe(topic, (topic1, message) -> {
            System.out.println("TOPIC: "+topic1);
            System.out.println("MSG: "+ new String(message.getPayload()));
            setCIDExistis(new String(message.getPayload()));
        });
    }

    public void subscribeProduto(String topic) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        client.subscribe(topic, (topic1, message) -> {
            System.out.println("TOPIC: "+topic1);
            System.out.println("MSG: "+ new String(message.getPayload()));
            setPIDExistis(new String(message.getPayload()));
        });
    }

    public volatile String CIDExistis = null;
    public void setCIDExistis(String cidExistis){
        this.CIDExistis = cidExistis;
    }
    public boolean verifyIfCIDExistis(String content) throws MqttException {
        CIDExistis = null;
        publish("portal/client/CID", content);
        subscribeClient("portal/admin/CID");
        while(CIDExistis == null){
            System.out.println("teste");
        }
        return Boolean.parseBoolean(CIDExistis);
    }

    public volatile String PIDExistis = null;
    public void setPIDExistis(String pidExistis){
        this.PIDExistis = pidExistis;
    }
    public boolean verifyIfPIDExistis(String content) throws MqttException {
        PIDExistis = null;
        publish("portal/client/PID", content);
        subscribeProduto("portal/admin/PID");
        while(PIDExistis == null){
            System.out.println("teste");
        }
        return Boolean.parseBoolean(PIDExistis);
    }

}

