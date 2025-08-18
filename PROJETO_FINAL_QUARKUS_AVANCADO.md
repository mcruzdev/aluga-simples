# Projeto final Quarkus Avançado

Nesse projeto será necessário a implementação dos dois exercícios, o [Exercício 1](/EXERCISE1.md) e o [Exercício 2](/EXERCISE2.md).

Cada regra abaixo vai contar pontos para o projeto final.

## Regras

### Ajuste no serviço de vehicle

> [!NOTE]
> O microserviço de `vehicle` não deve mais guardar o status de `RENTED`, quem deve controlar isso é o microserviço de `booking`. O microserviço de `vehicle` deve guardar apenas o status `AVAILABLE` ou `UNDER_MAINTENANCE`.

### Ajuste no serviço de booking

> [!NOTE]
> O campo `customerName` na entidade `Booking` deve ser substituída pelo campo `customerId`. Agora, o `customerId` vai ser buscado a partir do **access token** que a aplicação vai receber através do header `Authorization` bearer token.

* As duas aplicações (`vehicle` e `booking`) devem compilar corretamente e seus testes devem ser executados sem erro.


```shell
mvn package
```

* As duas aplicações devem conter testes de integração.

* É obrigatório o uso da extensão `quarkus-oidc` para que em tempo de desenvolvimento a gente tenha o `DevService do Keycloak` funcionando corretamente. O Keycloak vai ser o nosso identity provider, que vai nos permitir ter SSO na aplicação Aluga Simples.

* Autorização utilizando RBAC:
  * Usuários com a role `admin` devem ter acesso a tudo na aplicação.
  * Usuários com a role `user` podem apenas realizar leitura de veículos, ou seja, buscar veículos, buscar veículo por ID, realizar agendamento e ver apenas os seus próprios agendamentos.
  * **Um `user` não pode ver os agendamentos de um outro `user`**.
  * **Um `admin` pode ver tudo de todos**.

* A aplicação `booking` deve se comunicar primeiro com o microserviço de `vehicle` para verificar o status do veículo que está sendo agendado para locação.

* As duas aplicações devem ter pelo menos 65% de cobertura de código.

### Extra, extra, extra (para as próximas aulas)

* Adicionar um novo endpoint para realizar a retirada e a devolução do veículo.
  * Ao efetuar o check-in (retirada do veículo na locadora), o status do Booking deve ser atualizado para `ACTIVE` (Esse status não existe e deve ser adicionado).
  * Você deve adicionar três novos atributos na entidade `Booking`, `canceledAt` (data que foi cancelado), `activedAt` (data que foi ativado, ou seja, o usuário realizou a retirada) e `finishedAt` (data que foi entregue pelo usuário). Você deve atualizar esses status corretamente.
  * Ao realizar a devolução do veículo, o status deve ser       atualizado para `FINISHED`.
  * Todas essas operações devem ser feitas pela role `employee` (essa role não existe no Keycloak, você deve criar ela no Realm `quarkus-app`).

* Adicionar cache distribuído com Redis, na busca de todos os veículos.
  * Quando for adicionado um novo veículo o Cache deve ser invalidado.

* Quando o usuário realizar a retirada do carro, uma mensagem deve ser enviado para o tópico `reserva-ativa` com os dados da entidade `Booking`.

* Quando o usuário realizar a entrega do carro, uma mensagem deve ser enviada para o tópico `reserva-concluida`.

* Quando o usuário realizar o cancelamento da reserva, uma mensagem deve ser enviada para o tópico `reserva-cancelada`.
