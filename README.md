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
- ...

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
4. Aperte com o botão direito do mouse na pasta mercado_libre/src/main
5. Clique em Build Module 'mercado_livre.main'

## Execução

###  Server
1.Admin
  1. Navegue até mercado_libre/server/admin/AdminServer
  2. Aperte o botão play localizado ao lado de "public class AdminServer"
  3. Digite a porta desejada (Ex: 5051)
2.Cliente
  1. Navegue até mercado_libre/server/cliente/ClienteServer
  2. Aperte o botão play localizado ao lado de "public class ClienteServer"
  3. Digite a porta desejada (Ex: 5052)
    
### Client
1.Admin
  1. Navegue até mercado_libre/client/admin/AdminClient
  2. Aperte o botão play localizado ao lado de "public class AdminClient"
  3. Digite a porta escolhida ao criar o AdminServer (Ex: 5052)
2.Cliente
  1. Navegue até mercado_libre/client/cliente/ClientCliente
  2. Aperte o botão play localizado ao lado de "public class ClientCliente"
  3. Digite a porta escolhida ao criar o ClienteServer (Ex: 5052)

### Funcionalidades
1. Admin
  1. Criar Cliente
  2. Modificar Cliente
  3. Buscar Cliente
  4. Apagar Cliente
  5. Criar Produto
  6. Modificar Produto
  7. Buscar Produto
  8. Apagar Produto
3. Cliente
  1. Criar Pedido
  2. Modificar Pedido
  3. Buscar Pedido
  4. Buscar Pedidos
  5. Apagar Pedido






                                                 


