PROJETO
* Projeto de uma siumulação simplificada de banco com sistema de autorização aletoria de pagamento ou não, a fim de demonstrar conhecimento no uso das tecnologias presentes no mesmo inspirado no desafio presente no link https://github.com/PicPay/picpay-desafio-backend.
* O mesmo conta com o swagger funcional para testes de metodos, presente na pagina http://localhost:8080/swagger-ui/index.html#/ 

TECNOLOGIAS
* Docker
* RabbitMq
* Spring boot 3.2.4
* PostgreSQL
* JWT security
* Swagger

REQUIZITOS

RABBITMQ
* Executar o docker compose que o RabbitMQ seja iniciado
* acesse a pagina http://localhost:15672/
* ultilize o usuario padrão guest para user e password(caso não tenha customizado no docker compose)
* Configurar o mesmo com as querue e keys presentes nos diagramas do projeto na pasta Doc


POSSIVEIS FUTURAS ATUALIZAÇÕES

* No momento o projeto esta para rodar localmente, testes em docker agendados e adequações no docker compose serão feitas
* Possivel separação do modulo de segurança levando o mesmo para um micro serviço
* Possivel Adição do Eureka gateway para mapeamento de micro serviços
* Possivel melhoria ou adição de novos recursos de transferencia
* Buscar ou adicionar micro serviço de envio de email, assim como de autorização de pagamentos
