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
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ClientRatis {

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
    String response = null;
    switch (function) {
      case "addCliente":
        value = value.replace(":", ".");
        getValue = client.io().send(Message.valueOf("addCliente:" + "cliente->"+key + ":" +value));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "addProduto":
        value = value.replace(":", ".");
        getValue = client.io().send(Message.valueOf("addProduto:" + "produto->"+key + ":" +value));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "addPedido":
        value = value.replace(":", ".");
        getValue = client.io().send(Message.valueOf("addPedido:" + "pedido->"+key + ":" +value));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "getCliente":
        getValue = client.io().sendReadOnly(Message.valueOf("getCliente:" + "cliente->"+key));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "getProduto":
        getValue = client.io().sendReadOnly(Message.valueOf("getProduto:" + "produto->"+key));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "getPedido":
        getValue = client.io().sendReadOnly(Message.valueOf("getPedido:" + key + ":" + value));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "delCliente":
        getValue = client.io().send(Message.valueOf("delCliente:" + "cliente->"+key));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "delProduto":
        getValue = client.io().send(Message.valueOf("delProduto:" + "produto->"+key));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      case "delPedido":
        getValue = client.io().send(Message.valueOf("delPedido:" +  key + ":" + value));
        response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        break;
      default:
        System.out.println("comando inválido");
    }
    client.close();
    if(response != null) {
      if (!response.split(":")[1].equals("null")) {
        return response.split(":")[1].replace(".", ":");
      }
    }
    return null;
  }
}
