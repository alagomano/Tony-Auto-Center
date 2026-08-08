# 🔧 Tony Auto Center — Sistema de Gerenciamento para Oficina Mecânica

Sistema de gerenciamento para oficinas mecânicas de pequeno porte desenvolvido em **Java 25**, com foco em arquitetura em camadas, regras de negócio e persistência de dados.

O projeto nasceu como uma aplicação Java utilizando **JDBC + MySQL** e evoluiu progressivamente para **JPA/Hibernate**, mantendo a mesma modelagem de domínio e regras de negócio. A próxima etapa consiste na migração para **Spring Boot e Spring Data JPA**, transformando a aplicação em uma API REST.

O objetivo é substituir controles manuais por uma aplicação organizada e escalável, aplicando boas práticas de desenvolvimento orientado a objetos, persistência de dados, modelagem de domínio e arquitetura de software.

> **Status:** Em desenvolvimento — persistência migrada para JPA/Hibernate. Migração para Spring Boot em andamento.

---

# 📋 Índice

* [📖 Visão Geral](#-visão-geral)
* [🚀 Funcionalidades](#-funcionalidades)
* [🏗 Arquitetura](#-arquitetura)
* [📦 Modelagem do Domínio](#-modelagem-do-domínio)
* [🛠 Tecnologias](#-tecnologias)
* [📁 Estrutura do Projeto](#-estrutura-do-projeto)
* [📐 Regras de Negócio](#-regras-de-negócio)
* [🗺 Evolução do Projeto](#-evolução-do-projeto)
* [📚 Aprendizados](#-aprendizados)
* [📈 Status Atual](#-status-atual)

---

# 📖 Visão Geral

O Tony Auto Center nasceu com o objetivo de resolver problemas comuns encontrados em oficinas mecânicas que ainda realizam seus controles de forma manual.

Entre eles:

| Problema                             | Solução                                              |
| ------------------------------------ | ---------------------------------------------------- |
| Perda do histórico de serviços       | Histórico persistido por veículo                     |
| Dificuldade de localizar informações | Busca por placa e identificação da ordem de serviço  |
| Controle manual suscetível a erros   | Cadastro centralizado de clientes, veículos e ordens |
| Falta de organização                 | Fluxo controlado por regras de negócio               |

O projeto também serve como estudo prático de desenvolvimento **Back-end com Java**, aplicando conceitos de Programação Orientada a Objetos, persistência relacional, arquitetura em camadas e evolução incremental de uma aplicação.

A aplicação foi desenvolvida de forma incremental, permitindo a evolução da camada de persistência sem alterar o domínio principal do sistema.

---

# 🚀 Funcionalidades

## Clientes

* Cadastro de clientes
* Atualização de dados
* Remoção de clientes
* Busca por CPF
* Listagem de clientes

---

## Veículos

* Cadastro vinculado a um cliente
* Atualização
* Remoção
* Busca por placa
* Histórico de ordens de serviço

---

## Ordens de Serviço

* Abertura de ordem
* Atualização de informações
* Controle de status
* Listagem
* Busca por ID
* Associação com veículo

Estados da ordem:

* `OPEN`
* `IN_PROGRESS`
* `FINISHED`
* `DELIVERED`

---

## Itens da Ordem de Serviço

* Adicionar, atualizar e remover itens
* Cálculo automático do subtotal
* Cálculo automático do valor total da ordem

---

# 🏗 Arquitetura

A versão atual utiliza uma arquitetura em camadas:

```text
Application
      │
      ▼
Services
      │
      ▼
DAO
      │
      ▼
JPA / Hibernate
      │
      ▼
MySQL
```

Cada camada possui responsabilidade bem definida:

* **Application:** interação com o usuário e execução dos fluxos da aplicação.
* **Services:** implementação das regras de negócio e validações.
* **DAO:** acesso e persistência dos dados.
* **JPA/Hibernate:** mapeamento objeto-relacional e gerenciamento da persistência.
* **Database:** armazenamento dos dados.

A arquitetura foi projetada para permitir a evolução da tecnologia de persistência sem acoplar as regras de negócio diretamente ao banco de dados.

---

# 📦 Modelagem do Domínio

As entidades são mapeadas utilizando as principais funcionalidades da especificação **Jakarta Persistence (JPA)**, com **Hibernate** como provedor de persistência.

## Client

* `id`
* `name`
* `cpf`
* `phone`
* `address`
* `vehicles`

Relacionamento:

* 1 Cliente → N Veículos (`@OneToMany`)
* Cascade e `orphanRemoval` configurados conforme a relação

---

## Vehicle

* `id`
* `plate`
* `brand`
* `model`
* `year`

Relacionamento:

* N Veículos → 1 Cliente (`@ManyToOne`)
* 1 Veículo → N Ordens de Serviço (`@OneToMany`)

---

## ServiceOrder

* `id`
* `entryDate`
* `exitDate`
* `problemDescription`
* `observations`
* `status`
* `totalValue`

Relacionamento:

* N Ordens → 1 Veículo (`@ManyToOne`)
* 1 Ordem → N Itens de Serviço (`@OneToMany`)

---

## ServiceItem

* `id`
* `description`
* `quantity`
* `unitValue`

Relacionamento:

* N Itens → 1 Ordem de Serviço (`@ManyToOne`)

O subtotal é calculado automaticamente com base na quantidade e no valor unitário.

---

## OrderStatus

* `OPEN`
* `IN_PROGRESS`
* `FINISHED`
* `DELIVERED`

---

# 🛠 Tecnologias

## Linguagem

* Java 25

## Persistência

* Jakarta Persistence (JPA)
* Hibernate ORM
* MySQL

## Build

* Maven

## Controle de Versão

* Git
* GitHub

## Próxima etapa

* Spring Boot
* Spring Data JPA
* API REST
* Bean Validation
* DTOs
* OpenAPI/Swagger

---

# 📁 Estrutura do Projeto

```text
src
├── main
│   ├── java
│   │   ├── application
│   │   │   ├── Main.java
│   │   │   └── tests
│   │   │       ├── TestClient.java
│   │   │       ├── TestVehicle.java
│   │   │       └── TestServiceOrder.java
│   │   │
│   │   └── model
│   │       ├── dao
│   │       │   ├── impl
│   │       │   │   ├── ClientDaoJPA.java
│   │       │   │   ├── VehicleDaoJPA.java
│   │       │   │   ├── ServiceOrderDaoJPA.java
│   │       │   │   └── ServiceItemDaoJPA.java
│   │       │   │
│   │       │   ├── ClientDao.java
│   │       │   ├── VehicleDao.java
│   │       │   ├── ServiceOrderDao.java
│   │       │   ├── ServiceItemDao.java
│   │       │   └── DaoFactory.java
│   │       │
│   │       ├── entities
│   │       │   ├── Client.java
│   │       │   ├── Vehicle.java
│   │       │   ├── ServiceOrder.java
│   │       │   └── ServiceItem.java
│   │       │
│   │       ├── enums
│   │       │   └── OrderStatus.java
│   │       │
│   │       ├── exception
│   │       │   ├── DbException.java
│   │       │   ├── DomainException.java
│   │       │   └── ServiceException.java
│   │       │
│   │       ├── infrastructure
│   │       │   └── JPAUtil.java
│   │       │
│   │       └── services
│   │           ├── ClientService.java
│   │           ├── VehicleService.java
│   │           └── ServiceOrderService.java
│   │
│   └── resources
│       ├── META-INF
│       │   └── persistence.xml
│       └── db.properties
│
├── .gitignore
├── pom.xml
└── README.md
```

> **Testes:** as classes em `application/tests` executam e validam manualmente os principais fluxos das camadas de serviço para clientes, veículos e ordens de serviço.

---

# 📐 Regras de Negócio

* Um cliente pode possuir vários veículos.
* Um veículo pertence a apenas um cliente.
* Um veículo pode possuir várias ordens de serviço.
* Apenas uma ordem pode estar ativa (`OPEN` ou `IN_PROGRESS`) para um mesmo veículo.
* Cada ordem pode possuir vários itens de serviço.
* O subtotal de cada item é calculado automaticamente.
* O valor total da ordem corresponde à soma dos subtotais.
* A data de entrada é registrada automaticamente na abertura da ordem.
* A data de saída é registrada automaticamente ao finalizar a ordem.
* Ordens entregues não podem voltar para estados anteriores.

---

# 🗺 Evolução do Projeto

O projeto é desenvolvido de forma incremental, utilizando cada etapa como base para a próxima evolução tecnológica.

## ✅ Versão 1 — Java + JDBC

A primeira implementação foi desenvolvida utilizando **JDBC + MySQL**, com arquitetura em camadas e o padrão DAO.

Principais objetivos dessa etapa:

* Modelagem inicial do domínio
* Arquitetura em camadas
* Persistência utilizando JDBC
* CRUD de Clientes
* CRUD de Veículos
* CRUD de Ordens de Serviço
* Gerenciamento de Itens da Ordem
* Implementação das primeiras regras de negócio
* Uso de `PreparedStatement`
* Gerenciamento de `ResultSet`
* Controle de transações

Essa etapa estabeleceu a base estrutural e as regras de negócio utilizadas nas versões seguintes.

---

## ✅ Versão 1.5 — Java + JPA/Hibernate

A segunda etapa substituiu a implementação de persistência baseada em JDBC por **JPA/Hibernate**.

Principais mudanças:

* Entidades mapeadas com anotações JPA
* Relacionamentos entre entidades
* `EntityManager`
* Consultas utilizando JPQL
* Cascade
* `orphanRemoval`
* Lazy/Eager Loading
* DAOs adaptados para JPA
* Centralização do gerenciamento de persistência
* Revalidação das regras de negócio após a migração
* Tratamento de exceções relacionadas à persistência

A migração foi realizada mantendo o domínio e as principais responsabilidades da aplicação, permitindo comparar na prática diferentes abordagens de persistência em Java.

---

## 🔄 Versão 2 — Spring Boot + Spring Data JPA

Próxima etapa da evolução da aplicação.

Planejado:

* Migração para Spring Boot
* Spring Data JPA
* Injeção de dependências com Spring
* API REST
* Controllers
* DTOs
* Bean Validation
* Tratamento global de exceções
* Documentação com OpenAPI/Swagger
* Configuração externa da aplicação

O objetivo dessa etapa é transformar a aplicação atual em uma aplicação web backend estruturada para consumo por clientes externos.

---

## 🔄 Versão 3 — Evolução da Aplicação

Após a construção da API REST, estão planejadas funcionalidades voltadas à utilização da aplicação em um cenário mais próximo de produção:

* Autenticação e autorização
* Controle de usuários
* Dashboard
* Relatórios
* Upload de imagens
* Logs e monitoramento
* Deploy em nuvem
* Melhorias de segurança
* Testes automatizados

---

# 📚 Aprendizados

Durante o desenvolvimento deste projeto foram aplicados conceitos como:

* Programação Orientada a Objetos
* Encapsulamento
* Composição entre entidades
* Camada de Serviço
* DAO Pattern
* Persistência de dados com JDBC
* Persistência e mapeamento objeto-relacional com JPA/Hibernate
* `EntityManager`
* JPQL
* Relacionamentos entre entidades
* Cascade
* `orphanRemoval`
* Lazy/Eager Loading
* Modelagem de banco de dados relacional
* Tratamento de exceções
* Regras de negócio
* Arquitetura em camadas
* Maven
* Git e GitHub

A evolução do projeto também permitiu compreender na prática as diferenças entre acesso direto ao banco de dados utilizando JDBC e uma abordagem baseada em ORM utilizando JPA/Hibernate.

---

# 📈 Status Atual

**Versão atual:** Java 25 + JPA/Hibernate + MySQL.

### Implementado

* Modelagem completa do domínio
* Entidades mapeadas com JPA
* Persistência utilizando `EntityManager`
* Consultas utilizando JPQL
* CRUD de Clientes
* CRUD de Veículos
* CRUD de Ordens de Serviço
* Gerenciamento de Itens da Ordem de Serviço
* Controle de status das ordens
* Associação entre Clientes, Veículos, Ordens de Serviço e Itens
* Cálculo automático de subtotais e valor total
* Regras de negócio para controle das ordens
* Tratamento de exceções
* Migração completa da camada de persistência de JDBC para JPA/Hibernate

### Próximos passos

* Refinar regras de negócio conforme necessário
* Criar testes automatizados para os principais casos de uso
* Iniciar a migração para Spring Boot
* Adotar Spring Data JPA
* Transformar a aplicação em uma API REST
* Implementar DTOs e Bean Validation
* Adicionar documentação da API
