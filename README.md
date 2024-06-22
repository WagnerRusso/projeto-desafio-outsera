# projeto-desafio-outsera

# Golden Raspberry Awards API

Este projeto implementa uma API RESTful para consultar prêmios do Golden Raspberry Awards.

## Instruções de Uso

1. Clone o repositório do GitHub: `git clone https://github.com/WagnerRusso/projeto-desafio-outsera.git`
2. Navegue para o diretório do projeto: `cd projeto-desafio-outsera/projeto-desafio-outsera`
3. Execute a aplicação Spring Boot: `./mvnw spring-boot:run`
4. Acesse a API através de: `http://localhost:8080/api/films/awards`
5. Para acessao o banco de dados H2
	http://localhost:8080/h2-console/
	User: sa
	pwd:

## Testes de Integração

Para executar os testes de integração:

1. Certifique-se de que a aplicação está em execução (`spring-boot:run`).
2. Execute os testes de integração com Maven: `./mvnw test`

Os testes de integração garantem que a API esteja funcionando corretamente conforme as especificações.

## Tecnologias Utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- JUnit e Spring Test