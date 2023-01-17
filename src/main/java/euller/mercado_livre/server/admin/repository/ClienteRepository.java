package euller.mercado_livre.server.admin.repository;

import com.google.gson.Gson;
import euller.mercado_livre.ratis.ClientRatis;
import euller.mercado_livre.server.admin.model.Cliente;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class ClienteRepository {

    private final Logger logger = Logger.getLogger(ClienteRepository.class.getName());
    private final Hashtable<String, String> clientes = new Hashtable<>();
    private final ClientRatis clientRatis = new ClientRatis();

    public void salvarCLienteNoCache(String CID, String clienteJson){
        clientes.put(CID, clienteJson);
        System.out.println("Cliente salvo no cache");
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            System.out.println("A vida útil do cache se esgotou!");
            apagarCliente(CID, false);
        };
        executor.schedule(task, 60, java.util.concurrent.TimeUnit.SECONDS);
    }

    public String criarCliente(Cliente cliente) {
        logger.info("Criando cliente: "+cliente+"\n");
        String CID = cliente.getCID();
        try {
            if (buscarCliente(CID) == null) {
                Gson gson = new Gson();
                String clienteJson = gson.toJson(cliente);
                clientRatis.exec("addCliente", CID, clienteJson);
                System.out.println("Cliente salvo no database");
                return buscarCliente(CID);
            }
        } catch (Exception e) {
            logger.info("Erro ao buscar o cliente no database: "+e.getMessage()+"\n");
        }
        return null;
    }

    public String modificarCLiente(Cliente cliente) {
        logger.info("Modificando cliente: "+cliente+"\n");
        String CID = cliente.getCID();
        if(buscarCliente(CID) != null){
            if(apagarCliente(CID,true) != null){
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
                String clienteJson = clientRatis.exec("getCliente", CID, null);
                if(clienteJson != null){
                    System.out.println("Cliente encontrado no database");

                    //Save Of Cache and delete after 1 minute
                    salvarCLienteNoCache(CID, clienteJson);

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

    public String apagarCliente(String CID, boolean deleteOfDatabase) {
        logger.info("Apagando cliente: " + CID + "\n");

        boolean isDeleteCache = false;
        boolean isDeleteDatabase = false;

        //Delete Of Cache
        if(clientes.containsKey(CID)) {
            clientes.remove(CID);
            System.out.println("Cliente apagado do cache");
            isDeleteCache = true;
        }

        if(deleteOfDatabase) {
            //Delete of Database
            try {
                if (clientRatis.exec("delCliente", CID, null) != null) {
                    System.out.println("Cliente apagado do database");
                    isDeleteDatabase = true;
                }
            } catch (Exception e) {
                logger.info("Erro ao apagar o cliente do database: " + e.getMessage() + "\n");
            }
        }

        if(isDeleteCache || isDeleteDatabase) {
            return "Cliente apagado";
        }

        return null;
    }
}
