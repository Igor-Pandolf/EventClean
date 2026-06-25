# EventClean

API RESTful para gerenciamento e consulta de eventos baseada em **Clean Architecture** (Arquitetura Limpa), desenvolvida no curso Java10x.

## Sobre o Projeto

O **EventClean** permite cadastrar e consultar eventos de diferentes tipos (palestras, workshops, seminários, etc.), garantindo validação de integridade e regras de negócio complexas. O projeto foi estruturado seguindo os princípios de Clean Architecture, garantindo o desacoplamento das regras de negócio (core) em relação aos detalhes de infraestrutura, como banco de dados e frameworks (infra).

## Funcionalidades

- Cadastro de novos eventos com geração automatizada de identificador único (Hash/UUID)
- Bloqueio de cadastros manuais de identificador externo (retornando erro `422 Unprocessable Entity`)
- Listagem completa de todos os eventos cadastrados
- Busca refinada de eventos filtrando pelo identificador exclusivo
- Documentação interativa e detalhada do sistema via Swagger UI / OpenAPI 3
- Persistência estruturada utilizando PostgreSQL
- Controle de versão e migrações de banco de dados com Flyway

## Tecnologias

- **Java 17**
- **Spring Boot 4.1.0**
- **Spring Data JPA**
- **PostgreSQL Database**
- **Flyway**
- **Springdoc OpenAPI (Swagger)**
- **Lombok**
- **Maven**

### Testes
- **JUnit Jupiter 6**
- **Mockito 5**
- **Spring Boot WebMvc Test** (`@WebMvcTest` + `MockMvc`)

## Como Executar

Clone o repositório e acesse a pasta raiz:

```bash
git clone https://github.com/Igor-Pandolf/EventClean.git
cd EventClean
```

Suba o container com o banco de dados PostgreSQL configurado no Docker Compose:

```bash
docker-compose up -d
```

Execute a aplicação através do Maven Wrapper:

```bash
.\mvnw.cmd spring-boot:run
```

O servidor estará disponível em `http://localhost:8080`

A documentação interativa Swagger UI estará disponível em `http://localhost:8080/swagger-ui/index.html`

## Testes

O projeto conta com **39 testes unitários** distribuídos em **7 classes**, cobrindo todas as camadas da aplicação com isolamento total via mocks (sem necessidade de banco de dados ou servidor em execução).

### Executar todos os testes

```bash
.\mvnw.cmd test
```

### Executar uma classe específica

```bash
.\mvnw.cmd test -Dtest="CriarEventoCaseImplTest"
```

### Cobertura por camada

| Classe de Teste | Camada | Nº de Testes | O que é testado |
|---|---|---|---|
| `CriarEventoCaseImplTest` | Use Case | 4 | Delegação ao gateway, propagação de exceções |
| `BuscarEventoCaseImplTest` | Use Case | 4 | Retorno de lista, lista vazia, propagação de exceções |
| `FiltrarIdentificadorCaseImplTest` | Use Case | 4 | Busca por identificador, `EventNotExistsException` |
| `EventoRepositoryGatewayTest` | Gateway | 12 | Criação, colisão de identificador, busca, exceções de negócio |
| `EventoDtoMapperTest` | Mapper | 4 | Mapeamento DTO ↔ Domínio, round-trip, valores nulos |
| `EventoEntityMapperTest` | Mapper | 4 | Mapeamento Entity ↔ Domínio, round-trip, valores nulos |
| `EventoControllerTest` | Controller | 7 | Status HTTP (200/404/422), JSON response, passagem de parâmetros |

### Estrutura de testes

```
src/test/java/java10x/EventClean/
├── core/
│   └── usecases/
│       ├── BuscarEventoCaseImplTest.java
│       ├── CriarEventoCaseImplTest.java
│       └── FiltrarIdentificadorCaseImplTest.java
└── infra/
    ├── gateway/
    │   └── EventoRepositoryGatewayTest.java
    ├── mapper/
    │   ├── EventoDtoMapperTest.java
    │   └── EventoEntityMapperTest.java
    └── presentation/
        └── EventoControllerTest.java
```

## Rotas da API

### Eventos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/criarevento` | Cadastra um novo evento no sistema |
| GET | `/api/v1/buscarevento` | Listagem de todos os eventos cadastrados |
| GET | `/api/v1/identificador/{identificador}` | Busca um evento específico pelo identificador gerado |

### Exemplo de Requisição

**Criar evento:**
```json
POST /api/v1/criarevento
{
  "nome": "Workshop de Clean Architecture",
  "descricao": "Aprenda a estruturar seu código Java seguindo boas práticas.",
  "dataInicio": "2026-06-20T09:00:00",
  "dataFim": "2026-06-20T17:00:00",
  "localEvento": "Auditório Principal ou Online",
  "organizador": "Java10x Team",
  "capacidade": 150,
  "tipoEvento": "WORKSHOP"
}
```

**Retorno de Sucesso (200 OK):**
```json
{
  "Mensagem:": "Evento cadastrado com sucesso no nosso banco de dados",
  "Dados do evento:": {
    "id": 1,
    "nome": "Workshop de Clean Architecture",
    "descricao": "Aprenda a estruturar seu código Java seguindo boas práticas.",
    "dataInicio": "2026-06-20T09:00:00",
    "dataFim": "2026-06-20T17:00:00",
    "identificador": "evt_4b2c1d9f",
    "localEvento": "Auditório Principal ou Online",
    "organizador": "Java10x Team",
    "capacidade": 150,
    "tipoEvento": "WORKSHOP"
  }
}
```

**Buscar por identificador:**
```
GET /api/v1/identificador/evt_4b2c1d9f
```

## Estrutura do Projeto

```
EventClean/
├── src/main/java/java10x/EventClean/
│   ├── EventCleanApplication.java
│   ├── core/
│   │   ├── entities/
│   │   │   └── Evento.java
│   │   ├── enums/
│   │   │   └── TipoEvento.java
│   │   ├── gateway/
│   │   │   └── EventoGateway.java
│   │   └── usecases/
│   │       ├── BuscarEventoCase.java
│   │       ├── BuscarEventoCaseImpl.java
│   │       ├── CriarEventoCase.java
│   │       ├── CriarEventoCaseImpl.java
│   │       ├── FiltrarIdentificadorCase.java
│   │       └── FiltrarIdentificadorCaseImpl.java
│   └── infra/
│       ├── config/
│       │   ├── EventoConfig.java
│       │   └── OpenApiConfiguration.java
│       ├── dtos/
│       │   └── EventoDto.java
│       ├── exception/
│       │   ├── ControllerExceptionHandler.java
│       │   ├── DuplicateEventException.java
│       │   ├── EventNotExistsException.java
│       │   └── ManualIdentifierNotAllowedException.java
│       ├── gateway/
│       │   └── EventoRepositoryGateway.java
│       ├── mapper/
│       │   ├── EventoDtoMapper.java
│       │   └── EventoEntityMapper.java
│       ├── persistence/
│       │   ├── EventoEntity.java
│       │   └── EventoRepository.java
│       └── presentation/
│           └── EventoController.java
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/
│       └── V1__criar_tabela_evento.sql
├── docker-compose.yml
└── pom.xml
```

## Banco de Dados

O projeto utiliza **PostgreSQL** rodando via container Docker. O schema é gerenciado e migrado automaticamente com o **Flyway**.

**Tabela Eventos:**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | Identificador interno único (Auto-incremento / PK) |
| nome | VARCHAR(255) | Nome do evento |
| descricao | TEXT | Detalhes/descrição do evento |
| data_inicio | TIMESTAMP | Data e hora de início |
| data_fim | TIMESTAMP | Data e hora de término |
| identificador | VARCHAR(255) | Identificador externo exclusivo gerado (Unique) |
| local_evento | VARCHAR(255) | Onde o evento ocorrerá |
| organizador | VARCHAR(255) | Responsável pela organização |
| capacidade | INTEGER | Limite máximo de participantes |
| tipo_evento | VARCHAR(50) | Categoria do evento (ex: WORKSHOP, PALESTRA) |

## Configurações de Conexão

O arquivo `application.yml` vem configurado por padrão com os seguintes parâmetros de conexão ao PostgreSQL rodando no Docker local:

| Variável / Parâmetro | Valor Padrão | Descrição |
|----------------------|--------------|-----------|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5431/EventClean` | URL de conexão com a base de dados |
| `spring.datasource.username` | `postgres` | Usuário do banco de dados |
| `spring.datasource.password` | `postgres` | Senha de acesso |
| `spring.datasource.driver-class-name` | `org.postgresql.Driver` | Driver de conexão JDBC |