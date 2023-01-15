package euller.mercado_livre.server.admin.service.mosquitto;

import com.google.gson.Gson;
import euller.mercado_livre.server.admin.model.Cliente;
import euller.mercado_livre.server.admin.model.Produto;
import euller.mercado_livre.server.admin.repository.ClienteRepository;
import euller.mercado_livre.server.admin.repository.ProdutoRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;
import java.util.logging.Logger;

public class MosquittoService {
    private final Logger logger = Logger.getLogger(MosquittoService.class.getName());

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
        logger.info("Publishing topic: "+topic);
        logger.info("Publishing message: "+message+"\n");
        client.publish(topic, message);
        client.disconnect();
    }

    public void subscribeCliente(String topicFrom, String topicTo, ClienteRepository clienteRepository) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        client.subscribe(topicFrom, (topic, message) -> {
            logger.info("Subscribing topic: "+topic);
            logger.info("Subscribing message: "+message+"\n");
            String CID;
            switch (topicFrom) {
                case "server/cliente/cliente/verificar":
                    CID = new String(message.getPayload());
                    publish(topicTo, clienteRepository.isCliente(CID));
                    break;
                case "server/admin/cliente/apagar":
                    CID = new String(message.getPayload());
                    clienteRepository.apagarCliente(CID,true);
                    break;
            }
        });
    }

    public void subscribeProduto(String topicFrom, String topicTo, ProdutoRepository produtoRepository) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        client.subscribe(topicFrom, (topic, message) -> {
            logger.info("Subscribing topic: "+topic);
            logger.info("Subscribing message: "+message+"\n");
            Gson gson = new Gson();
            Produto produto;
            String produtoJson;
            String PID;
            switch (topicFrom){
                case "server/cliente/produto/buscar":
                    PID = new String(message.getPayload());
                    produtoJson = produtoRepository.buscarProduto(PID);
                    if(produtoJson == null){
                        publish(topicTo, "false");
                    }else{
                        publish(topicTo, produtoJson);
                    }
                    break;
                case "server/cliente/produto/modificar":
                    produtoJson = new String(message.getPayload());
                    produto = gson.fromJson(produtoJson, Produto.class);
                    produtoRepository.modificarProduto(produto);
                    break;
                case "server/admin/produto/apagar":
                    PID = new String(message.getPayload());
                    produtoRepository.apagarProduto(PID, true);
                    break;
            }
        });
    }


}

