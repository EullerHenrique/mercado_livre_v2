package euller.mercado_livre.server.admin.repository;

import com.google.gson.Gson;
import euller.mercado_livre.server.admin.model.Cliente;
import euller.mercado_livre.server.admin.service.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.util.Hashtable;

public class ClienteRepository {
    private final Hashtable<String, String> clientes = new Hashtable<>();
    private final MosquittoService mosquittoService = new MosquittoService();

    public String criarCliente(Cliente cliente, boolean otherServerUpdate) {
        String CID = cliente.getCID();
        System.out.println("Criando cliente: " + CID);
        Gson gson = new Gson();
        String clienteJson = gson.toJson(cliente);
        System.out.println("Dados: " + clienteJson);
        clientes.put(CID, clienteJson);
        if(!otherServerUpdate) {
            try {
                mosquittoService.publish("portal/admin/cliente/criar", buscarCliente(CID));
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
        return "CID: " + CID + " Cliente: "+ buscarCliente(CID);
    }

    public String modificarCLiente(Cliente cliente, boolean otherServerUpdate) {
        String CID = cliente.getCID();
        Gson gson = new Gson();
        String clienteJson = gson.toJson(cliente);
        if(clientes.containsKey(CID)){
            clientes.remove(CID);
            clientes.put(CID, clienteJson);
            if(!otherServerUpdate) {
                try {
                    mosquittoService.publish("portal/admin/cliente/modificar", buscarCliente(CID));
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
            return buscarCliente(CID);
        }
        return null;
    }

    public String buscarCliente(String CID){
        if(clientes.containsKey(CID)) {
            return clientes.get(CID);
        }
        return null;
    }

    public String isCliente(String CID){
        if(clientes.containsKey(CID)) {
            return "true";
        }
        return "false";
    }

    public String apagarCliente(String CID, boolean otherServerUpdate){
        if (clientes.containsKey(CID)) {
            clientes.remove(CID);
            if(!otherServerUpdate) {
                try {
                    mosquittoService.publish("portal/admin/cliente/apagar", CID);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
            return " Cliente apagado";
        }
        return null;
    }


}
