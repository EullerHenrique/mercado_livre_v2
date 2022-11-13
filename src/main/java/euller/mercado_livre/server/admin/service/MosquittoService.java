package euller.mercado_livre.server.admin.service;

import euller.mercado_livre.server.admin.repository.ClienteRepository;
import euller.mercado_livre.server.admin.repository.ProdutoRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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

    public void subscribeCliente(String topic, ClienteRepository clienteRepository) throws MqttException {
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
            System.out.println("STATUS: "+clienteRepository.isCliente(new String(message.getPayload())));
            publish("portal/admin/CID", clienteRepository.isCliente(new String(message.getPayload())));
        });
    }

    public void subscribeProduto(String topicFrom, String topicTo, ProdutoRepository produtoRepository, int method) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        client.subscribe(topicFrom, (topic1, message) -> {
            System.out.println("TOPIC: "+topic1);
            System.out.println("MSG: "+ new String(message.getPayload()));
            System.out.println("STATUS: "+produtoRepository.isProduto(new String(message.getPayload())));
            System.out.println("PRODUTO: "+produtoRepository.buscarProduto(new String(message.getPayload())));
            if(method == 1){
                publish(topicTo, produtoRepository.buscarProduto(new String(message.getPayload())));
            }else if(method == 2){
                String mensagem = new String(message.getPayload());
                String[] pidProduto = mensagem.split(" , ");
                String pid = pidProduto[0];
                String produto = pidProduto[1];
                produtoRepository.modificarProduto(pid, produto);
                //publish(topicTo, produtoRepository.modificarProduto(pid, produto));
            }
        });
    }

}

