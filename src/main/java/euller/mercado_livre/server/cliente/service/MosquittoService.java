package euller.mercado_livre.server.cliente.service;
import com.google.gson.Gson;
import euller.mercado_livre.server.admin.repository.ProdutoRepository;
import euller.mercado_livre.server.cliente.model.Pedido;
import euller.mercado_livre.server.cliente.model.Produto;
import euller.mercado_livre.server.cliente.respository.PedidoRepository;
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

    public void subscribePedido(String topicFrom, PedidoRepository pedidoRepository) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttClient client = new MqttClient("tcp://localhost:1883", publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        client.subscribe(topicFrom, (topic, message) -> {
            Gson gson = new Gson();
            Pedido pedido;
            switch (topicFrom){
                case "portal/cliente/pedido/criar":
                    pedido = gson.fromJson(new String(message.getPayload()), Pedido.class);
                    pedidoRepository.criarPedido(pedido, true);
                    break;
                case "portal/cliente/pedido/modificar":
                    pedido = gson.fromJson(new String(message.getPayload()), Pedido.class);
                    pedidoRepository.modificarPedido(pedido, true);
                    break;
                case "portal/cliente/pedido/apagar":
                    pedido = gson.fromJson(new String(message.getPayload()), Pedido.class);
                    pedidoRepository.apagarPedido(pedido.getCID(), pedido.getOID(), true);
                    break;
            }
        });
    }

    public String verificarSeClienteExiste(String cid) throws MqttException {
        publish("server/client/cliente/verificar", cid);
        return subscribe("server/admin/cliente/verificar");
    }
    public String buscarProduto(String content) throws MqttException {
        publish("server/client/produto/buscar", content);
        return subscribe("server/admin/produto/buscar");
    }
    public void modificarProduto(Produto produto) throws MqttException {
        Gson gson = new Gson();
        String produtoJson = gson.toJson(produto);
        publish("portal/client/produto/modificar", produtoJson);
    }

}

