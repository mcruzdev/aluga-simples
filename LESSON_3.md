# 🛡️ Exercício: JWT e Interceptadores

## 📘 Contexto

Usuários devem se autenticar usando **JWT**, e cada usuário possui um papel (role): USUARIO, FUNCIONARIO ou ADMIN.

Você deve usar o microserviço de veículos para isso para mantermos a simplicidade (vamos centralizar a autenticação e autorização nas próximas aulas).

## 🎯 Objetivos

Você deve implementar a **autenticação** e a **autorização** utilizando **JWT do Quarkus**.

## Parte 1

### 1.A aplicação veículo, precisa agora permitir a criação de usuários com

- id
- username
- password (salvo de forma segura, com hashing usando BCrypt)
- nome
- email
- role (Enum: USUARIO, FUNCIONARIO, ADMIN)

### 2. Crie um endpoint POST /sign-up

Permite o cadastro de um novo usuário.

### 3. Implemente um endpoint POST /sign-in

Permite a validação da senha e e-mail do usuário e crie um token JWT contendo o `upn` o ID do usuário na aplicação. O token deve ser assinado apenas, não é necessário criptografá-lo.

### 4. Regras

- Um usuário com role `ADMIN` pode fazer tudo na aplicação
- Um usuário com role `FUNCIONARIO` pode fazer tudo menos criar um veículo
- Um usuário com role `USUARIO` tem acesso apenas de leitura

## ✅ Critérios de avaliação

- [ ] Cadastro de usuários funcionando.
- [ ] Filtro de autenticação via Basic Auth implementado.
