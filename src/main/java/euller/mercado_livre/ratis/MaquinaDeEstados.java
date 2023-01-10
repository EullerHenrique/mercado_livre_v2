package euller.mercado_livre.ratis;

import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;

import java.io.File;
import java.io.IOException;
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
  private DB levelDB = null;
  public String buscarPedido(DB levelDB, String[] opKey, int type) {
    StringBuilder result = new StringBuilder("");
    levelDB.iterator().forEachRemaining(entry -> {
      byte[] key = entry.getKey();
      byte[] value = entry.getValue();
      String keyString = new String(key, StandardCharsets.UTF_8);
      String valueString = new String(value, StandardCharsets.UTF_8);
      valueString = keyString + "--" + valueString.replace(":", ".");
      result.append(valueString).append(";");
    });

    System.out.println("RESULT: " + result);

    if (result.toString().equals("") || opKey[1] == null || opKey[2] == null) {
      return null;
    } else {
      String[] results = result.toString().split(";");
      for (String r : results) {
        //if r.contains(CID) and r.contains(OID)
        if (r.contains(opKey[1]) && r.contains(opKey[2])) {
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
  public String buscarPedidosPeloCliente(DB levelDB, String[] opKey) {
    StringBuilder result = new StringBuilder("");
    levelDB.iterator().forEachRemaining(entry -> {
      byte[] key = entry.getKey();
      byte[] value = entry.getValue();
      String keyString = new String(key, StandardCharsets.UTF_8);
      String valueString = new String(value, StandardCharsets.UTF_8);
      valueString = keyString + "--" + valueString.replace(":", ".");
      result.append(valueString).append(";");
    });

    System.out.println("RESULT: " + result);
    if (result.toString().equals("") || opKey[1] == null) {
      return null;
    } else {
      String[] results = result.toString().split(";");
      boolean flag = false;
      StringBuilder resultFinal = new StringBuilder("");
      for (String r : results) {
        //if r.contains(CID)
        if (r.contains(opKey[1])) {
          resultFinal.append(r.split("--")[1]).append(";");
        }
      }
      return resultFinal.toString();
    }
  }


  public CompletableFuture<Message> queryAdmin(String[] opKey){
    byte[] value = levelDB.get(bytes(opKey[1]));
    String result;
    if(value == null){
      result = opKey[0] + ":" + null;
    }else{
      String valueString = new String(value, StandardCharsets.UTF_8);
      valueString = valueString.replace(":", ".");
      result = opKey[0] + ":" + valueString;;
    }
    LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
    try {
      levelDB.close();
      levelDB=null;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return CompletableFuture.completedFuture(Message.valueOf(result));
  }

  public CompletableFuture<Message> queryClient(String[] opKey){
    String result;
    if(Objects.equals(opKey[2], "cliente")){
      String pedidos = buscarPedidosPeloCliente(levelDB, opKey);
      if(pedidos != null){
        result = pedidos;
      }else{
        result = "false";
      }
    }else {
      result = buscarPedido(levelDB, opKey, 1);
    }
    try {
      levelDB.close();
      levelDB=null;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return CompletableFuture.completedFuture(Message.valueOf("getClient:"+result));
  }

  @Override
  public CompletableFuture<Message> query(Message request) {
    if(levelDB == null) {
      try {
        levelDB = factory.open(new File("src/main/resources/db/" + this.getLifeCycle().toString().split(":")[1]), options);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    String[] opKey = request.getContent().toString(Charset.defaultCharset()).split(":");
    if(opKey[0].equals("getAdmin")){
      return queryAdmin(opKey);
    }else if(opKey[0].equals("getClient")){
      return queryClient(opKey);
    }else{
      return null;
    }
  }

  @Override
  public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
    if(levelDB == null) {
      try {
        levelDB = factory.open(new File("src/main/resources/db/" + this.getLifeCycle().toString().split(":")[1]), options);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    final RaftProtos.LogEntryProto entry = trx.getLogEntry();
    final String[] opKeyValue =
        entry.getStateMachineLogEntry().getLogData().toString(Charset.defaultCharset()).split(":");

    final String op = opKeyValue[0];
    String result = op + ":";

    String key = opKeyValue.length < 2 ? "" : opKeyValue[1];
    String value = opKeyValue.length < 3 ? "" : opKeyValue[2];
    switch (op) {
      case "add":
        result += null;
        value = value.replace(".", ":");
        levelDB.put(bytes(key), bytes(value));
        break;
      case "delAdmin":
        result += null;
        levelDB.delete(key.getBytes());
        break;
      case "delClient":
        String keyString = buscarPedido(levelDB, opKeyValue, 2);
        if(keyString != null) {
          levelDB.delete(bytes(keyString));
          result += "Pedido apagado";
        }else {
          result += null;
        }
    }
    final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));

    final RaftProtos.RaftPeerRole role = trx.getServerRole();
    LOG.info("{}:{} {} {}={}", role, getId(), op, key, value);
    try {
      levelDB.close();
      levelDB=null;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return f;
  }



}
