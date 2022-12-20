package euller.mercado_livre.server.admin.repository;

import com.google.gson.Gson;
import euller.mercado_livre.server.admin.config.ratis.ClienteRatis;
import euller.mercado_livre.server.admin.model.Cliente;

import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ClienteRepository {

    private final Logger logger = Logger.getLogger(ClienteRepository.class.getName());

    private final ClienteRatis clienteRatis = new ClienteRatis();

    public String criarCliente(Cliente cliente) {
        logger.info("Criando cliente: "+cliente+"\n");
        String CID = cliente.getCID();
        try {
            if (buscarCliente(CID) == null) {
                Gson gson = new Gson();
                String clienteJson = gson.toJson(cliente);
                clienteRatis.clienteRatis("add", CID, clienteJson);
                return clienteJson;
            }
            return null;
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String modificarCLiente(Cliente cliente) {
        logger.info("Modificando cliente: "+cliente+"\n");
        String CID = cliente.getCID();
        if(buscarCliente(CID) !=null){
            if(apagarCliente(CID) != null){
                String clienteJson = criarCliente(cliente);
                if(clienteJson != null){
                    return clienteJson;
                };
            };
        }
        return null;
    }

    public String buscarCliente(String CID){
        logger.info("Buscando cliente: "+CID+"\n");
        try {
            String clienteJson = clienteRatis.clienteRatis("get", CID, null);
            if (clienteJson != null) {
                return clienteJson;
            }
            return null;
        }catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String isCliente(String CID){
        logger.info("Verificando cliente: "+CID+"\n");
        if(buscarCliente(CID) != null){
            return "true";
        }
        return "false";
    }

    public String apagarCliente(String CID) {
        logger.info("Apagando cliente: " + CID + "\n");
        try {
            if (buscarCliente(CID) != null) {
                clienteRatis.clienteRatis("del", CID, null);
                return "Cliente apagado";
            }
            return null;
        }catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
