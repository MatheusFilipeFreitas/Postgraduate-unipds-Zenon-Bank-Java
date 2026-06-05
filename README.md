# Zenon Bank — Fundamentos Java

Backend educacional da fintech fictícia **Zenon Bank**: ingestão de transações a partir de CSV (PaySim), análise de fraudes, relatórios, benchmark de estruturas de dados, internacionalização e persistência em PostgreSQL com ingestão concorrente.

## Requisitos

- **Java JDK 25**
- **Maven 3.9+**
- **Docker** e **Docker Compose** (PostgreSQL)
- **Git**
- IDE recomendada: IntelliJ IDEA

## Estrutura do projeto

```
src/main/java/br/com/zenon/
├── Main.java                 # Ponto de entrada
├── app/                      # Orquestração dos fluxos (ZenonApplication, ZenonMigration)
├── models/                   # Records de domínio (Transaction, Origin, …)
├── constants/                # Caminhos, limites, colunas CSV, JDBC
├── io/                       # Leitura/mapeamento CSV e SQL
├── service/                  # Casos de uso (ingest, fraud, report, benchmark)
├── presentation/             # Saída no console e i18n (ResourceBundle)
└── repository/               # Repositório, queries SQL, acesso JDBC (Database)

docker/
├── compose (raiz)            # docker-compose.yaml
└── postgres/init/            # Script de criação da tabela (primeira subida do volume)

data/                         # Arquivos CSV (não versionados — adicionar localmente)
```

## Funcionalidades por área

| Área | Descrição |
|------|-----------|
| **Records** | Modelagem `Transaction`, `Origin`, `FraudDemark`, validações |
| **Ingestão CSV** | `TransactionIngestor`, `AbstractReader`, tratamento de linhas inválidas |
| **Streams** | Dashboard de fraudes com `FraudAnalyzer` |
| **Benchmark** | Busca por nome de origem em `List` vs `Map` com medição de tempo |
| **NIO** | Relatório agregado em um passe com `Files.lines` |
| **i18n** | `messages_en.properties` / `messages_pt.properties` |
| **JDBC** | `Database`, `TransactionQuery`, `TransactionSQLMapper`, Docker PostgreSQL |
| **Concorrência** | `readLazy` com batches e virtual threads + `insertAll` em lote |

## Dados

Coloque o CSV principal em:

`./data/PS_20174392719_1491204439457_log.csv`

Opcional (linhas com erro, para testes de exceção):

`./data/paysim_with_bad_data.csv`

Caminhos definidos em `br.com.zenon.constants.FilePath`.

## PostgreSQL (Docker)

Subir o banco:

```bash
docker compose up -d
```

O script `docker/postgres/init/01-create-transactions.sql` roda **apenas na primeira criação do volume**. Se a tabela não existir e o volume já existir:

```bash
./docker/postgres/apply-schema.sh
```

Recriar do zero (apaga dados):

```bash
docker compose down -v
docker compose up -d
```

| Configuração | Valor |
|--------------|--------|
| Host / porta | `localhost:5432` |
| Database | `zenon_bank` |
| Usuário | `zenon` |
| Senha | `zenon123#` |

Credenciais alinhadas com `DatabaseConstant` e `docker-compose.yaml`.

## Build e execução

```bash
mvn compile
mvn exec:java
```

O `Main` executa:

1. **`ZenonApplication`** — relatório NIO, ingestão (memória), dashboard de fraudes, benchmark e fluxos em `en` / `pt-BR`
2. **`ZenonMigration`** — ingestão lazy do CSV completo com batches concorrentes e gravação no PostgreSQL

> A migração em um CSV com milhões de linhas pode levar bastante tempo. Certifique-se de que o Postgres está no ar antes de rodar.

## Branches (tarefas)

| Branch | Tema |
|--------|------|
| `task/01-setup` … `task/07-nio` | Setup, records, ingest, erros, streams, benchmark, NIO |
| `task/08-i18n` | Internacionalização |
| `task/09-jdbc` | JDBC + Docker + schema |
| `task/10-concurrency` | Ingestão lazy concorrente + migração em lote |

## Referência

Projeto prático da disciplina **Fundamentos Java** (pós-graduação). Siga o quadro de atividades (Trello) e os vídeos do projeto prático da disciplina.
