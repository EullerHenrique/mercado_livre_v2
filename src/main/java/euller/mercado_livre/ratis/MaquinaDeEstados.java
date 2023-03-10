package euller.mercado_livre.ratis;

import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.iq80.leveldb.DB;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import org.iq80.leveldb.Options;

public class MaquinaDeEstados extends BaseStateMachine {
  private final Options options = new Options();
  public String buscarPedido(String[] opKey, int type) {
    StringBuilder result = new StringBuilder("");
    DB levelDB = null;
    while(levelDB == null) {
      try {
        levelDB = factory.open(new File("/tmp/" + this.getLifeCycle().toString().split(":")[1] +"/db/"), options);
        levelDB.iterator().forEachRemaining(entry -> {
          byte[] key = entry.getKey();
          byte[] value = entry.getValue();
          String keyString = new String(key, StandardCharsets.UTF_8);
          String valueString = new String(value, StandardCharsets.UTF_8);
          valueString = keyString + "--" + valueString.replace(":", ".");
          if(keyString.contains("pedido->")) {
            result.append(valueString).append(";");
          }
        });
        levelDB.close();
        break;
      } catch (Exception ignored) {}
    }

    if (result.toString().equals("") || opKey[1] == null || opKey[2] == null) {
      return null;
    } else {
      String[] results = result.toString().split(";");
      for (String r : results) {
        //if r.contains(CID) and r.contains(OID)
        if (r.contains("\"CID\".\""+opKey[1]+"\"") && r.contains("\"OID\".\""+opKey[2]+"\"")) {
          if(type == 1) {
            return r.split("--")[1];
          }else if(type == 2){
            return r.split("--")[0];
          }
        }
      }
    }
    return null;
  }
  public String buscarPedidosPeloCliente(String[] opKey) {
    StringBuilder result = new StringBuilder("");
    DB levelDB = null;
    while(levelDB == null) {
      try {
        levelDB = factory.open(new File("/tmp/" + this.getLifeCycle().toString().split(":")[1] +"/db/"), options);
        levelDB.iterator().forEachRemaining(entry -> {
          byte[] key = entry.getKey();
          byte[] value = entry.getValue();
          String keyString = new String(key, StandardCharsets.UTF_8);
          String valueString = new String(value, StandardCharsets.UTF_8);
          valueString = keyString + "--" + valueString.replace(":", ".");
          if(keyString.contains("pedido->")) {
            result.append(valueString).append(";");
          }
        });
        levelDB.close();
        break;
      } catch (Exception ignored) {}
    }

    if (result.toString().equals("") || opKey[1] == null) {
      return null;
    } else {
      String[] results = result.toString().split(";");
      StringBuilder resultFinal = new StringBuilder("");
      for (String r : results) {
        //if r.contains(CID)
        if (r.contains("\"CID\".\""+opKey[1]+"\"")) {
          resultFinal.append(r.split("--")[1]).append(";");
        }
      }
      return resultFinal.toString();
    }

    
  }


  public CompletableFuture<Message> queryAdmin(String[] opKey){
    DB levelDB = null;
    byte[] value = null;
    while(levelDB == null) {
      try {
        levelDB = factory.open(new File("/tmp/" + this.getLifeCycle().toString().split(":")[1] +"/db/"), options);
        value = levelDB.get(bytes(opKey[1]));
        levelDB.close();
        break;
      } catch (Exception ignored) {}
    }
    String result;
    if(value == null){
      result = opKey[0] + ":" + null;
    }else{
      String valueString = new String(value, StandardCharsets.UTF_8);
      valueString = valueString.replace(":", ".");
      result = opKey[0] + ":" + valueString;;
    }
    LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
    return CompletableFuture.completedFuture(Message.valueOf(result));
  }

  public CompletableFuture<Message> queryClient(String[] opKey, int type){
    String result;
    if(Objects.equals(opKey[2], "cliente")){
      String pedidos = buscarPedidosPeloCliente(opKey);
      if(pedidos != null){
        result = pedidos;
      }else{
        result = "null";
      }
    }else {
      result = buscarPedido(opKey, type);
    }
    return CompletableFuture.completedFuture(Message.valueOf("getClient:"+result));
  }

  @Override
  public CompletableFuture<Message> query(Message request) {
    String[] opKey = request.getContent().toString(Charset.defaultCharset()).split(":");
    switch (opKey[0]) {
      case "getCliente":
      case "getProduto":
        return queryAdmin(opKey);
      case "getPedido":
        return queryClient(opKey, 1);
      case "getKeyPedido":
        return queryClient(opKey, 2);
      default:
        return null;
    }
  }

  @Override
  public CompletableFuture<Message> applyTransaction(TransactionContext trx) {

    final RaftProtos.LogEntryProto entry = trx.getLogEntry();
    final String[] opKeyValue =
        entry.getStateMachineLogEntry().getLogData().toString(Charset.defaultCharset()).split(":");

    DB levelDB = null;

    final String op = opKeyValue[0];
    String result = op + ":";

    String key = opKeyValue.length < 2 ? "" : opKeyValue[1];
    String value = opKeyValue.length < 3 ? "" : opKeyValue[2];
    boolean isPresent = false;
    switch (op) {
      case "addCliente":
      case "addProduto":
      case "addPedido":
        value = value.replace(".", ":");
        while(levelDB == null) {
          try {
            levelDB = factory.open(new File("/tmp/" + this.getLifeCycle().toString().split(":")[1] +"/db/"), options);
            levelDB.put(bytes(key), bytes(value));
            levelDB.close();
            break;
          } catch (Exception ignored) {}
        }
        result += null;
        break;
      case "delCliente":
        try {
          String pedidoJson = query(Message.valueOf("getCliente:" + key)).get().getContent().toString(Charset.defaultCharset());
          if(!pedidoJson.split(":")[1].equals("null")){
            isPresent = true;
          }
        } catch (Exception ignored) {}
        if(isPresent) {
          while (levelDB == null) {
            try {
              levelDB = factory.open(new File("/tmp/" + this.getLifeCycle().toString().split(":")[1] +"/db/"), options);
              levelDB.delete(key.getBytes());
              levelDB.close();
              break;
            } catch (Exception ignored) {
            }
          }
          result += "Cliente apagado";
        }else{
          result += null;
        }
        break;
      case "delProduto":
        try {
          String pedidoJson = query(Message.valueOf("getProduto:" + key)).get().getContent().toString(Charset.defaultCharset());
          if(!pedidoJson.split(":")[1].equals("null")){
            isPresent = true;
          }
        } catch (Exception ignored) {}
        if(isPresent) {
          while (levelDB == null) {
            try {
              levelDB = factory.open(new File("/tmp/" + this.getLifeCycle().toString().split(":")[1] +"/db/"), options);
              levelDB.delete(key.getBytes());
              levelDB.close();
              break;
            } catch (Exception ignored) {}
          }
          result += "Produto apagado";
        }else{
          result += null;
        }
        break;
      case "delPedido":
        String keyString = null;
        try {
          keyString = query(Message.valueOf("getKeyPedido:" + key + ":" + value)).get().getContent().toString(Charset.defaultCharset()).split(":")[1];
          if(keyString.equals("null")){
            keyString = null;
          }
        }catch (Exception ignored) {}

        if(keyString != null) {
          while(levelDB == null) {
            try {
              levelDB = factory.open(new File("/tmp/" + this.getLifeCycle().toString().split(":")[1] +"/db/"), options);
              levelDB.delete(bytes(keyString));
              levelDB.close();
              break;
            } catch (Exception ignored) {}
          }
          result += "Pedido apagado";
        }else {
          result += null;
        }
    }

    final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));

    final RaftProtos.RaftPeerRole role = trx.getServerRole();
    LOG.info("{}:{} {} {}={}", role, getId(), op, key, value);
    return f;
  }



}
