
# 🚗 Exercício: Criação do Microserviço de Reservas (`book`) com Testes

Visão Geral

Neste exercício, você irá criar o microserviço `book`, responsável por gerenciar as reservas de veículos no sistema AlugaSimples.

O foco deste exercício está em:

- Modelar o domínio de reservas
- Aplicar regras de negócio
- Criar testes de unidade e de integração

---

Entidade Booking

public class Booking {
    Long id;
    Long vehicleId;
    String customerName;
    LocalDate startDate;
    LocalDate endDate;
    BookingStatus status;
}

Enum BookingStatus

public enum BookingStatus {
    CREATED,
    CANCELED,
    FINISHED
}

> [!NOTE]
> Você pode aceitar qualquer valor no campo `vehicleId`.

---

Regras de Negócio

1. A reserva deve ser criada apenas se:
   - O vehicleId for válido (simulado, veja abaixo)
   - O startDate for hoje ou no futuro
   - O endDate for igual ou posterior ao startDate

2. Ao criar a reserva:
   - O status inicial deve ser `CREATED`

3. Cancelar ou finalizar a reserva:
   - Só é permitido se a reserva estiver com status `CREATED`
   - O status passa a ser CANCELED ou FINISHED

4. Não é necessário se preocupar com o status do veículo neste momento (vamos ver isso futuramente)

## Endpoints REST esperados

| Método | Caminho                  | Descrição                                     |
|--------|-------------------------|------------------------------------------------|
| POST   | /bookings               | Cria uma nova reserva                          |
| PATCH  | /bookings/{id}          | Altera o status de uma uma reserva             |
| GET    | /bookings/{id}          | Consulta reserva por ID                        |
| GET    | /bookings               | Lista todas as reservas                        |

---

### Parte 1 — Testes de Unidade

Crie testes unitários da BookingService:

Cenários esperados:

- Criar reserva com datas válidas e veículo disponível → sucesso
- Criar reserva com startDate no passado → falha
- Criar reserva com endDate antes de startDate → falha
- Criar reserva com veículo indisponível → falha
- Cancelar reserva com status CREATED → sucesso
- Cancelar reserva já cancelada ou finalizada → falha
- Finalizar reserva com status CREATED → sucesso
- Finalizar reserva já cancelada ou finalizada → falha

---

Parte 2 — Testes de Integração

Crie testes com @QuarkusTest e banco em memória (H2):

O que testar:

- Criar reserva com dados válidos
- Criar reserva com data inválida
- Cancelar reserva
- Tentar cancelar reserva já cancelada
- Finalizar reserva
- Seja crítico e adicione mais testes

---

Parte 3 - Testes de unidade do microserviço de vehicles

- Testar todas as regras de negócio da classe Vehicle (mudança de status)
- Os campos da classe Vehicle (`model`, `brand`, `engine` e `engine`) **não** devem ser nulos ou vazios.

Parte 4 - Testes de integração do microserviço vehicles

- Adicione mais testes de integração:
  - Buscar todos deve retornar 200
  - Mudar o status de `UNDER_MAINTENANCE` para `RENTED` deve retornar o status code 409
  - Testar todos os possíveis casos que podem retornar `400` (`erro de validação no vehicle, ou seja, dados inválidos passados pelo cliente HTTP`) e `404` (quando o `vehicle` não for encontrado).

Observações:

- Use Panache com PanacheEntity ou PanacheEntityBase para facilitar.
- Separe bem as responsabilidades: BookingService, BookingResource, BookingRepository.
- Nomeie bem seus testes e use `@DisplayName` quando quiser deixar mais claro o que está sendo testado.

---

Bônus (opcional, pois não vimos na aula)

- Adicione paginação no endpoint de listagem
- Adicione validação para não permitir sobreposição de reservas para o mesmo veículo
