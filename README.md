# 🔧 Tony Auto Center — Sistema de Gestão para Oficina Mecânica

Sistema web de gestão para oficina mecânica de pequeno porte. Centraliza o controle de clientes, veículos, ordens de serviço e histórico de manutenção, substituindo controles manuais por uma plataforma digital organizada e escalável.

---

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Funcionalidades da MVP](#funcionalidades-da-mvp)
- [Requisitos Funcionais](#requisitos-funcionais)
- [Requisitos Não Funcionais](#requisitos-não-funcionais)
- [Regras de Negócio](#regras-de-negócio)
- [Modelagem do Domínio](#modelagem-do-domínio)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Roadmap de Desenvolvimento](#roadmap-de-desenvolvimento)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Status Atual](#status-atual)

---

## Visão Geral

O **Tony Auto Center** é uma oficina mecânica de pequeno porte administrada pelo próprio proprietário. O sistema resolve problemas comuns de oficinas que ainda operam de forma manual:

| Problema atual | Solução proposta |
|---|---|
| Perda de histórico de serviços | Histórico persistido por veículo, consultável pela placa |
| Falta de organização dos atendimentos | Ordens de serviço com status e datas controlados |
| Controle manual suscetível a erros | Cadastro digital centralizado |
| Dificuldade de localizar informações | Busca principal pela placa do veículo |
| Ausência de rastreabilidade | Todos os serviços vinculados à OS e ao veículo |

---

## Funcionalidades da MVP

### 1. Cadastro de Clientes
- Nome, telefone e e-mail

### 2. Cadastro de Veículos
- Placa, marca, modelo, ano e quilometragem
- Vinculado a um cliente
- **A placa é o identificador principal de busca**

### 3. Ordem de Serviço
- Data de entrada, data de saída, descrição do problema, observações, status e valor total
- Controle de status via enum `OrderStatus`

### 4. Itens de Serviço
- Cada OS pode ter vários serviços (ex.: troca de óleo, alinhamento, freios)
- Descrição, quantidade, valor unitário e subtotal calculado automaticamente

### 5. Histórico do Veículo
- Busca pela placa retorna todas as OSs anteriores e serviços realizados

---

## Requisitos Funcionais

| ID | Funcionalidade | Prioridade |
|---|---|---|
| RF-01 | Cadastrar cliente | Alta |
| RF-02 | Editar cliente | Média |
| RF-03 | Listar clientes | Alta |
| RF-04 | Cadastrar veículo vinculado a um cliente | Alta |
| RF-05 | Editar veículo | Média |
| RF-06 | Buscar veículo por placa (retorna OS ativa + histórico) | Alta |
| RF-07 | Abrir ordem de serviço para um veículo | Alta |
| RF-08 | Atualizar status da OS conforme transições permitidas | Alta |
| RF-09 | Adicionar itens de serviço a uma OS | Alta |
| RF-10 | Calcular subtotal por item e total da OS automaticamente | Alta |
| RF-11 | Registrar data de saída ao fechar a OS | Alta |
| RF-12 | Consultar histórico completo de manutenção do veículo | Alta |
| RF-13 | Listar ordens de serviço filtrando por status | Média |

---

## Requisitos Não Funcionais

| ID | Categoria | Descrição |
|---|---|---|
| RNF-01 | Usabilidade | Interface intuitiva, sem necessidade de treinamento especializado |
| RNF-02 | Desempenho | Busca por placa em menos de 2 segundos |
| RNF-03 | Disponibilidade | Disponível no horário de funcionamento da oficina (seg–sáb, 8h–18h) |
| RNF-04 | Manutenibilidade | Arquitetura em camadas (Controller → Service → Repository → Database) |
| RNF-05 | Escalabilidade | Modelagem preparada para autenticação, relatórios e upload de fotos (V2/V3) |
| RNF-06 | Portabilidade | Acessível via navegador em desktop e mobile |
| RNF-07 | Confiabilidade | Integridade referencial garantida pelo banco de dados |
| RNF-08 | Segurança | Autenticação e controle de acesso planejados para a V2 (fora da MVP) |
| RNF-09 | Tecnologia | Java + Spring Boot + PostgreSQL + HTML/CSS/JS → React |
| RNF-10 | Padronização | API REST com códigos HTTP semânticos, DTOs e tratamento centralizado de exceções |

---

## Regras de Negócio

### Relacionamentos
- Um cliente pode ter zero ou mais veículos
- Um veículo pertence a exatamente um cliente
- Um veículo pode ter zero ou mais ordens de serviço
- Uma OS pertence a exatamente um veículo
- Uma OS pode ter um ou mais itens de serviço

### Regras Operacionais
- `RN-07` A placa é única no sistema e é o identificador principal de busca
- `RN-08` Não é permitido abrir nova OS para veículo com OS em status `OPEN` ou `IN_PROGRESS`
- `RN-09` Subtotal do item = `quantidade × valor_unitário` (calculado automaticamente)
- `RN-10` Total da OS = soma dos subtotais de todos os itens
- `RN-11` Data de entrada é registrada automaticamente na abertura da OS
- `RN-12` Data de saída é registrada ao fechar a OS (`FINISHED`)
- `RN-13` A quilometragem do veículo pode ser atualizada a cada nova OS
- `RN-14` Uma OS com status `DELIVERED` não pode ser alterada (estado final)

### Ciclo de Vida da Ordem de Serviço

```
[OPEN] → [IN_PROGRESS] → [FINISHED] → [DELIVERED]
                ↕
        [WAITING_PARTS]
```

| De | Para | Condição |
|---|---|---|
| `OPEN` | `IN_PROGRESS` | Atendimento iniciado |
| `IN_PROGRESS` | `WAITING_PARTS` | Aguardando chegada de peças |
| `IN_PROGRESS` | `FINISHED` | Todos os serviços concluídos |
| `WAITING_PARTS` | `IN_PROGRESS` | Peças recebidas |
| `FINISHED` | `DELIVERED` | Veículo entregue ao cliente |

---

## Modelagem do Domínio

### Entidades

```
Client
├── id: Long
├── name: String
├── phone: String
├── email: String
└── vehicles: List<Vehicle>

Vehicle
├── id: Long
├── plate: String          ← chave de busca principal
├── brand: String
├── model: String
├── year: Integer
├── mileage: Integer
└── serviceOrders: List<ServiceOrder>

ServiceOrder
├── id: Long
├── entryDate: LocalDateTime
├── exitDate: LocalDateTime
├── problemDescription: String
├── observations: String
├── status: OrderStatus
├── totalValue: BigDecimal
├── vehicle: Vehicle
└── items: List<ServiceItem>

ServiceItem
├── id: Long
├── description: String
├── quantity: Integer
├── unitValue: BigDecimal
└── subtotal: quantity × unitValue   ← calculado

OrderStatus (enum)
├── OPEN
├── IN_PROGRESS
├── WAITING_PARTS
├── FINISHED
└── DELIVERED
```

### Relacionamentos
```
Client 1 ──── 0..* Vehicle
Vehicle 1 ──── 0..* ServiceOrder
ServiceOrder 1 ──── 1..* ServiceItem
ServiceOrder ──── OrderStatus
```

---

## Arquitetura

```
Controller  →  recebe requisições HTTP, valida DTOs
    ↓
Service     →  regras de negócio e orquestração
    ↓
Repository  →  acesso e persistência de dados (JPA)
    ↓
Database    →  PostgreSQL
```

### Estrutura de pacotes planejada
```
br.com.tonyauto
├── controller
├── service
├── repository
├── domain
│   ├── model        ← entidades JPA
│   └── enums        ← OrderStatus
├── dto
│   ├── request
│   └── response
├── exception
└── config
```

---

## Tecnologias

### Backend
- Java
- Spring Boot
- JPA / Hibernate

### Banco de Dados
- PostgreSQL

### Frontend
- MVP: HTML, CSS, JavaScript
- Evolução: React

### Ferramentas
- Git / GitHub
- Docker
- Postman

---

## Roadmap de Desenvolvimento

### ✅ Etapa 1 — Modelagem e Domínio
- [x] Levantamento de requisitos
- [x] Regras de negócio
- [x] Diagramas UML (Classes, Pacotes, Casos de Uso, Sequência, Estados, Atividades)
- [x] Modelagem do domínio

### 🔄 Etapa 2 — Implementação em Java Puro
- [ ] Entidades com atributos e métodos
- [ ] Enums (`OrderStatus`)
- [ ] Cálculo de subtotal e total da OS
- [ ] Histórico de serviços em memória
- [ ] Validações e exceções de domínio

### ⏳ Etapa 3 — Persistência de Dados
- [ ] Configuração do PostgreSQL
- [ ] Mapeamento JPA (`@Entity`, `@OneToMany`, `@ManyToOne`)
- [ ] Relacionamentos entre entidades
- [ ] Persistência e consultas básicas

### ⏳ Etapa 4 — API REST
- [ ] Controllers com endpoints REST
- [ ] DTOs de request e response
- [ ] Validações com Bean Validation
- [ ] Tratamento global de exceções
- [ ] Documentação com SpringDoc / OpenAPI

### ⏳ Etapa 5 — Frontend
- [ ] Tela de busca por placa
- [ ] Cadastro de clientes e veículos
- [ ] Abertura e gestão de ordens de serviço
- [ ] Histórico do veículo

### ⏳ Etapa 6 — Deploy
- [ ] Backend hospedado
- [ ] Banco de dados online
- [ ] Frontend hospedado
- [ ] Integração completa

---

## Evoluções Futuras

**V2**
- Upload de fotos dos veículos
- Geração de PDF
- Relatórios e exportação CSV

**V3**
- Autenticação e permissões
- Dashboard administrativo

---

## Status Atual

> 🔄 **Etapa 2 em andamento** — Implementação das entidades em Java puro.
>
> Entidades implementadas até o momento: `Vehicle`, `ServiceOrder`.
> Próximo passo: implementar `ServiceItem`, `Client` e conectar as entidades via `main` para validar o fluxo completo.