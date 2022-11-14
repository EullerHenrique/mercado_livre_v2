package euller.mercado_livre.server.admin.repository;

import euller.mercado_livre.server.admin.service.MosquittoService;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.util.Hashtable;

public class ClienteRepository {
    private final Hashtable<String, String> clientes = new Hashtable<>();
    private final MosquittoService mosquittoService = new MosquittoService();

    public String criarCliente(String CID, String dados, boolean otherServerUpdate) {
        System.out.println("Criando cliente: " + CID);
        System.out.println("Dados: " + dados);
        clientes.put(CID, dados);
        if(!otherServerUpdate) {
            try {
                mosquittoService.publish("portal/client/cliente/criar", CID + " , " + buscarCliente(CID));
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
        return "CID: " + CID + " Cliente: "+ buscarCliente(CID);
    }

    public String modificarCLiente(String CID, String dados, boolean otherServerUpdate) {
        if(clientes.containsKey(CID)){
            clientes.remove(CID);
            clientes.put(CID, dados);
            if(!otherServerUpdate) {
                try {
                    mosquittoService.publish("portal/client/cliente/modificar", CID + " , " + buscarCliente(CID));
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
            return buscarCliente(CID);
        }
        return " Cliente não encontrado";
    }

    public String buscarCliente(String CID){
        if(clientes.containsKey(CID)) {
            return clientes.get(CID);
        }
        return " Cliente não encontrado";
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
                    mosquittoService.publish("portal/client/cliente/apagar", CID);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
            return " Cliente apagado";
        }
        return " Cliente não encontrado";
    }


}
