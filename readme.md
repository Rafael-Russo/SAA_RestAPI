# SAA_RestAPI

## Sumário
- [Introdução](#introdução)
- [Executando a Aplicação](#executando-a-aplicação)
- [Configurando o MongoDB](#configurando-o-mongodb)
- [Rotas e Permissões](#rotas-e-permissões)
- [Alterando Configurações de Permissões](#alterando-configurações-de-permissões)

## Introdução

Este projeto é uma API REST para o Sistema de Autenticação e Autorização (SAA) utilizando Spring Boot e MongoDB.

## Executando a Aplicação

### Pré-requisitos

- Java 17 ou superior
- Maven
- MongoDB

### Passos

1. Clone o repositório:
    ```sh
    git clone https://github.com/Rafael-Russo/SAA_RestAPI.git
    cd SAA_RestAPI
    ```

2. Configure o arquivo `application.properties` com as informações do seu MongoDB:
    ```properties
    spring.application.name=SAA_RestAPI
    spring.data.mongodb.host=localhost
    spring.data.mongodb.port=27017
    spring.data.mongodb.database=bd-saa
    ```

3. Compile e execute a aplicação:
    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

## Configurando o MongoDB

1. Certifique-se de que o MongoDB está instalado e em execução:
    ```sh
    mongod --dbpath <seu-caminho-para-os-dados>
    ```

2. Use o MongoDB Compass ou o shell do MongoDB para criar o banco de dados `bd-saa` e a coleção `user`.

## Rotas e Permissões

### Rotas Públicas

- **POST /register**: Registro de novos usuários.
    - Permissão: Aberta a todos.

### Rotas de Usuário

- **POST /login**: Autenticação de usuários, retorna o token do usuário.
    - Permissão: `USER`, `MOD`, `ADMIN`.

- **GET /info/{token}**: Extrair informações de um token.
    - Permissão: `USER`, `MOD`, `ADMIN`.

- **GET /user**: Obtém informações do usuário autenticado.
    - Permissão: `USER`, `MOD`, `ADMIN`.

### Rotas de Moderador

- **PUT /edit/{id}**: Atualizar informações de um usuário.
    - Permissão: `MOD`, `ADMIN`.

- **GET /mod**: Obtém informações do moderador autenticado.
    - Permissão: `MOD`, `ADMIN`.

### Rotas de Administrador

- **DELETE /remove/{id}**: Remove um usuário pelo ID.
    - Permissão: `ADMIN`.

- **GET /admin**: Obtém informações do administrador autenticado.
    - Permissão: `ADMIN`.

## Alterando Configurações de Permissões

As permissões das rotas são configuradas na classe `SecurityConfig`. Você pode alterar as permissões modificando as regras de autorização dentro do método `securityFilterChain`.

### Exemplo de Configuração de Permissões

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request -> request
            .requestMatchers(HttpMethod.POST, "/login/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/register/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/info/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/mod/**").hasAnyRole("MOD", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/edit/**").hasAnyRole("MOD", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/remove/**").hasRole("ADMIN")
            .anyRequest()
            .authenticated()
        ).httpBasic(Customizer.withDefaults());
    return http.build();
}
```
Para adicionar ou modificar permissões, ajuste os métodos requestMatchers e hasRole ou hasAnyRole conforme necessário.

### Exemplo de Adição de Nova Rota
Se você adicionar uma nova rota ao seu controlador e quiser definir permissões específicas para ela, adicione a rota no método securityFilterChain.
```java
// Exemplo de nova rota GET /reports/** acessível apenas para ADMIN
.requestMatchers(HttpMethod.GET, "/reports/**").hasRole("ADMIN")
```
