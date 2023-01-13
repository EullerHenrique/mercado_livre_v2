# Mercado Livre

## Sumário

- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configuração](#configuração)
- - [Mosquitto](#mosquitto)
- - [Java](#java)
- [Execução](#execução)
  - [Server](#server)   
  - [Client](#client)
  - [Funcionalidades](#funcionalidades)
  
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

### Servidor Ratis

    1. Navegue até mercado_livre/ratis/ReplicationServer
    2. Aperte o botão play localizado ao lado de "public class ReplicationServer"
    3. O servidor ratis cria o servidor p1 (porta 3000) da máquina de estado (A pasta será inserida no caminho /tmp/)
    4. O servidor ratis cria o servidor p2 (porta 3500) da máquina de estado (A pasta será inserida no caminho /tmp/)
    5. O servidor ratis cria o servidor p3 (porta 4000) da máquina de estado (A pasta será inserida no caminho /tmp/)
    4. O servidor p1 da máquina de estado cria o database levelDB p1 (A pasta será inserida no caminho /main/resources/db/p1)
    5. O servidor p2 da máquina de estado cria o database levelDB p2 (A pasta será inserida no caminho /main/resources/db/p2)
    6. O servidor p3 da máquina de estado cria o database levelDB p2 (A pasta será inserida no caminho /main/resources/db/p3)
    7. O ReplicationClient é responsável por receber solicitações e redirecionar para o ReplicationServer
    8. o ReplicationServer é responsável por receber solitações do ReplicationServer e redirecionar para as réplicas da máquina de estado criadas
    9. Cada máquina de estado é responsável por atender a solicitação (get, add ou del)
    10. O ReplicationServer é responsável por enviar a resposta de cada réplica para o ReplicationClient
    11. O ReplicationClient é responsável por enviar a resposta do ReplicationServer para quem a solicitou.
    
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

## Tabelas Chave/Valor (LevelDB)

1. Cliente
     1. Key: CID Value: {CID, nome, email, telefone}
2. Produto
     2. key: PID Value: {CID, OID, PID, produto, quantidade, preco}
3. Pedido 
     1. key: UUID Value: {CID, OID, produtos: [CID, OID, PID, produto, quantidade, preco]}

### Funcionalidades

1. Admin

    1. Criar Cliente
        1. AdminClient: Digite o nome do cliente
        2. AdminClient: Digite o email do cliente
        3. AdminClient: Digite o telefone do cliente
        4. AdminClient->Grpc: CriarCliente -> Realiza uma requisição por meio do protocolo rpc
        5. AdminServer->Grpc: CriarCliente -> Recebe uma requisição por meio do protocolo rpc
        6. AdminServer->Ratis: Faz uma solicitação para o ReplicationClient (add)
        7. ReplicationClient: Faz uma solicitação para o ReplicationServer (add)
        8. ReplicationServer: Faz uma solicitação para as réplicas p1, p2 e p3
        9. AdminCliente: O cliente criado é exibido
        
    2. Modificar Cliente
        1. AdminCliente: Digite o nome do cliente
        2. AdminCliente: Digite o email do cliente
        3. AdminCliente: Digite o telefone do cliente
        4. AdminCliente->Grpc: ModificarCliente -> Realiza uma requisição por meio do protocolo rpc
        5. AdminServer->Grpc: ModificarCliente -> Recebe uma requisição por meio do protocolo rpc
        6. AdminServer->Ratis: Faz três solicitações para o ReplicationClient (getAdmin:CID, delAdmin:CID e add)
        7. ReplicationClient: Faz três solicitações para o ReplicationServer (getAdmin:CID, delAdmin:CID e add)
        8. ReplicationServer: Faz três solicitações para as réplicas p1, p2 e p3 (get, del e add) e retorna a resposta do get para o ReplicationClient
        10. ReplicationClient: Retorna a resposta para o AdminServer
        11. AdminCliente: O cliente modificado é exibido se ele existir
        12. AdminCliente: A mensagem "Cliente não encontrado" é exibida se o get retornar null
        
    3. Buscar Cliente
        1. AdminCliente: Digite o CID do cliente
        2. AdminCliente->Grpc: BuscarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. AdminServer->Grpc: BuscarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. AdminServer->Ratis: Faz uma solicitação para o ReplicationClient (getAdmin:CID)
        5. ReplicationClient: Faz uma solicitação para o ReplicationServer (getAdmin:CID)
        6. ReplicationServer: Faz uma solicitação para as réplicas p1, p2 e p3 e retorna a resposta para o ReplicationClient
        7. ReplicationClient: Retorna a resposta para o AdminServer
        8. AdminCliente: O cliente buscado é exibido se ele existir
        9. AdminCliente: A mensagem "Cliente não encontrado" é exibida se o get retornar null
        
    4. Apagar Cliente
        1. AdminCliente: Digite o CID do cliente
        2. AdminCliente->Grpc: ApagarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. AdminServer->Grpc: ApagarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. AdminServer->Ratis: Faz duas solicitações para o ReplicationClient (getAdmin:CID e delAdmin:CID)
        5. ReplicationClient: Faz duas solicitações para o ReplicationServer (getAdmin:CID e delAdmin:CID)
        6. ReplicationServer: Faz duas solicitações para as réplicas p1, p2 e p3 e retorna a resposta do get para o ReplicationClient
        7. ReplicationClient: Retorna a resposta para o AdminServer
        8. AdminCliente: A mensagem "Cliente apagado" é exibida se ele existir
        9. AdminCliente: A mensagem "Cliente não encontrado" é exibida se o get retornar null
        
    5. Criar Produto  
        1. AdminCliente: Digite o nome do produto
        2. AdminCliente: Digite a quantidade do produto
        3. AdminCliente: Digite o preço do produto
        4. AdminCliente->Grpc: CriarProduto -> Realiza uma requisição por meio do protocolo rpc
        5. AdminServer->Grpc: CriarProduto -> Recebe uma requisição por meio do protocolo rpc
        5. AdminServer->Grpc: CriarCliente -> Recebe uma requisição por meio do protocolo rpc
        7. AdminServer->Ratis: Faz uma solicitação para o ReplicationClient (add)
        8. ReplicationClient: Faz uma solicitação para o ReplicationServer (add)
        9. ReplicationServer: Faz uma solicitação para as réplicas p1, p2 e p3 
        10. AdminCliente: O produto criado é exibido
        
    6. Modificar Produto
        1. AdminCliente: Digite o PID do produto
        2. AdminCliente: Digite o nome do produto
        3. AdminCliente: Digite a quantidade do produto
        4. AdminCliente: Digite o preço do produto
        5. AdminCliente->Grpc: ModificarProduto -> Realiza uma requisição por meio do protocolo rpc
        6. AdminServer->Grpc: ModificarProduto -> Recebe uma requisição por meio do protocolo rpc
        7. AdminServer->Ratis: Faz três solicitações para o ReplicationClient (getAdmin:PID, delAdmin:PID e add)
        8. ReplicationClient: Faz três solicitações para o ReplicationServer (getAdmin:PID, delAdmin:PID e add)
        9. ReplicationServer: Faz três solicitações para as réplicas p1, p2 e p3 (get, del e add) e retorna a resposta do get para o ReplicationClient
        10. RecationClient: Retorna a resposta para o AdminServer
        11. AdminCliente: O produto atualizado é exibido se ele existir
        12. AdminCliente: A mensagem "Produto não encontrado" é exibida se o get retornar null
        
    7. Buscar Produto
        1. AdminCliente: Digite o PID do produto
        2. AdminCliente->Grpc: BuscarProduto -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: BuscarProduto -> Recebe uma requisição por meio do protocolo rpc
        4. AdminServer->Ratis: Faz uma solicitação para o ReplicationClient (getAdmin:PID)
        5. ReplicationClient: Faz uma solicitação para o ReplicationServer (getAdmin:PID)
        6. ReplicationServer: Faz uma solicitação para as réplicas p1, p2 e p3 e e retorna a resposta do get para o ReplicationClient
        7. ReplicationClient: Retorna a resposta para o AdminServer
        8. AdminCliente: O produto buscado é exibido se ele existir
        9. AdminCliente: A mensagem "Produto não encontrado" é exibida se o get retornar null
        
    8. Apagar Produto
        1. AdminCliente: Digite o PID do cliente
        2. AdminCliente->Grpc: ApagarProduto -> Realiza uma requisição por meio do protocolo rpc
        3. AdminServer->Grpc: ApagarProduto -> Recebe uma requisição por meio do protocolo rpc
        4. AdminServer->Ratis: Faz duas solicitações para o ReplicationClient (getAdmin:PID e delAdmin:PID)
        5. ReplicationClient: Faz duas solicitações para o ReplicationServer (getAdmin:PID e delAdmin:PID)
        6. ReplicationServer: Faz duas solicitações para as réplicas p1, p2 e p3 e retorna a resposta do get para o ReplicationClient
        7. ReplicationClient: Retorna a resposta para o AdminServer
        8. AdminCliente: A mensagem "Produto apagado" é exibida se ele existir
        9. AdminCliente: A mensagem "Produto não encontrado" é exibida se o get retornar null
        
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
        30. ServerCliente->Ratis: Faz uma solicitação para o ReplicationClient (add)
        31. ReplicationClient: Faz uma solicitação para o ReplicationServer (add)
        32. ReplicationServer: Faz uma solicitação para as réplicas p1, p2 e p3 
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
        28. ClienteServer->Ratis: Faz três solicitações para o ReplicationClient (getClient, delClient e add)
        29. ReplicationClient: Faz três solicitações para o ReplicationServer (getClient:CID, OID, delClient:CID, OID e add)
        30. ReplicationServer: Faz três solicitações para as réplicas p1, p2 e p3 (getClient:CID, OID, delClient:CID, OID e add) e retorna a resposta do get para o ReplicationClient
        31. ReplicationClient: Retorna a resposta para o AdminServer
        32. ClienteCliente: A mensagem "Pedido não encontrado" é exibida se o get retornar null
        33. ClienteCliente->Grpc: ModificarProduto -> Realiza uma requisição por meio do protocolo rpc
        34. ServerCliente->Grpc: ModificarProduto -> Recebe uma requisição por meio do protocolo rpc
        35. ClienteServer->Mosquitto: Publica o produto no tópico server/cliente/produto/modificar
        36. AdminServer->Mosquitto: Se subscreve no tópico server/cliente/produto/modificar
        37. AdminServer-> Armazena a nova quantidade do produto no database das 3 réplicas da máquina de estado
        38. ClientCliente: O pedido modificado é exibido
    4. Buscar Pedido
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
        11. ServerCliente->Ratis: Faz uma solicitação para o ReplicationClient (getClient:CID, OID)
        12. ReplicationClient: Faz uma solicitação para o ReplicationServer (getClient:CID, OID)
        13. ReplicationServer: Faz uma solicitação para as réplicas p1, p2 e p3 e retorna a resposta para o ReplicationClient
        14. ReplicationClient: Retorna a resposta para o ClienteCliente
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
        12. ServerCliente->Ratis: Faz uma solicitação para o ReplicationClient (getClient:CID)
        13. ReplicationClient: Faz uma solicitação para o ReplicationServer (getClient:CID)
        14. ReplicationServer: Faz uma solicitação para as réplicas p1, p2 e p3 e retorna a resposta para o ReplicationClient
        15. ReplicationClient: Retorna a resposta para o ClienteCliente
        16. ClienteCliente: A mensagem "O cliente não possui pedidos" é exibida se o get retornar null         
        17. ServerServer-> Retorna cada pedido associado a soma dos produtos presentes nele
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
        13. ServerCliente->Grpc: ApagarPedido -> Recebe uma requisição por meio do protocolo rpc
        14. ServerCliente: Se o produto estiver presente na tabela hash (Peiddo) do servidor x:
        15. ServerCliente: Apaga o produto presente na tabela hash (Pedido) do servidor x 
        16. ServerCliente->Mosquitto: Se subscreve no tópico server/cliente/pedido/apagar  
        17. ServerCliente->Mosquitto: Publica pedido no tópico server/cliente/pedido/apagar 
        18. ServerCliente: A subcrição realizada recebe o pedido que foi publicado 
        19. ServerCliente: Se o pedido existir na tabela hash (Pedido) do servidor x, nada é feito
        20. ServerCliente: Se o pedido não existir na tabela hash (Produto) do servidor y, z, w, n ..., o produto é salvo  no servidor y, z, w, n ... 
        21. ClienteCliente: A mensagem "Pedido apagado" é exibida se ele existir
        22. ClienteCliente: A mensagem "Pedido não encontrado" é exibida se ele não existir    
    
 ## Critérios Atendidos
   
 ## Vídeo De Apresentação





                                                 


