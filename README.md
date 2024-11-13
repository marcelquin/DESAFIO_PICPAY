PROJETO
* Projeto de uma siumulação simplificada de banco com sistema de autorização aletoria de pagamento ou não, a fim de demonstrar conhecimento no uso das tecnologias presentes no mesmo.

TECNOLOGIAS
* Docker
* RabbitMq
* Spring boot 3.2.4
* PostgreSQL
* JWT security

REQUIZITOS

RABBITMQ
* Executar o docker compose que o RabbitMQ seja iniciado
* Configurar o mesmo com as querue presentes
* authorization-request-queue
  authorization-request-queue-rout-key
* authorization-response-queue
  authorization-response-queue-rout-key 


POSSIVEIS FUTURAS ATUALIZAÇÕES

* No momento o projeto esta para rodar localmente, testes em docker agendados e adequações no docker compose serão feitas
* Possivel separação do modulo de segurança levando o mesmo para um micro serviço
* Possivel Adição do Eureka gateway para mapeamento de micro serviços
* Possivel melhoria ou adição de novos recursos de transferencia 
