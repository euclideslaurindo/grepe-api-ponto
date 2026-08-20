# GREPE API - Relatório de Ponto Eletrônico

API REST desenvolvida em Java (Spring Boot) para processar batidas de ponto eletrônico e calcular saldo de horas trabalhadas de servidores, aplicando regras de negócio e corrigindo anomalias nos registros.

## Regras de Negócio e Cálculos

O núcleo do sistema (`CalculadoraHorasService`) foi construído para lidar com as inconsistências comuns no registro de ponto:

- **Deduplicação de batidas:** Previne erros de clique duplo do usuário ignorando batidas registradas num intervalo menor que 5 minutos.
- **Dedução de almoço presumido:** Caso o servidor registre apenas a entrada e a saída do expediente (2 batidas), o algoritmo detecta a ausência de pausa e deduz automaticamente o horário padrão de almoço (12:00 às 13:30).
- **Cálculo de sobreposição temporal:** Utilização da API `java.time.Duration` para calcular interseções de horários e deduzir pausas exatas.
- **Tolerância de horas extras:** Ignora saldos positivos pequenos (atrasos naturais na fila do ponto), convertendo para hora extra apenas saldos superiores a 10 minutos diários.

## Tecnologias e Arquitetura

- **Java 21**
- **Spring Boot 3** (Web, Data JPA)
- **Banco de Dados:** MySQL (Produção) / H2 (Para testes locais)
- **Testes Unitários:** JUnit 5
- **Documentação:** SpringDoc / Swagger OpenAPI

O código prioriza paradigmas funcionais (Streams API) e utiliza exclusivamente a biblioteca moderna `java.time`, garantindo precisão nos cálculos de tempo. As regras de cálculo de horas e parseamento de datas estão cobertas por testes automatizados (`CalculadoraHorasServiceTest.java`).

## Como rodar o projeto

O repositório já inclui o Maven Wrapper, dispensando a instalação prévia do Maven.

1. Certifique-se de que o MySQL está em execução local (porta `3306`) com um banco criado chamado `grepe_test`.
2. Clone o repositório e rode os comandos abaixo na raiz do projeto:

```bash
# No Windows
.\mvnw.cmd spring-boot:run

# No Linux ou Mac
./mvnw spring-boot:run