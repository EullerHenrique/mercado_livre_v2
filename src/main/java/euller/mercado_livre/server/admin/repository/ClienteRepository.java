package euller.mercado_livre.server.admin.repository;

import java.util.Hashtable;
import java.util.UUID;

public class ClienteRepository {
    private final Hashtable<String, String> clientes = new Hashtable<>();

    public String criarCliente(String dados) {
        String CID = UUID.randomUUID().toString();
        clientes.put(CID, dados);
        return "CID: " + CID + " Cliente: "+ buscarCliente(CID);
    }

    public String modificarCLiente(String CID, String dados){
        if(clientes.containsKey(CID)){
            clientes.remove(CID);
            clientes.put(CID, dados);
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

    public String apagarCliente(String CID){
        if (clientes.containsKey(CID)) {
            clientes.remove(CID);
            return " Cliente apagado";
        }
        return " Cliente não encontrado";
    }


}
