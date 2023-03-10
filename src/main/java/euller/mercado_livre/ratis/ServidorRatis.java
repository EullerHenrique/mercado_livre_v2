package euller.mercado_livre.ratis;

import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServidorRatis {

  // Parametros: myId
  public ServidorRatis(String id) throws IOException, InterruptedException {
    String raftGroupId = "raft_group____um"; // 16 caracteres.

    // Setup for node all nodes.
    Map<String, InetSocketAddress> id2addr = new HashMap<>();
    id2addr.put("p1", new InetSocketAddress("127.0.0.1", 3000));
    id2addr.put("p2", new InetSocketAddress("127.0.0.1", 3500));
    id2addr.put("p3", new InetSocketAddress("127.0.0.1", 4000));

    List<RaftPeer> addresses =
        id2addr.entrySet().stream()
            .map(e -> RaftPeer.newBuilder().setId(e.getKey()).setAddress(e.getValue()).build())
            .collect(Collectors.toList());

    RaftPeerId myId = RaftPeerId.valueOf(id);

    if (addresses.stream().noneMatch(p -> p.getId().equals(myId))) {
      System.out.println("Identificador " + id + " é inválido.");
      System.exit(1);
    }

    // Setup for this node.
    final int port = id2addr.get(id).getPort();
    RaftProperties properties = new RaftProperties();
    properties.setInt(GrpcConfigKeys.OutputStream.RETRY_TIMES_KEY, Integer.MAX_VALUE);
    GrpcConfigKeys.Server.setPort(properties, port);
    RaftServerConfigKeys.setStorageDir(
        properties, Collections.singletonList(new File("/tmp/" + myId)));

    // Join the group of processes.
    final RaftGroup raftGroup =
        RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)), addresses);
    RaftServer raftServer =
        RaftServer.newBuilder()
            .setServerId(myId)
            .setStateMachine(new MaquinaDeEstados())
            .setProperties(properties)
            .setGroup(raftGroup)
            .build();
    raftServer.start();
    System.out.println("Servidor Ratis com id " + id + " iniciado  em" + id2addr.get(id));
  }
}
