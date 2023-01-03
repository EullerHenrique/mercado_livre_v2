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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

public class MaquinaDeEstados extends BaseStateMachine {
  private final Options options = new Options();
  public String buscarPedido(String[] opKey, int type) {
    DB levelDB;
    try {
      levelDB = factory.open(new File("src/main/resources/db/cliente/" + this.getLifeCycle().toString().split(":")[1]), options);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    StringBuilder result = new StringBuilder("");
    levelDB.iterator().forEachRemaining(entry -> {
      byte[] key = entry.getKey();
      byte[] value = entry.getValue();
      String keyString = new String(key, StandardCharsets.UTF_8);
      String valueString = new String(value, StandardCharsets.UTF_8);
      valueString = keyString + "--" + valueString.replace(":", ".");
      result.append(valueString).append(";");
    });

    try {
      levelDB.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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

  @Override
  public CompletableFuture<Message> query(Message request) {
    String result = buscarPedido(request.getContent().toString(Charset.defaultCharset()).split(":"), 1);
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
        result += null;
        value = value.replace(".", ":");
        levelDB.put(bytes(key), bytes(value));
        break;
      case "del":
        result += null;
        String keyString = buscarPedido(opKeyValue, 2);
        levelDB.delete(keyString.getBytes());
        break;
      case "clear":
        //key2values.clear();
        result += ":ok";
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
