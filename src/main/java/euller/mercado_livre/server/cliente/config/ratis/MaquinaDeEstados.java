package euller.mercado_livre.server.cliente.config.ratis;

import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

public class MaquinaDeEstados extends BaseStateMachine {
  private final Options options = new Options();
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

  @Override
  public CompletableFuture<Message> query(Message request) {
    DB levelDB;
    try {
      levelDB = factory.open(new File("src/main/resources/db/cliente/" + this.getLifeCycle().toString().split(":")[1]), options);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String result;
    String[] opKey = request.getContent().toString(Charset.defaultCharset()).split(":");
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
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return CompletableFuture.completedFuture(Message.valueOf("get:"+result));
  }

  @Override
  public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
    DB levelDB;
    try {
      levelDB = factory.open(new File("src/main/resources/db/cliente/"+this.getLifeCycle().toString().split(":")[1]), options);
    } catch (IOException e) {
      throw new RuntimeException(e);
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
        value = value.replace(".", ":");
        levelDB.put(bytes(key), bytes(value));
        result += null;
        break;
      case "del":
        String keyString = buscarPedido(levelDB, opKeyValue, 2);
        if(keyString != null) {
          levelDB.delete(bytes(keyString));
          result += "Pedido apagado";
        }else {
          result += null;
        }
        break;
      default:
        result += "invalid-op";
    }
    final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));

    final RaftProtos.RaftPeerRole role = trx.getServerRole();
    LOG.info("{}:{} {} {}={}", role, getId(), op, key, value);

    try {
      levelDB.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return f;
  }
}
