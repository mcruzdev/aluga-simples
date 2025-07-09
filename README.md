# Regras de Negócio: Sistema de Aluguel de Carros Simplificado

---

### Visão Geral

Este documento descreve as regras de negócio para um sistema de aluguel de carros simplificado, composto por dois microserviços principais: **Microserviço de Veículos** e **Microserviço de Reservas**. O objetivo é gerenciar a frota de veículos e suas respectivas reservas, garantindo a coordenação entre os dois domínios.

---

### 1. Microserviço de Veículos

Este microserviço é a fonte de verdade para todas as informações relacionadas aos veículos da frota e seu status de disponibilidade.

#### 1.1. Cadastro de Veículos

* Um **veículo** é identificado por um `ID` único.
* Cada veículo possui atributos como `modelo`, `placa`, `ano`, `tipo` (ex: carro, moto, van).
* Ao ser cadastrado, o **status inicial** de um veículo deve ser `DISPONIVEL`.

#### 1.2. Status do Veículo

* O status de um veículo pode ser:
    * `DISPONIVEL`: O veículo está pronto para ser alugado.
    * `ALUGADO`: O veículo está atualmente em uma reserva.
    * `EM_MANUTENCAO`: O veículo está fora de serviço para reparos ou manutenção.
* **Regra de Transição de Status:**
    * Um veículo só pode ser alterado para `ALUGADO` se seu status atual for `DISPONIVEL`.
    * Um veículo só pode ser alterado para `DISPONIVEL` se seu status atual for `ALUGADO` ou `EM_MANUTENCAO`.
    * Um veículo pode ser alterado para `EM_MANUTENCAO` a partir de qualquer status.

#### 1.3. Consulta de Veículos

* Deve ser possível **listar todos os veículos** da frota, com seus detalhes e status.
* Deve ser possível **consultar um veículo específico** pelo seu `ID`, retornando todos os seus detalhes e status.

#### 1.4. Atualização de Veículos

* Deve ser possível **atualizar atributos** de um veículo (modelo, placa, ano, tipo).
* Deve ser possível **alterar o status** de um veículo explicitamente, seguindo as regras de transição.

#### 1.5. Exclusão de Veículos

* Um veículo pode ser **removido da frota**.
* **Restrição:** Um veículo **não pode ser removido** se o seu status atual for `ALUGADO`. Ele deve ser `DISPONIVEL` ou `EM_MANUTENCAO` para ser removido.

---

### 2. Microserviço de Reservas

Este microserviço é responsável por gerenciar o ciclo de vida das reservas, interagindo com o Microserviço de Veículos para verificar e alterar a disponibilidade.

#### 2.1. Criação de Reserva

* Uma **reserva** é identificada por um `ID` único.
* Cada reserva possui atributos como:
    * `ID` do `veículo` (`veiculoId`)
    * `dataInicio` e `dataFim` do aluguel
    * `ID` do `cliente`
    * `status` da reserva (`PENDENTE`, `CONFIRMADA`, `CANCELADA`, `CONCLUIDA`).
* **Fluxo de Criação (Integração Essencial):**
    1.  Ao receber uma solicitação para criar uma reserva, o Microserviço de Reservas deve **consultar o Microserviço de Veículos** para verificar o status do veículo desejado pelo `veiculoId`.
    2.  **Se o veículo estiver `DISPONIVEL`**:
        * A reserva é criada com o status `PENDENTE`.
        * O Microserviço de Reservas deve **solicitar ao Microserviço de Veículos** que altere o status do veículo para `ALUGADO`.
        * Se a alteração de status no veículo for bem-sucedida, o status da reserva é atualizado para `CONFIRMADA`.
        * Uma resposta de sucesso é enviada ao cliente.
    3.  **Se o veículo não estiver `DISPONIVEL`**:
        * A reserva **não é criada**.
        * Uma resposta de erro é enviada ao cliente, indicando que o veículo não está disponível.
* **Restrição:** Não é possível criar uma reserva com `dataFim` anterior ou igual à `dataInicio`.

#### 2.2. Status da Reserva

* O status de uma reserva pode ser:
    * `PENDENTE`: Reserva em processo de criação, aguardando confirmação do status do veículo.
    * `CONFIRMADA`: Reserva criada e veículo alugado.
    * `CANCELADA`: Reserva foi explicitamente cancelada pelo cliente ou sistema.
    * `CONCLUIDA`: O período de aluguel terminou e o veículo foi devolvido.
* **Regras de Transição de Status:**
    * `PENDENTE` -> `CONFIRMADA` (após alteração de status do veículo)
    * `CONFIRMADA` -> `CANCELADA`
    * `CONFIRMADA` -> `CONCLUIDA`
    * Uma reserva `CANCELADA` ou `CONCLUIDA` não pode ter seu status alterado.

#### 2.3. Cancelamento de Reserva

* Ao receber uma solicitação de cancelamento de uma reserva com status `CONFIRMADA`:
    1.  O status da reserva é alterado para `CANCELADA`.
    2.  O Microserviço de Reservas deve **solicitar ao Microserviço de Veículos** que altere o status do veículo associado à reserva para `DISPONIVEL`.
* **Restrição:** Somente reservas com status `CONFIRMADA` podem ser canceladas.

#### 2.4. Conclusão de Reserva

* Ao receber uma solicitação de conclusão de uma reserva com status `CONFIRMADA`:
    1.  O status da reserva é alterado para `CONCLUIDA`.
    2.  O Microserviço de Reservas deve **solicitar ao Microserviço de Veículos** que altere o status do veículo associado à reserva para `DISPONIVEL`.
* **Restrição:** Somente reservas com status `CONFIRMADA` podem ser concluídas.

#### 2.5. Consulta de Reservas

* Deve ser possível **listar todas as reservas**, com seus detalhes e status.
* Deve ser possível **consultar uma reserva específica** pelo seu `ID`, retornando todos os seus detalhes.
* Deve ser possível **consultar reservas por `veiculoId`** ou `clienteId`.
