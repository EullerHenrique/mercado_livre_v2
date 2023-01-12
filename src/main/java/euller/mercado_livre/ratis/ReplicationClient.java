package euller.mercado_livre.ratis;

import org.apache.ratis.client.RaftClient;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.*;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ReplicationClient {

  public String exec(String function, String key, String value)
      throws IOException, InterruptedException, ExecutionException {
    String raftGroupId = "raft_group____um"; // 16 caracteres.

    Map<String, InetSocketAddress> id2addr = new HashMap<>();
    id2addr.put("p1", new InetSocketAddress("127.0.0.1", 3000));
    id2addr.put("p2", new InetSocketAddress("127.0.0.1", 3500));
    id2addr.put("p3", new InetSocketAddress("127.0.0.1", 4000));

    List<RaftPeer> addresses =
        id2addr.entrySet().stream()
            .map(e -> RaftPeer.newBuilder().setId(e.getKey()).setAddress(e.getValue()).build())
            .collect(Collectors.toList());

    final RaftGroup raftGroup =
        RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)), addresses);
    RaftProperties raftProperties = new RaftProperties();

    RaftClient client =
        RaftClient.newBuilder()
            .setProperties(raftProperties)
            .setRaftGroup(raftGroup)
            .setClientRpc(
                new GrpcFactory(new Parameters())
                    .newRaftClientRpc(ClientId.randomId(), raftProperties))
            .build();

    RaftClientReply getValue;
    CompletableFuture<RaftClientReply> compGetValue;
    String response = null;
    switch (function) {
      case "add":
        value = value.replace(":", ".");
        getValue = client.io().send(Message.valueOf("add:" + key + ":" +value));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "getAdmin":
        getValue = client.io().sendReadOnly(Message.valueOf("getAdmin:" + key));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "getClient":
        getValue = client.io().sendReadOnly(Message.valueOf("getClient:" + key + ":" + value));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "delAdmin":
        getValue = client.io().send(Message.valueOf("delAdmin:" + key));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "delClient":
        getValue = client.io().send(Message.valueOf("delClient:" + key + ":" + value));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      default:
        System.out.println("comando inválido");
    }
    client.close();

    if(response.split(":")[1].equals("null")){
      return null;
    }else{
      return response.split(":")[1].replace(".", ":");
    }

  }
}
