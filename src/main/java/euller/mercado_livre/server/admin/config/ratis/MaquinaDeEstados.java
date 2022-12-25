package euller.mercado_livre.server.admin.config.ratis;

import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.iq80.leveldb.DB;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import org.iq80.leveldb.Options;

public class MaquinaDeEstados extends BaseStateMachine {
  private final Options options = new Options();

  @Override
  public CompletableFuture<Message> query(Message request) {
    DB levelDB;
    try {
      levelDB = factory.open(new File("src/main/resources/db/"+this.getLifeCycle().toString().split(":")[1]), options);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String[] opKey = request.getContent().toString(Charset.defaultCharset()).split(":");
    byte[] value = levelDB.get(bytes(opKey[1]));
    String result;
    if(value == null){
      result = opKey[0] + ":" + null;
    }else{
      result = opKey[0] + ":" + new String(value, StandardCharsets.UTF_8);;
    }
    LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
    try {
      levelDB.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return CompletableFuture.completedFuture(Message.valueOf(result));
  }

  @Override
  public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
    DB levelDB;
    try {
      levelDB = factory.open(new File("src/main/resources/db/"+this.getLifeCycle().toString().split(":")[1]), options);
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
        levelDB.put(bytes(key), bytes(value));
        break;
      case "del":
        //result += key2values.remove(key);
        result += null;
        levelDB.delete(key.getBytes());
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
