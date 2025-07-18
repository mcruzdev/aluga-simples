# Regras de Negócio: AlugaSimples

---

## Visão Geral

Este documento descreve as regras de negócio para um sistema de aluguel de carros simplificado, composto por dois microserviços principais: **vehicles** e **book**. O objetivo é gerenciar a frota de veículos e suas respectivas reservas, garantindo a coordenação entre os dois domínios.

---

### 1. Microserviço de Veículos (vehicles)

Este microserviço é a fonte de verdade para todas as informações relacionadas aos veículos da frota e seu status de disponibilidade.

#### 1.1. Cadastro de Veículos

* Um **veículo** é identificado por um `ID` único.
* Cada veículo possui atributos como `id`, `brand`, `year`, `engine`, `status`, `model`.
* Ao ser cadastrado, o **status inicial** de um veículo deve ser `AVAILABLE`.

Exemplo de um veículo pós criação:

```json
 {
   "brand": "Fiat",
   "id": 1,
   "model": "Mobi",
   "status": "AVAILABLE",
   "year": 2022,
   "engine": "1.0"
 }
```

#### 1.2. Status do Veículo

* O status de um veículo pode ser:
  * `AVAILABLE`: O veículo está pronto para ser alugado.
    * `RENTED`: O veículo está atualmente em uma reserva.
    * `UNDER_MAINTENANCE`: O veículo está fora de serviço para reparos ou manutenção.
* **Regra de Transição de Status:**
  * Um veículo só pode ser alterado para `RENTED` se seu status atual for `AVAILABLE`.
    * Um veículo só pode ser alterado para `AVAILABLE` se seu status atual for `RENTED` ou `UNDER_MAINTENANCE`.
    * Um veículo pode ser alterado para `UNDER_MAINTENANCE` a partir de qualquer status.

#### 1.3. Consulta de Veículos

* Deve ser possível **listar todos os veículos** da frota, com seus detalhes e status.
* Deve ser possível **consultar um veículo específico** pelo seu `id`, retornando todos os seus detalhes e status.
* Deve ser possível na consulta do veículo termos o seguinte campo `carTitle` que é composto por `brand`, `model` e `engine`, por exemplo: `Fiat Mobi 1.0`.

#### 1.4. Atualização de Veículos

* Deve ser possível **atualizar atributos** de um veículo.
* Deve ser possível **alterar o status** de um veículo explicitamente, seguindo as regras de transição.

#### 1.5. Exclusão de Veículos

* Um veículo pode ser **removido da frota**.
* **Restrição:** Um veículo **não pode ser removido** se o seu status atual for `ALUGADO`. Ele deve ser `DISPONIVEL` ou `EM_MANUTENCAO` para ser removido.
