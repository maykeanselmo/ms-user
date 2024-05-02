# Microserviço de Usuário (ms-user)
## Visão Geral

O projeto concentra-se no desenvolvimento de um microsserviço voltado para o gerenciamento de usuários, aproveitando as tecnologias e conhecimentos adquiridos durante o programa de estágio da Compass UOL | Back-end Journey (Spring Boot) - AWS Cloud Context.

Utilizando o Java JDK na versão 17, o projeto integrou-se com outros dois microserviços: o Microserviço de Notificação e o Microserviço de Endereço. Ao cadastrar, atualizar ou realizar login de um usuário, uma mensagem é enviada via RabbitMQ para o microserviço de notificação, o qual armazena o evento ocorrido em um banco de dados separado. Quando um usuário é cadastrado, uma mensagem contendo seu CEP é enviada ao microserviço de endereço, que, seguindo a regra de negócio, consome ou não a API ViaCep e armazena o endereço encontrado em seu próprio banco de dados.

Para testar as implementações, inicialmente utilizamos o aplicativo Postman. Posteriormente, foram implementados testes unitários com JUnit 5 e Mockito. No entanto, esses testes foram apenas parcialmente implementados devido a imprevistos no projeto, impactando a cobertura esperada.

## Tecnologias utilizadas
- Java JDK 17

### Dependências
- Spring Boot 3
- Spring Boot Test (inclui o JUnit 5 e Mockito)
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring DevTools
- Spring Doc OpenAPI (Swagger)
- Spring Cloud OpenFeign
- Spring Security (jwt)
- ModelMapper
- Lombok
- RabbitMQ
- Banco de dados MySQL

---
## Regras de negócio 
- Os campos `firstName` e `lastName` devem conter no mínimo 3 caracteres para atender aos requisitos mínimos de nome.
- O campo `email` precisa estar formatado corretamente como um endereço de email válido e não pode ser duplicado, garantindo unicidade na base de dados.
- O campo `cpf` deve aderir ao padrão xxx-xxx-xxx.xx estabelecido e também é necessário garantir sua unicidade no sistema.
- A senha (`password`) deve possuir no mínimo 6 caracteres e ser armazenada de forma criptografada no banco de dados, garantindo a segurança das informações dos usuários.
- O campo `birthdate` precisa ser salvo no banco como o tipo date e tem que estar no formato ISO-8601, entretanto na hora de serializar o objeto e enviar no payload do response esse campo precisa estar no formato dd/mm/aaaa.
- O campo `active` deve ser restrito a valores booleanos, permitindo apenas a indicação de atividade ou inatividade do usuário.
- Apenas os endpoints de login e cadastro de usuário devem ser de livre acesso. O restante deve estar autenticado para realizar requisições
- Criptografar a senha do usuário antes de salvar no banco de dados
- Sempre que ocorrer um evento de criação de cadastro, edição de usuário ou login, uma mensagem deve ser enviada ao MS Notification
---
### Endpoints
- **POST - /v1/login:** Endpoint para realizar login de usuário.
- **POST - /v1/users:** Endpoint para cadastrar um novo usuário.
- **GET - /v1/users/:id:** Endpoint para recuperar os detalhes de um usuário específico.
- **PUT - /v1/users/:id:** Endpoint para atualizar os dados de um usuário específico.
- **PUT - /v1/users/:id/password:** Endpoint para alterar a senha de um usuário específico.

Collections do postman: https://api.postman.com/collections/32204854-d2de2fd3-dc2f-455a-82c1-4f35f3f2eed1?access_key=PMAT-01HQXWXXYNP7ST78EZT97B73RP

### Exemplo de Payload para Cadastro de Usuário

```json
{
  "firstName": "Maria",
  "lastName": "Oliveira",
  "cpf": "000.000.000-00",
  "birthdate": "0000-00-00",
  "email": "maria@email.com",
  "cep": "69999-999",
  "password": "12345678",
  "active": true
}
````

### Exemplo de Payload para Login de Usuário

```json
{
  "email": "maria@email.com",
  "password": "12345678"
}
````
### Exemplo de playload para alterar a senha

```json
{
  "password": "12345678"
}
````

## Problemas


Durante o desenvolvimento do projeto, enfrentei desafios significativos, especialmente ao lidar com a implementação da autenticação usando JWT e a integração da mensageria. Esses processos exigiram um esforço extra, pois surgiram dúvidas complexas que demandaram tempo para serem compreendidas e resolvidas adequadamente.

Inicialmente, planejei estabelecer a comunicação entre os serviços por meio de mensageria. Contudo, após uma cuidadosa análise e discussões, concluí que a abordagem assíncrona entre os microserviços de usuário e endereço poderia não ser a mais eficiente. Diante disso, optei por uma mudança estratégica para uma comunicação síncrona, aproveitando a tecnologia OpenFeign. Essa decisão não apenas exigiu ajustes na arquitetura, mas também afetou a implementação dos outros microserviços, impactando a validação e o tratamento de exceções no microserviço de endereço.

Essas adversidades foram oportunidades valiosas de aprendizado, ressaltando a importância de uma abordagem flexível diante dos desafios e da necessidade de adaptação durante o processo de desenvolvimento. A reflexão e a capacidade de ajustar estratégias foram cruciais para garantir a entrega funcional do projeto. É importante ressaltar que esses desafios também contribuíram para a falta de validação, tratamento de exceções e testes no microserviço de endereço, bem como para a decisão de manter o banco de dados como SQL, em vez de migrá-lo para a AWS.

## Melhorias

Para aprimorar ainda mais o projeto, é essencial concluir os testes unitários e implementar as validações necessárias, especialmente no microserviço de endereço. Além disso, visando uma arquitetura de microserviços mais robusta e escalável, podemos considerar a implementação de um Discovery Server para servir como repositório das instâncias dos microserviços. Isso facilitaria a descoberta e comunicação entre os serviços de forma mais dinâmica. Adicionalmente, a introdução de um Gateway proporcionaria uma distribuição balanceada das requisições entre os microserviços, promovendo uma arquitetura mais resiliente e eficiente. Essas melhorias não apenas fortaleceriam a estrutura do projeto, mas também contribuiriam significativamente para sua escalabilidade e manutenibilidade a longo prazo.




