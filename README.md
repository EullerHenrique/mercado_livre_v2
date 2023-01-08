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
4. Digite no CMD: git clone https://github.com/EullerHenrique/mercado_livre

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

###  Server

1. Ratis
    1. Navegue até mercado_livre/server/config/ratis/ReplicationAdminServer
    2. Aperte o botão play localizado ao lado de "public class ReplicationAdminServer"
    
2. Admin
    1. Navegue até mercado_livre/server/admin/AdminServer
    2. Aperte o botão play localizado ao lado de "public class AdminServer"
    3. Digite a porta desejada (Ex: 5051)
3. Cliente
    1. Navegue até mercado_livre/server/cliente/ClienteServer
    2. Aperte o botão play localizado ao lado de "public class ClienteServer"
    3. Digite a porta desejada (Ex: 5052)
    
### Client

1. Ratis
     1. Navegue até mercado_livre/server/config/ratis/ReplicationClienteServer
     2. Aperte o botão play localizado ao lado de "public class ReplicationClienteServer"
     
2. Admin
     1. Navegue até mercado_libre/client/admin/AdminClient
     2. Aperte o botão play localizado ao lado de "public class AdminClient"
     3. Digite a porta escolhida ao criar o AdminServer (Ex: 5052)

3. Cliente
    1. Navegue até mercado_libre/client/cliente/ClientCliente
    2. Aperte o botão play localizado ao lado de "public class ClientCliente"
    3. Digite a porta escolhida ao criar o ClienteServer (Ex: 5052)

### Funcionalidades

1. Admin
    1. Criar Cliente
        1. ClientCliente: Digite o nome do cliente
        2. ClientCliente: Digite o email do cliente
        3. ClientCliente: Digite o telefone do cliente
        4. ClienteCliente->Grpc: CriarCliente -> Realiza uma requisição por meio do protocolo rpc
        5. ServerCliente->Grpc: CriarCliente -> Recebe uma requisição por meio do protocolo rpc
        6. ServerCliente->Ratis->LevelDB: Salva o cliente no database admin presente nas réplicas do servidor x (Réplicas p1, p2 e p3) 
        7. ClientCliente: O cliente criado é exibido
    2. Modificar Cliente
        1. ClientCliente: Digite o nome do cliente
        2. ClientCliente: Digite o email do cliente
        3. ClientCliente: Digite o telefone do cliente
        4. ClienteCliente->Grpc: ModificarCliente -> Realiza uma requisição por meio do protocolo rpc
        5. ServerCliente->Grpc: ModificarCliente -> Recebe uma requisição por meio do protocolo rpc
        6. ServerCliente: Se o cliente estiver presente na tabela hash (Cliente) do servidor x -> g-m
        7. ServerCliente: Salva a modificação do cliente na tabela hash (Cliente) do servidor x 
        8. ServerClient->Mosquitto: Se subscreve no tópico server/admin/cliente/modificar  
        9. ServerClient->Mosquitto: Publica o cliente modificado no tópico server/admin/cliente/modificar 
        10. ServerClient: A subcrição realizada recebe o cliente que foi publicado 
        11. ServerClient: Se o cliente existir na tabela hash (Cliente) do servidor x, nada é feito
        12. ServerClient: Se o cliente não existir na tabela hash (Cliente) do servidor y, z, w, n ..., o cliente é salvo  no servidor y, z, w, n ... 
        13. ClientCliente: O cliente atualizado é exibido se ele existir
        14. ClientCliente: A mensagem "Cliente não encontrado" é exibida se ele não existir
    3. Buscar Cliente
        1. ClienteCliente: Digite o CID do cliente
        2. ClienteCliente->Grpc: BuscarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: BuscarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. ServerCliente: Realiza a busca do produto na tabela hash (Cliente)
        5. ClienteCliente: O cliente buscado é exibido se ele existir
        6. ClienteCliente: A mensagem "Cliente não encontrado" é exibida se ele não existir
    4. Apagar Cliente
        1. ClienteCliente: Digite o CID do cliente
        2. ClienteCliente->Grpc: ApagarCliente -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: ApagarCliente -> Recebe uma requisição por meio do protocolo rpc
        4. ServerCliente: Se o cliente estiver presente na tabela hash (Cliente) do servidor x -> e-k
        5. ServerCliente: Apaga o cliente presente na tabela hash (Cliente) do servidor x 
        6. ServerClient->Mosquitto: Se subscreve no tópico server/admin/cliente/modificar  
        7. ServerClient->Mosquitto: Publica o CID no tópico server/admin/cliente/modificar 
        8. ServerClient: A subcrição realizada recebe o CID que foi publicado 
        9. ServerClient: Se o cliente existir na tabela hash (Cliente) do servidor x, nada é feito
        10. ServerClient: Se o cliente não existir na tabela hash (Cliente) do servidor y, z, w, n ..., o cliente é salvo  no servidor y, z, w, n ... 
        11. ClienteCliente: A mensagem "Cliente apagado" é exibida se ele não existir
        12. ClienteCliente: A mensagem "Cliente não encontrado" é exibida se ele não existir    
    5. Criar Produto  
        1. ClientCliente: Digite o nome do produto
        2. ClientCliente: Digite a quantidade do produto
        3. ClientCliente: Digite o preço do produto
        4. ClienteCliente->Grpc: CriarProduto -> Realiza uma requisição por meio do protocolo rpc
        5. ServerCliente->Grpc: CriarProduto -> Recebe uma requisição por meio do protocolo rpc
        6. ServerCliente: Salva o produto na tabela hash (Produto) do servidor x
        7. ServerClient->Mosquitto: Se subscreve no tópico server/admin/produto/criar  
        8. ServerClient->Mosquitto: Publica o cliente criado no tópico server/admin/produto/criar 
        9. ServerClient: A subcrição realizada recebe o produto que foi publicado 
        10. ServerClient: Se o produto existir na tabela hash (Produto) do servidor x, nada é feito
        11. ServerClient: Se o produto não existir na tabela hash (Produto) do servidor y, z, w, n ..., o produto é salvo  no servidor y, z, w, n ... 
        12. ClientCliente: O produto criado é exibido
    6. Modificar Produto
        1. ClientCliente: Digite o PID do produto
        2. ClientCliente: Digite o nome do produto
        3. ClientCliente: Digite a quantidade do produto
        4. ClientCliente: Digite o preço do produto
        5. ClienteCliente->Grpc: ModificarProduto -> Realiza uma requisição por meio do protocolo rpc
        6. ServerCliente->Grpc: ModificarProduto -> Recebe uma requisição por meio do protocolo rpc
        7. ServerCliente: Se o produto estiver presente na tabela hash (Produto) do servidor x -> g-n
        8. ServerCliente: Salva a modificação do produto na tabela hash (Produto) do servidor x 
        9. ServerClient->Mosquitto: Se subscreve no tópico server/admin/produto/modificar  
        10. ServerClient->Mosquitto: Publica o produto modificado no tópico server/admin/produto/modificar 
        11. ServerClient: A subcrição realizada recebe o produto que foi publicado 
        12. ServerClient: Se o produto existir na tabela hash (Produto) do servidor x, nada é feito
        13. ServerClient: Se o produto não existir na tabela hash (Produto) do servidor y, z, w, n ..., o produto é salvo  no servidor y, z, w, n ... 
        14. ClientCliente: O produto atualizado é exibido se ele existir
        15. ClientCliente: A mensagem "Produto não encontrado" é exibida se ele não existir
    7. Buscar Produto
        1. ClienteCliente: Digite o PID do produto
        2. ClienteCliente->Grpc: BuscarProduto -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: BuscarProduto -> Recebe uma requisição por meio do protocolo rpc
        4. ServerCliente: Realiza a busca do produto na tabela hash (Produto)
        5. ClienteCliente: O produto buscado é exibido se ele existir
        6. ClienteCliente:A mensagem "Produto não encontrado" é exibida se ele não existir
    8. Apagar Produto
        1. ClienteCliente: Digite o PID do cliente
        2. ClienteCliente->Grpc: ApagarProduto -> Realiza uma requisição por meio do protocolo rpc
        3. ServerCliente->Grpc: ApagarProduto -> Recebe uma requisição por meio do protocolo rpc
        4. ServerCliente: Se o produto estiver presente na tabela hash (Produto) do servidor x -> e-k
        5. ServerCliente: Apaga o produto presente na tabela hash (Produto) do servidor x 
        6. ServerClient->Mosquitto: Se subscreve no tópico server/admin/produto/apagar  
        7. ServerClient->Mosquitto: Publica o PID no tópico server/admin/produto/apagar 
        8. ServerClient: A subcrição realizada recebe o PID que foi publicado 
        9. ServerClient: Se o produto existir na tabela hash (Produto) do servidor x, nada é feito
        10. ServerClient: Se o produto não existir na tabela hash (Produto) do servidor y, z, w, n ..., o produto é salvo  no servidor y, z, w, n ... 
        11. ClienteCliente: A mensagem "Produto apagado" é exibida se ele não existir
        12. ClienteCliente: A mensagem "Produto não encontrado" é exibida se ele não existir    
   
2. Cliente
    1. Criar Pedido
    2. Modificar Pedido
    3. Buscar Pedido
    4. Buscar Pedidos
    5. Apagar Pedido






                                                 


