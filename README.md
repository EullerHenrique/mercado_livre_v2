# Mercado Livre

## Sumário

- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configuração](#configuração)
- - [Mosquitto](#mosquitto)
- - [Java](#java)
- [Execução](#execução)
  - [Server](#server)   
  - [Client](#client)
- [Tabelas Hash](#tabelas-hash) 
- [Tabelas LevelDB](#tabelas-leveldb)  
- [Funcionalidades](#funcionalidades)
- [Cache](#cache)
- [Critérios Atendidos](#critérios-atendidos)
- [Vídeo De Apresentação](#vídeo-de-apresentação) 
## Tecnologias Utilizadas

- Java
- Grpc
- Mosquitto
- Ratis
- LevelBD

## Configuração

1. Abra o CMD
2. Crie uma pasta
3. Navegue até a pasta criada
4. Digite no CMD: git clone https://github.com/EullerHenrique/mercado_livre_v2

### Mosquitto

1. Abra a pasta mercado_livre
2. Abra o arquivo mosquitto-2.0.15-install-windows-x64.exe
3. Aperte next
4. Selecione visual estudio runtime e service
5. Selecione a pasta mercado_livre como local de instalação
6. Abra o CMD
7. Navegue até a pasta mercado_livre
8. Digite no CMD: mosquitto -v

### Java

1. Abra o intellij 
2. Instale o JAVA 17:  
    1. No canto superior esquerdo, aperte na aba File
    2. Clique em Project Structure
    3. Clique em Project
    4. Clique no Select do SDK
    5. Clique em Add SDK
    6. Clique em Download JDK
    7. Clique em version 
    8. Escolha a versão 17
    9. Clique em download
    10. Clique em Apply
    11. Clique em OK
4. Aperte com o botão direito do mouse na pasta mercado_livre/src/main
5. Clique em Build Module 'mercado_livre.main'

## Execução

### Ratis

- Replicação da máquina de estado
    1. Navegue até mercado_livre/ratis/StartReolication
    2. Aperte o botão play localizado ao lado de "public class Start"
    3. O servidor ratis p1 é criado
    4. O servidor ratis p2 é criado
    5. O servidor ratis p3 é criado
    6. O servidor ratis p1 cria a réplica p1 (porta 3000) da máquina de estado (A pasta será inserida no caminho /tmp/p1)
    7. O servidor ratis p1 cria a réplica p2 (porta 3500) da máquina de estado (A pasta será inserida no caminho /tmp/p2)
    8. O servidor ratis p1 cria a réplica p3 (porta 4000) da máquina de estado (A pasta será inserida no caminho /tmp/p3)
    4. A réplica p1 da máquina de estado cria o database levelDB p1 (A pasta será inserida no caminho /main/resources/db/p1)
    5. A réplica p2 da máquina de estado cria o database levelDB p2 (A pasta será inserida no caminho /main/resources/db/p2)
    6. A réplica p3 da máquina de estado cria o database levelDB p2 (A pasta será inserida no caminho /main/resources/db/p3)
    7. Se a solitaçâo exigir uma mudança de estado (add ou del), o ClientRatis irá solicitar que cada réplica processe essa solicitação -> 8, 9, 10
    8. A réplica p1 da máquina de estado atende a solicitação e envia a resposta para o ClientRatis
    9. A réplica p2 da máquina de estado atende a solicitação e envia a resposta para o ClientRatis
    10. A réplica p3 da máquina de estado atende a solicitação e envia a resposta para o ClientRatis
    11. Se a solicitação não exigir uma mudançã de estado (get), o ClientRatis irá solicitar para qualquer réplica uma resposta -> 12
    12. A réplica p1, p2 ou p3 da máquina de estado atende a solicitação e envia a resposta para o ClientRatis
    13. O ClientRatis envia a resposta recebida para quem a solicitou.
    
###  Server

2. Admin
    1. Navegue até mercado_livre/server/admin/AdminServer
    2. Aperte o botão play localizado ao lado de "public class AdminServer"
    3. Digite a porta desejada (Ex: 5051)
    
3. Cliente
    1. Navegue até mercado_livre/server/cliente/ClienteServer
    2. Aperte o botão play localizado ao lado de "public class ClienteServer"
    3. Digite a porta desejada (Ex: 5052)
    
### Client
     
2. Admin
     1. Navegue até mercado_libre/client/admin/AdminClient
     2. Aperte o botão play localizado ao lado de "public class AdminClient"
     3. Digite a porta escolhida ao criar o AdminServer (Ex: 5052)

3. Cliente
    1. Navegue até mercado_libre/client/cliente/ClientCliente
    2. Aperte o botão play localizado ao lado de "public class ClientCliente"
    3. Digite a porta escolhida ao criar o ClienteServer (Ex: 5052)
    
## Tabelas Hash

1. Cliente
     1. Hashtable<String, String> 
     2. <CID, ClienteJson>
2. Produto
     1. Hashtable<String, String>
     2. <PID, ProdutoJson>
3. Pedido 
     1. Hashtable<String, List<Hashtable<String, List< String >>>> 
     2. <CID<List<OID, List< ProdutoJson >>>>

## Tabelas LevelDB

1. Cliente
     - Key: "cliente->CID" Value: "{CID, nome, email, telefone}"
2. Produto
     - Key: "produto->PID" Value: "{CID, OID, PID, produto, quantidade, preco}"
3. Pedido 
     - Key: "pedido->UUID" Value: "{CID, OID, produtos: List[CID, OID, PID, produto, quantidade, preco]}"

### Funcionalidades

1. Admin

    1. Criar Cliente
        1. AdminClient: Digite o nome do cliente
        2. AdminClient: Digite o email do cliente
        3. AdminClient: Digite o telefone do cliente
        4. AdminClient->Grpc: CriarCliente -> Realiza uma requisição por meio do protocolo rpc
        5. AdminServer->Grpc: CriarCliente -> Recebe uma requisição por meio do protocolo rpc
        6. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (add)
        7. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (add) 
        10. AdminCliente: O cliente criado é exibido
        
    2. Modificar Cliente
        1. AdminCliente: Digite o nome do cliente
        2. AdminCliente: Digite o email do cliente
        3. AdminCliente: Digite o telefone do cliente
        4. AdminCliente->Grpc: ModificarCliente -> Realiza uma requisição por meio do protocolo rpc
        5. AdminServer->Grpc: ModificarCliente -> Recebe uma requisição por meio do protocolo rpc
        6. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (getAdmin:CID)
        7. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getAdmin:CID) e retorna a resposta para o AdminServer
        8. AdminCliente: A mensagem "Cliente não encontrado" é exibida se o get retornar null
        9. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (delAdmin:CID)
        10. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (delAdmin:CID)
        11. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (add)
        12. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (add) 
        13. AdminCliente: O cliente modificado é exibido 
        
    3. Buscar Cliente
        1. AdminCliente: Digite o CID do cliente
        2. AdminCliente->Grpc: BuscarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. AdminServer->Grpc: BuscarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. AdminServer->Ratis:Faz uma solicitação para o ClientRatis (getAdmin:CID)
        5. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getAdmin:CID) e retorna a resposta para o AdminServer
        6. AdminCliente: O cliente buscado é exibido se ele existir
        7. AdminCliente: A mensagem "Cliente não encontrado" é exibida se o get retornar null
        
    4. Apagar Cliente
        1. AdminCliente: Digite o CID do cliente
        2. AdminCliente->Grpc: ApagarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. AdminServer->Grpc: ApagarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (getAdmin:CID)
        5. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getAdmin:CID) e retorna a resposta para o AdminServer
        6. AdminCliente: A mensagem "Cliente não encontrado" é exibida se o get retornar null
        7. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (delAdmin:CID)
        8. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (delAdmin:CID)
        9. AdminCliente: A mensagem "Cliente apagado" é exibida 
        
    5. Criar Produto  
        1. AdminCliente: Digite o nome do produto
        2. AdminCliente: Digite a quantidade do produto
        3. AdminCliente: Digite o preço do produto
        4. AdminCliente->Grpc: CriarProduto -> Realiza uma requisição por meio do protocolo rpc
        5. AdminServer->Grpc: CriarProduto -> Recebe uma requisição por meio do protocolo rpc
        6. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (add)
        7. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (add)
        8. AdminCliente: O produto criado é exibido
        
    6. Modificar Produto
        1. AdminCliente: Digite o PID do produto
        2. AdminCliente: Digite o nome do produto
        3. AdminCliente: Digite a quantidade do produto
        4. AdminCliente: Digite o preço do produto
        5. AdminCliente->Grpc: ModificarProduto -> Realiza uma requisição por meio do protocolo rpc
        6. AdminServer->Grpc: ModificarProduto -> Recebe uma requisição por meio do protocolo rpc
        7. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (getAdmin:CID)
        8. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getAdmin:CID) e retorna a resposta para o AdminServer
        9. AdminCliente: A mensagem "Produto não encontrado" é exibida se o get retornar null
        10. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (delAdmin:CID)
        11. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (delAdmin:CID)
        12. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (add)
        13. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (add) 
        14. AdminCliente: O produto modificado é exibido 
        
    7. Buscar Produto
        1. AdminCliente: Digite o PID do produto
        2. AdminCliente->Grpc: BuscarProduto -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: BuscarProduto -> Recebe uma requisição por meio do protocolo rpc
        4. AdminServer->Ratis:Faz uma solicitação para o ClientRatis (getAdmin:CID)
        5. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getAdmin:CID) e retorna a resposta para o AdminServer
        6. AdminCliente: O produto buscado é exibido se ele existir
        7. AdminCliente: A mensagem "Produto não encontrado" é exibida se o get retornar null
        
    8. Apagar Produto
        1. AdminCliente: Digite o PID do cliente
        2. AdminCliente->Grpc: ApagarProduto -> Realiza uma requisição por meio do protocolo rpc
        3. AdminServer->Grpc: ApagarProduto -> Recebe uma requisição por meio do protocolo rpc
        4. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (getAdmin:CID)
        5. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getAdmin:CID) e retorna a resposta para o AdminServer
        6. AdminCliente: A mensagem "Produto não encontrado" é exibida se o get retornar null
        7. AdminServer->Ratis: Faz uma solicitação para o ClientRatis (delAdmin:CID)
        8. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (delAdmin:CID)
        9. AdminCliente: A mensagem "Produto apagado" é exibida 
       
2. Cliente

    1. Criar Pedido
        1. ClientCliente: Digite o CID
        2. ClienteCliente->Grpc: VerificarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: VerificarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. ClienteServer->Mosquitto: Publica o CID no tópico server/cliente/cliente/verificar 
        5. AdminServer-> Mosquitto: Se subscreve no tópico server/cliente/cliente/verificar
        6. AdminServer-> Verifica se o cliente existe
        7. AdminServer->Mosquitto: Publica a resposta no tópico server/admin/cliente/verificar 
        8. ClienteServer->Mosquitto: Se subscreve no tópico server/admin/cliente/verificar
        9. ClienteServer: A subcrição realizada recebe o que foi publicado 
        10. ClientCliente->Se o clinte existir:
        11. ClientCliente: Digite o PID
        12. ClienteCliente->Grpc: BuscarProduto -> Realiza uma requisição por meio do protocolo rpc
        13. ServerCliente->Grpc: BuscarProduto -> Recebe uma requisição por meio do protocolo rpc
        14. ClienteServer->Mosquitto: Publica o PID no tópico server/cliente/produto/buscar 
        16. AdminServer-> Mosquitto: Se subscreve no tópico server/cliente/produto/buscar
        17. AdminServer-> Busca o produto
        18. AdminServer->Mosquitto: Publica a resposta no tópico server/admin/produto/buscar
        19. ClienteServer->Mosquitto: Se subscreve no tópico server/admin/produto/buscar
        20. ClienteServer: A subcrição realizada recebe o que foi publicado 
        21. ClienteCliente: Se o produto existir:
        22. ClientCliente: Exibe o nome do produto, quantidade disponível e preço
        23. ClientCliente: Digite a quantidade desejada (>0)
        24. ClientCliente: Multiplica a quantidade escolhida pelo preço do produto e salva no objeto ProdutoPedido
        25. ClientCliente: Você deseja adicionar mais pedidos? 
        26. ClientCLiente: Sim -> Volta até: Digite o PID... 
        27. ClientCliente: Não ->  Continua
        28. ClienteCliente->Grpc: CriarPedido -> Realiza uma requisição por meio do protocolo rpc
        29. ServerCliente->Grpc: CriarPedido -> Recebe uma requisição por meio do protocolo rpc
        30. ServerCliente->Ratis: Faz uma solicitação para o ClientRatis (add)
        32. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (add)
        34. ClienteCliente: Solicita a modificação de cada produto presente no pedido:
        35. ClienteCliente->Grpc: ModificarProduto -> Realiza uma requisição por meio do protocolo rpc
        36. ServerCliente->Grpc: ModificarProduto -> Recebe uma requisição por meio do protocolo rpc
        37. ClienteServer->Mosquitto: Publica o produto no tópico server/cliente/produto/modificar
        38. AdminServer->Mosquitto: Se subscreve no tópico server/cliente/produto/modificar
        39. AdminServer-> Armazena a nova quantidade do produto no database levelDB das 3 réplicas da máquina de estado
        40. ClientCliente: O pedido criado é exibido
        
    2. Modificar Pedido
        1. ClientCliente: Digite o CID
        2. ClienteCliente->Grpc: VerificarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: VerificarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. ClienteServer->Mosquitto: Publica o CID no tópico server/cliente/cliente/verificar 
        6. AdminServer-> Mosquitto: Se subscreve no tópico server/cliente/cliente/verificar
        7. AdminServer-> Verifica se o cliente existe
        8. AdminServer->Mosquitto: Publica a resposta no tópico server/admin/cliente/verificar 
        5. ClienteServer->Mosquitto: Se subscreve no tópico server/admin/cliente/verificar
        6. ClienteServer: A subcrição realizada recebe o que foi publicado 
        7. ClientCliente->Se o clinte existir:
        8. ClienteCliente: Digite o OID
        9. ClienteCliente->Grpc: BuscarPedido -> Realiza uma requisição por meio do protocolo rpc
        10. ServerCliente->Grpc: BuscarPedido -> Recebe uma requisição por meio do protocolo rpc
        11. Se o pedido não existir: Exibe a mensagem "Pedido não encontrado. Tente novamente
        12. Se o pedido existir:
        12. ClientCliente: Digite o PID
        13. ClienteCliente->Grpc: BuscarProduto -> Realiza uma requisição por meio do protocolo rpc
        14. ServerCliente->Grpc: BuscarProduto -> Recebe uma requisição por meio do protocolo rpc
        15. ClienteServer->Mosquitto: Publica o PID no tópico server/cliente/produto/buscar 
        16. AdminServer-> Mosquitto: Se subscreve no tópico server/cliente/produto/buscar
        17. AdminServer-> Busca o produto
        18. AdminServer->Mosquitto: Publica a resposta no tópico server/admin/produto/buscar
        19. ClienteServer->Mosquitto: Se subscreve no tópico server/admin/produto/buscar
        20. ClienteServer: A subcrição realizada recebe o que foi publicado 
        21. ClienteCliente: Se o produto existir:
        22. ClientCliente: Exibe o nome do produto, quantidade disponível, preço, quantidade presente no pedido, preço total presente no pedido 
        24. ClientCliente: Digite a quantidade desejada (Se for 0, o produto é apagado do pedido)
        25. ClientCliente: Multiplica a quantidade escolhida pelo preço do produto e salva no objeto ProdutoPedido
        26. ClienteCliente->Grpc: ModificarPedido -> Realiza uma requisição por meio do protocolo rpc
        27. ServerCliente->Grpc: ModificarPedido -> Recebe uma requisição por meio do protocolo rpc
        28. ClienteServer->Ratis: Faz uma solicitação para o ClientRatis (getClient:CID, OID)
        29. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getClient:CID, OID) e retorna a resposta para o AdminServer
        30. AdminCliente: A mensagem "Pedido não encontrado" é exibida se o get retornar null
        31. ClienteServer->Ratis: Faz uma solicitação para o ClientRatis (delClient:CID, OID)
        32. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (delClient:CID, OID)
        33. ClienteServer->Ratis: Faz uma solicitação para o ClientRatis (add)
        34. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (add) 
        35. ClienteCliente->Grpc: ModificarProduto -> Realiza uma requisição por meio do protocolo rpc
        36. ClienteServer->Grpc: ModificarProduto -> Recebe uma requisição por meio do protocolo rpc
        37. ClienteServer->Mosquitto: Publica o produto no tópico server/cliente/produto/modificar
        38. AdminServer->Mosquitto: Se subscreve no tópico server/cliente/produto/modificar
        39. AdminServer-> Armazena a nova quantidade do produto no database das 3 réplicas da máquina de estado
        40. ClientCliente: O pedido modificado é exibido
    4. Buscar Pedido
        1. ClientCliente: Digite o CID
        2. ClienteCliente->Grpc: VerificarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. ClienteServer->Grpc: VerificarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. ClienteServer->Mosquitto: Publica o CID no tópico server/cliente/cliente/verificar 
        6. AdminServer-> Mosquitto: Se subscreve no tópico server/cliente/cliente/verificar
        7. AdminServer-> Verifica se o cliente existe
        8. AdminServer->Mosquitto: Publica a resposta no tópico server/admin/cliente/verificar 
        5. ClienteServer->Mosquitto: Se subscreve no tópico server/admin/cliente/verificar
        6. ClienteServer: A subcrição realizada recebe o que foi publicado 
        7. ClientCliente->Se o clinte existir:
        8. ClienteCliente: Digite o OID
        9. ClienteCliente->Grpc: BuscarPedido -> Realiza uma requisição por meio do protocolo rpc
        10. ClienteServer->Grpc: BuscarPedido -> Recebe uma requisição por meio do protocolo rpc
        11. ClienteServer->Ratis: Faz uma solicitação para o ClientRatis (getClient:CID, OID)
        13. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getClient:CID, OID) e retorna a resposta para o ClienteServer
        15. ClienteCliente: O pedido é exibido se ele existir
        16. ClienteCliente: A mensagem "Pedido não encontrado" é exibida se o get retornar null                            
    5. Buscar Pedidos
        1. ClientCliente: Digite o CID
        2. ClienteCliente->Grpc: VerificarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: VerificarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. ClienteServer->Mosquitto: Publica o CID no tópico server/cliente/cliente/verificar 
        5. AdminServer-> Mosquitto: Se subscreve no tópico server/cliente/cliente/verificar
        6. AdminServer-> Verifica se o cliente existe
        7. AdminServer->Mosquitto: Publica a resposta no tópico server/admin/cliente/verificar 
        8. ClienteServer->Mosquitto: Se subscreve no tópico server/admin/cliente/verificar
        9. ClienteServer: A subcrição realizada recebe o que foi publicado 
        10. ClientCliente->Se o clinte existir:
        11. ClienteCliente->Grpc: BuscarPedidos -> Realiza uma requisição por meio do protocolo rpc
        12. ServerCliente->Grpc: BuscarPedidos -> Recebe uma requisição por meio do protocolo rpc
        12. ServerCliente->Ratis: Faz uma solicitação para o ClientRatis (getClient:CID)
        14. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getClient:CID) e retorna a resposta para o ClientServer
        16. ClienteCliente: A mensagem "O cliente não possui pedidos" é exibida se o get retornar null         
        17. ClientServer-> Retorna cada pedido associado a soma dos produtos presentes nele
        18. ClienCliente: As associações são exibidas
   6. Apagar Pedido
        1. ClientCliente: Digite o CID
        2. ClienteCliente->Grpc: VerificarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: VerificarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. ClienteServer->Mosquitto: Publica o CID no tópico server/cliente/cliente/verificar 
        5. AdminServer-> Mosquitto: Se subscreve no tópico server/cliente/cliente/verificar
        6. AdminServer-> Verifica se o cliente existe
        7. AdminServer->Mosquitto: Publica a resposta no tópico server/admin/cliente/verificar 
        8. ClienteServer->Mosquitto: Se subscreve no tópico server/admin/cliente/verificar
        9. ClienteServer: A subcrição realizada recebe o que foi publicado 
        10. ClientCliente->Se o clinte existir:
        11. ClienteCliente: Digite o OID
        12. ClienteCliente->Grpc: ApagarPedido -> Realiza uma requisição por meio do protocolo rpc
        13. ClienteServer->Grpc: ApagarPedido -> Recebe uma requisição por meio do protocolo rpc
        14. ClienteServer->Ratis: Faz uma solicitação para o ClientRatis (getClient:CID, OID)
        15. ClientRatis: Faz uma solicitação para a réplica p1, p2 ou p3 (getClient:CID, OID) e retorna a resposta para o ClienteServer
        16. ClienteCliente: A mensagem "Pedido não encontrado" é exibida se o get retornar null
        17. ClienteServer->Ratis: Faz uma solicitação para o ClientRatis (delClient: CID, OID)
        18. ClientRatis: Faz uma solicitação para as réplicas p1, p2 e p3 (delClient: CID, OID)
        19. ClienteCliente: A mensagem "Produto apagado" é exibida 
                   
 ### Cache
 
 1. Admin
 
    1. Criar Cliente/Criar Produto
        1. O cliente/produto é salvo no levelDB de cada réplica da máquina de estado
        2. A função buscarCliente/buscarProduto é chamada
    2. Modificar Cliente/Modificar Produto
        1. A função buscarCliente/buscarProduto é chamada
        1. A função apagarCliente/ApagarProduto é chamada
        2. A função criarCliente/CriarProduto é chamada
    3. Buscar Cliente/Buscar Produto
        1. Se o cliente/produto existir no cache, ele é retornado
        2. Se o cliente/produto exisitr no levelDB de qualquer réplica da máquina de estado
            1. Ele é salvo no cache
            2. Ele é retornado
    4. Apagar Cliente/Apagar Produto
        1. Se o cliente/produto existir no cache, ele é apagado
        2. Se o cliente/produto existir no levelDB de qualquer réplica da mmáquina de estado, ele é apagado de todas as réplicas da máquinas de estado
      
2. Cliente
 
    1. Criar Pedido  
        1. O pedido é salvo no levelDB de cada réplica da máquina de estado
        2. A função buscarPedido é chamada 
    2. Modificar Pedido
        1. A função buscarPedido é chamada
        2. A função apagarPedido é chamada
        3. A função criarPedido é chamada
    4. Buscar Pedido
       1. Se o pedido existir no cache, ele é retornado
       2. Se o pedido exisitr no levelDB de qualquer réplica da máquina de estado
            1. Ele é salvo no cache (Tempo de expiração: 30 segundos)
            2. Ele é retornado
    5. BuscarPedidos
       1. Se o cliente possuir pelo menos um pedido no cache, cliente: List[{pedido: soma dos produtos}] é retornado
       2. Se o cliente possuir pelo menos um pedido no levelDB de qualquer réplica da máquina de estado
            1.  O (s) pedido (s) é/sào salvo (s) no cache (Tempo de expiração: 30 segundos)
            2.  List[{pedido: soma dos produtos}] é retornado 
    7. Apagar Pedido
       1. Se o pedido existir no cache, ele é apagado
       2. Se o pedido existir no levelDB de qualquer réplica da mmáquina de estado, ele é apagado de todas as réplicas da máquinas de estado
      
 3.  Cliente -> Admin 
 - Admin
    1. verificarSeClienteExiste
       1. A função apagarCliente é chamada
       2. A função isCliente é chamada
    2. buscarProduto
       1. A função apagarProduto é chamada
       2. A função buscarProduto é chamada
    3. modificarProduto
       1. A função modificarProduto é chamada
       
## Critérios Atendidos

 - Como é possível notar:
    1. Todos os casos de uso foram implementados
    2. Há suporte para múltiplos clientes/servidores
    3. Dois tipos de comunicação foram utilizados RPC (Grpc) e PUB/SUB (Mosquitto)
    4. A documentação da configuração, execução e do uso foi feita
    5. As exçeções foram tratadas e os erros foram retornados
    6. Os portais possuem cache 
    7. Clientes e adminstradores possuem interface interativa (terminal)
    8. Testes automatizados foram criados
    9. 3 réplicas da máquina de estado foram criadas por meio do Ratis
    10. Cada réplica da máquina de estado contém um database levelDB
   
 ## Vídeo De Apresentação





                                                 


