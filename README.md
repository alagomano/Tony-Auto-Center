# 🔧 Tony Auto Center — Sistema de Gerenciamento para Oficina Mecânica

Sistema de gerenciamento para oficinas mecânicas de pequeno porte desenvolvido em **Java 25**, utilizando arquitetura em camadas, persistência de dados com **JDBC** e **MySQL**. O projeto tem como objetivo substituir controles manuais por uma aplicação organizada, aplicando boas práticas de desenvolvimento orientado a objetos e modelagem de domínio.

> **Status:** Em desenvolvimento (MVP funcional)

---

# 📋 Índice

- [📖 Visão Geral](#-visão-geral)
- [🚀 Funcionalidades](#-funcionalidades)
- [🏗 Arquitetura](#-arquitetura)
- [📦 Modelagem do Domínio](#-modelagem-do-domínio)
- [🛠 Tecnologias](#-tecnologias)
- [📁 Estrutura do Projeto](#-estrutura-do-projeto)
- [📐 Regras de Negócio](#-regras-de-negócio)
- [🗺 Roadmap](#-roadmap)
- [📚 Aprendizados](#-aprendizados)
- [📈 Status Atual](#-status-atual)

---

# 📖 Visão Geral

O Tony Auto Center nasceu com o objetivo de resolver problemas comuns encontrados em oficinas mecânicas que ainda realizam seus controles de forma manual.

Entre eles:

| Problema | Solução |
|----------|----------|
| Perda do histórico de serviços | Histórico persistido por veículo |
| Dificuldade de localizar informações | Busca por placa e identificação da ordem de serviço |
| Controle manual suscetível a erros | Cadastro centralizado de clientes, veículos e ordens |
| Falta de organização | Fluxo controlado por regras de negócio |

O projeto também serve como estudo prático de desenvolvimento Back-end utilizando Java, aplicando conceitos de Programação Orientada a Objetos, persistência relacional e arquitetura em camadas.

---

# 🚀 Funcionalidades

## Clientes

- Cadastro de clientes
- Atualização de dados
- Remoção de clientes
- Busca por CPF
- Listagem de clientes

---

## Veículos

- Cadastro vinculado a um cliente
- Atualização
- Remoção
- Busca por placa
- Histórico de ordens de serviço

---

## Ordens de Serviço

- Abertura de ordem
- Atualização de informações
- Controle de status
- Listagem
- Busca por ID
- Associação com veículo

Estados da ordem:

- OPEN
- IN_PROGRESS
- FINISHED
- DELIVERED

---

## Itens da Ordem de Serviço

- Adicionar itens
- Cálculo automático do subtotal
- Cálculo automático do valor total da ordem

---

# 🏗 Arquitetura

O projeto utiliza arquitetura em camadas.

```text
Application
      │
      ▼
Services
      │
      ▼
DAO (JDBC)
      │
      ▼
MySQL
```

Cada camada possui responsabilidade bem definida:

- **Application:** interação com o usuário e execução dos testes.
- **Services:** regras de negócio e validações.
- **DAO:** persistência e consultas utilizando JDBC.
- **Database:** armazenamento dos dados.

---

# 📦 Modelagem do Domínio

## Entidades

### Client

- id
- name
- cpf
- phone
- address

Relacionamento:

- 1 Cliente → N Veículos

---

### Vehicle

- id
- plate
- brand
- model
- year

Relacionamento:

- 1 Veículo → N Ordens de Serviço

---

### ServiceOrder

- id
- entryDate
- exitDate
- problemDescription
- observations
- status
- totalValue

Relacionamento:

- 1 Ordem → N Itens de Serviço

---

### ServiceItem

- id
- description
- quantity
- unitValue

Subtotal calculado automaticamente.

---

### OrderStatus

- OPEN
- IN_PROGRESS
- FINISHED
- DELIVERED

---

# 🛠 Tecnologias

## Linguagem

- Java 25

## Persistência

- JDBC
- MySQL

## Build

- Maven

## Controle de Versão

- Git
- GitHub

---

# 📁 Estrutura do Projeto

```text
src
├── application
│   ├── Main.java
│   └── tests
│
├── db
│
├── model
│   ├── dao
│   │   ├── impl
│   │   └── DaoFactory
│   │
│   ├── entities
│   │
│   ├── enums
│   │
│   ├── exception
│   │
│   └── services
│
└── resources
```

---

# 📐 Regras de Negócio

- Um cliente pode possuir vários veículos.
- Um veículo pertence a apenas um cliente.
- Um veículo pode possuir várias ordens de serviço.
- Apenas uma ordem pode estar ativa (OPEN ou IN_PROGRESS) para um mesmo veículo.
- Cada ordem pode possuir vários itens de serviço.
- O subtotal de cada item é calculado automaticamente.
- O valor total da ordem corresponde à soma dos subtotais.
- A data de entrada é registrada automaticamente na abertura da ordem.
- A data de saída é registrada automaticamente ao finalizar a ordem.
- Ordens entregues não podem voltar para estados anteriores.

---

# 🗺 Roadmap

## ✅ Versão 1 — Java + JDBC

- Modelagem do domínio
- Arquitetura em camadas
- Persistência com JDBC
- CRUD de Clientes
- CRUD de Veículos
- CRUD de Ordens de Serviço
- Gerenciamento de Itens da Ordem

---

## 🔄 Versão 2 — Spring Boot

Planejado:

- Migração para Spring Boot
- Spring Data JPA
- Hibernate
- API REST
- DTOs
- Bean Validation
- Tratamento global de exceções
- Documentação com OpenAPI/Swagger

---

## 🔄 Versão 3

Planejado:

- Autenticação
- Controle de usuários
- Dashboard
- Relatórios
- Upload de imagens
- Deploy em nuvem

---

# 📚 Aprendizados

Durante o desenvolvimento deste projeto foram aplicados conceitos como:

- Programação Orientada a Objetos
- Encapsulamento
- Composição entre entidades
- Camada de Serviço
- DAO Pattern
- Persistência de dados com JDBC
- Modelagem de banco de dados relacional
- Tratamento de exceções
- Regras de negócio
- Arquitetura em camadas
- Maven
- Git e GitHub

---

# 📈 Status Atual

**Versão atual:** MVP em desenvolvimento.

### Implementado

- Modelagem completa do domínio
- Persistência com JDBC
- CRUD de Clientes
- CRUD de Veículos
- CRUD de Ordens de Serviço
- Controle de status das ordens
- Associação entre Clientes, Veículos, Ordens de Serviço e Itens
- Cálculo automático de subtotais e valor total

### Próximos passos

- Validar todos os fluxos da aplicação e refinar as regras de negócio
- Implementar uma interface de console completa para navegação e testes do sistema
- Criar testes para os principais casos de uso da camada de serviço
- Preparar a migração do projeto para Spring Boot utilizando JPA/Hibernate