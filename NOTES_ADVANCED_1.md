# Quarkus avançado: Aula 1

1. Realizar uma visão geral no como a aplicação está hoje. Devemos ver o que temos de problemas, etc.
    1.1 - Nós estamos salvando um booking permitindo o overlapping;

2. Comentar sobre a responsabilidade de cada microserviço.
3. Vamos deixar que o serviço de booking responsa se o carro está alugado ou não.
    3.1 - Nós podemos manter o status lá, mas não vamos alterar quando for realizado um agendamento, até por que o agendamento pode estar no futuro.
4. Discutir mais decisões se necessário.

## ACID

---
A: Atomicidade

C: Consitência

I: Isolamento

D: Durabilidade

---

## Isolamento

O isolamento é o conceito mais complicado e cada banco de dados possui níveis de isolamento. Dependendo do nível de isolamento você pode ter alguns problemas e anomalias.

> https://vladmihalcea.com/a-beginners-guide-to-transaction-isolation-levels-in-enterprise-java/

### Transactional

O `@Transactional` é a anotação que usamos para informar o limite da transação.