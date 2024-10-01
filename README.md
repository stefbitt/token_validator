# Projeto Validador de Token

## Visão Geral
Este projeto é uma API RESTful para validação de JSON Web Tokens (JWT). Ele fornece um endpoint para verificar a validade de JWTs e oferece manipulação de erros e logs detalhados. A solução é construída usando Spring Boot e Java, e inclui classes de serviço para validação de tokens e validação de claims (reivindicações).

## Requisitos

Requisito obrigatprio ter as seguintes ferramentas
- docker
- Java 11 ou superior
- Maven

## Passo a passo para executar e testar a aplicação
git clone https://github.com/stefbitt/token_validator

cd token_validator

mvn clean install

mvn spring-boot:run<br>
A aplicação será iniciada na porta 80.

# Testando a Aplicação<br>
Você pode utilizar ferramentas como Postman para realizar requisições:

## Local
GET http://localhost/api/v1/token/validate?jwt=eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg

## Cloud Aws
GET http://token-validator-alb-596544518.us-east-1.elb.amazonaws.com/api/v1/token/validate?jwt=eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg

# Docker
para subir a aplicação através do docker basta executar os comandos abaixo
```bash
docker build -t valida-token:latest .
docker run valida-token:latest 80:80
```

# Estrutura e Detalhes do Projeto
## Classe TokenController
A classe TokenController expõe um endpoint REST para validação de tokens.

## Endpoint: GET /api/v1/token/validate
Valida um JWT fornecido no corpo da requisição. Retorna um booleano indicando se o token é válido (true para válido, false caso contrário).

Exemplo:

```java
@GetMapping("/validate")
public ResponseEntity<Boolean> validateToken(@RequestParam("jwt") String jwt) {
        try {
            boolean isValid = tokenValidationService.validate(jwt);
            return new ResponseEntity<>(isValid, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }    
```
# Manipulação de Erros:
Se uma exceção ocorrer durante a validação, ela é capturada e uma resposta com status 400 BAD REQUEST é retornada.

# Classe TokenValidationService
Este classe é responsável pela lógica de negócio para validar JWTs.

### validate(String jwt)
Este método é responsável por decodificar o JWT e validar suas claims. Ele usa o método getDecodedJWT para decodificar o token e, em seguida, passa as claims decodificadas para o ClaimsValidationService.


```java
public boolean validate(String jwt) throws InvalidTokenException
```
Este método decodifica o JWT usando uma biblioteca de terceiros (como java-jwt). Inclui também logs e manipulação de exceções para tratar possíveis erros de parsing.

### getDecodedJWT(String token)
```java
public DecodedJWT getDecodedJWT(String token)
```

# Classe ClaimsValidationService
O ClaimsValidationService é responsável por validar claims específicas dentro do JWT. Por exemplo, ele pode validar a presença de certas claims necessárias ou garantir que as claims sigam regras específicas.

# Logging
Os logs são tratados usando o log4j2, fornecendo diferentes níveis de log:

### Tratamento de Erros e Premissas
- InvalidTokenException: Uma exceção personalizada é lançada quando a validação do JWT falha, fornecendo uma mensagem de erro mais significativa.<br><br>
- Tratamento Geral de Exceções: Caso ocorra qualquer exceção inesperada, uma InvalidTokenException genérica é lançada com uma mensagem de erro apropriada.<br><br>

# Testes integrados
Os testes integrados foram criados utilizando o cumcuber<br>
para executar basta executar o comando abaixo:

```bash
mvn -Dcucumber.options="--glue com.itau.auth.token_validator.integration.steps src/test/resources/features" test
```

# Decisões de Design

## Arquitetura Orientada a Serviços:
O projeto utiliza serviços separados para validação de token e validação de claims, promovendo o Princípio da Responsabilidade Única (SRP) e tornando o código mais modular e testável.

Tratamento Personalizado de Exceções: Uma InvalidTokenException personalizada é usada para distinguir entre diferentes tipos de erros durante a validação, facilitando a identificação de problemas durante o processo de depuração.

Abordagem de Testes BDD: Os testes são escritos em um estilo orientado por comportamento para facilitar uma melhor comunicação sobre o comportamento esperado do processo de validação do JWT.

Parsing e Validação de JWT: O parsing e a validação de JWTs são feitos usando uma biblioteca dedicada (java-jwt) para garantir que o token seja decodificado e verificado de forma segura e precisa.

# Infra 

Os recursos de infra relacionados na aplicação foram provisionados na aws através do terraform<br>
Toda a estrutura está na pasta /infra
