package euller.mercado_livre.server.admin.repository;

import com.google.gson.Gson;
import euller.mercado_livre.ratis.ClientRatis;
import euller.mercado_livre.server.admin.model.Cliente;
import euller.mercado_livre.server.admin.service.mosquitto.MosquittoService;
import java.util.Hashtable;
import java.util.logging.Logger;

public class ClienteRepository {

    private final Logger logger = Logger.getLogger(ClienteRepository.class.getName());
    private final Hashtable<String, String> clientes = new Hashtable<>();
    private final ClientRatis clientRatis = new ClientRatis();

    private final MosquittoService mosquittoService = new MosquittoService();


    public String criarCliente(Cliente cliente) {
        logger.info("Criando cliente: "+cliente+"\n");
        String CID = cliente.getCID();
        try {
            if (buscarCliente(CID) == null) {
                Gson gson = new Gson();
                String clienteJson = gson.toJson(cliente);
                clientRatis.exec("add", CID, clienteJson);
                return clienteJson;
            }
        } catch (Exception e) {
            logger.info("Erro ao buscar o cliente no database: "+e.getMessage()+"\n");
        }
        return null;
    }

    public String modificarCLiente(Cliente cliente) {
        logger.info("Modificando cliente: "+cliente+"\n");
        String CID = cliente.getCID();
        if(buscarCliente(CID) !=null){
            if(apagarCliente(CID,false) != null){
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

        //Get Of Cache
        if(clientes.containsKey(CID)) {
            System.out.println("Cliente encontrado no cache");
            return clientes.get(CID);
        }else {
            System.out.println("Cliente não encontrado no cache");
            //Get Of Database
            try {
                String clienteJson = clientRatis.exec("getAdmin", CID, null);
                if(clienteJson != null){
                    //Save On Cache
                    clientes.put(CID, clienteJson);
                    System.out.println("Cliente encontrado no database");
                }else {
                    System.out.println("Cliente não encontrado no database");
                }
                return clienteJson;
            } catch (Exception e) {
                logger.info("Erro ao buscar o cliente no database: "+e.getMessage()+"\n");
                return null;
            }
        }

    }

    public String isCliente(String CID){
        logger.info("Verificando cliente: "+CID+"\n");
        if(buscarCliente(CID) != null){
            return "true";
        }
        return "false";
    }

    public String apagarCliente(String CID, boolean otherServerUpdate) {
        logger.info("Apagando cliente: " + CID + "\n");

        boolean isDeleteCache = false;
        boolean isDeleteDatabase = false;

        //Delete Of Cache
        if(clientes.containsKey(CID)) {
            clientes.remove(CID);
            System.out.println("Cliente apagado do cache");
            isDeleteCache = true;
        }

        if(!otherServerUpdate) {
            //Delete of Database
            try {
                if (buscarCliente(CID) != null) {
                    if (clientRatis.exec("delAdmin", CID, null) != null) {
                        System.out.println("Cliente apagado do database");
                        isDeleteDatabase = true;
                    }
                }
            } catch (Exception e) {
                logger.info("Erro ao apagar o cliente do database: " + e.getMessage() + "\n");
            }

            //Send Message for the others servers: Delete of Cache
            try {
                mosquittoService.publish("server/admin/cliente/apagar", CID);
                System.out.println("Mensagem 'Delete of cache' enviada para os outros servidores");
            } catch (Exception e) {
                logger.info("Erro ao solicitar que os outros servidores apaguem o cliente do cache " + e.getMessage() + "\n");
            }
        }else{
            System.out.println("O servidor recebeu a mensagem e apagou o cliente do cache");
        }

        if(isDeleteCache || isDeleteDatabase) {
            return "Cliente apagado";
        }

        return null;
    }
}
