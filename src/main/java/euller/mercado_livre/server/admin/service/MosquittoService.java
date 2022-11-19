package euller.mercado_livre.server.admin.service;

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
            Gson gson = new Gson();
            Cliente cliente;
            switch (topicFrom) {
                case "server/client/cliente/verificar":
                    publish(topicTo, clienteRepository.isCliente(new String(message.getPayload())));
                    break;
                case "server/admin/cliente/criar":
                    cliente = gson.fromJson(new String(message.getPayload()), Cliente.class);
                    clienteRepository.criarCliente(cliente, true);
                    break;
                case "server/admin/cliente/modificar":
                    cliente = gson.fromJson(new String(message.getPayload()), Cliente.class);
                    clienteRepository.modificarCLiente(cliente, true);
                    break;
                case "server/admin/cliente/apagar":
                    clienteRepository.apagarCliente(new String(message.getPayload()), true);
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
            switch (topicFrom){
                case "server/client/produto/buscar":
                    String produtoJson = produtoRepository.buscarProduto(new String(message.getPayload()));
                    if(produtoJson == null){
                        publish(topicTo, "false");
                    }else{
                        publish(topicTo, produtoJson);
                    }
                    break;
                case "server/client/produto/modificar":
                    produto = gson.fromJson(new String(message.getPayload()), Produto.class);
                    produtoRepository.modificarProduto(produto, false);
                    break;
                case "server/admin/produto/criar":
                    produto = gson.fromJson(new String(message.getPayload()), Produto.class);
                    produtoRepository.criarProduto(produto, true);
                    break;
                case "server/admin/produto/modificar":
                    produto = gson.fromJson(new String(message.getPayload()), Produto.class);
                    produtoRepository.modificarProduto(produto, true);
                    break;
                case "server/admin/produto/apagar":
                    produtoRepository.apagarProduto(new String(message.getPayload()), true);
                    break;
            }
        });
    }


}

