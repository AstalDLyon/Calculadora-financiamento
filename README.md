# Calculadora de Financiamento Imobiliário

Aplicação web construída com Spring Boot + Thymeleaf que simula financiamentos imobiliários
pelos sistemas SAC e Price, calculando parcelas, juros totais e custo final do financiamento.

---

## Visão geral da arquitetura

```
Usuário (navegador)
       │
       ▼
FinanciamentoController    ← recebe requisições HTTP GET e POST
       │
       ▼
FinanciamentoService       ← executa os cálculos de SAC e Price
       │
       ▼
SimulationResult           ← carrega o resultado de volta ao Controller
       │
       ▼
index.html (Thymeleaf)     ← renderiza o resultado no navegador
```

A aplicação segue o padrão **MVC (Model-View-Controller)** com arquitetura em camadas.
Não há persistência em banco de dados — todos os cálculos são realizados em memória.

---

## Estrutura de pastas

```
src/main/java/astal/projects/calculadorafinanciamento/
├── controller/
│   └── FinanciamentoController.java
├── model/
│   ├── SimulationRequest.java
│   └── SimulationResult.java
├── service/
│   └── FinanciamentoService.java
└── FinanciamentoApplication.java

src/main/resources/
├── static/css/
│   └── style.css
├── templates/
│   └── index.html
└── application.properties
```

---

## Camadas e responsabilidades

### Controller — `FinanciamentoController`

Gerencia as requisições HTTP da aplicação.

| Método | Rota | Tipo | Descrição |
|--------|------|------|-----------|
| `index()` | `/` | GET | Exibe o formulário vazio |
| `calcular()` | `/` | POST | Recebe os dados do formulário, aciona o Service e retorna o resultado |

O método `calcular()` utiliza `@Valid` para validar os dados de entrada antes de
acionar o Service. Em caso de erro de validação, retorna o formulário com mensagens de erro.

---

### Service — `FinanciamentoService`

Contém toda a lógica de cálculo dos sistemas de amortização.

#### `calcular(SimulationRequest req)`
Método principal — recebe os dados do formulário e direciona para SAC ou Price.

#### `calcularSAC(financiado, n, im, seguro)`
Calcula o financiamento pelo sistema SAC (Sistema de Amortização Constante).

- Amortização constante: `financiado / n`
- Juros decrescentes a cada parcela
- Primeira parcela: `amortização + (saldo devedor × taxa mensal) + seguro`
- Itera mês a mês para calcular o total pago

**Fórmula:**
```
Parcela_k = (financiado / n) + (saldo_devedor_k × im) + seguro
```

#### `calcularPrice(financiado, n, im, seguro)`
Calcula o financiamento pelo sistema Price (parcelas fixas).

- Parcela fixa durante todo o contrato
- Juros altos no início, amortização crescente ao longo do tempo

**Fórmula:**
```
Parcela = financiado × im × (1 + im)^n / ((1 + im)^n - 1)
```

---

### Model

#### `SimulationRequest` — dados de entrada

| Campo | Tipo | Validação | Descrição |
|-------|------|-----------|-----------|
| `valorImovel` | `Double` | `@NotNull`, `@Min(50000)` | Valor total do imóvel |
| `entradaPorcentagem` | `Double` | `@Min(5)`, `@Max(50)` | Percentual de entrada |
| `taxaAnual` | `Double` | `@DecimalMin(4.0)`, `@DecimalMax(20.0)` | Taxa de juros ao ano |
| `prazoAnos` | `Integer` | `@Min(5)`, `@Max(35)` | Prazo em anos |
| `sistema` | `String` | `@NotNull` | `"SAC"` ou `"PRICE"` |

#### `SimulationResult` — dados de saída

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `valorFinanciado` | `double` | Valor do imóvel menos a entrada |
| `primeiraParcela` | `double` | Valor da primeira parcela |
| `ultimaParcela` | `double` | Valor da última parcela |
| `totalPago` | `double` | Soma de todas as parcelas + seguro |
| `totalJuros` | `double` | Total pago em juros |
| `percentualJuros` | `double` | Juros em % sobre o valor financiado |
| `sistema` | `String` | Sistema utilizado (SAC ou Price) |

---

## Métricas emitidas

A aplicação não possui integração com sistemas de métricas (Prometheus, Micrometer, etc.)
na versão atual. As métricas disponíveis são as padrão do Spring Boot Actuator,
caso seja adicionado futuramente.

| Métrica | Descrição |
|---------|-----------|
| Nenhuma customizada | Sem métricas de negócio implementadas |

> Para adicionar métricas, incluir `spring-boot-starter-actuator` no `pom.xml`
> e expor endpoints como `/actuator/health` e `/actuator/metrics`.

---

## Serviços que se conectam a esta aplicação

| Serviço | Tipo | Descrição |
|---------|------|-----------|
| Navegador do usuário | Cliente HTTP | Acessa via `http://localhost:8080` |

---

## Serviços aos quais esta aplicação se conecta

| Serviço | Tipo | Descrição |
|---------|------|-----------|
| Nenhum | — | A aplicação não consome APIs externas nem banco de dados |

> Toda a lógica é executada localmente em memória.

---

## Dependências principais

| Dependência | Versão | Finalidade |
|-------------|--------|------------|
| `spring-boot-starter-web` | 3.2.5 | Servidor web embarcado (Tomcat) |
| `spring-boot-starter-thymeleaf` | 3.2.5 | Motor de templates HTML |
| `spring-boot-starter-validation` | 3.2.5 | Validação dos dados do formulário |
| `lombok` | — | Geração de getters, setters e builders |

---

## Como executar

```bash
# Clonar o projeto
git clone <url-do-repositorio>

# Entrar na pasta
cd calculadora-financiamento

# Executar com Maven
./mvnw spring-boot:run
```

Acessar no navegador: `http://localhost:8080`

---

## Melhorias futuras sugeridas

- Adicionar persistência com Spring Data JPA + H2/PostgreSQL para salvar histórico de simulações
- Implementar comparação lado a lado entre SAC e Price na mesma tela
- Adicionar Spring Boot Actuator para métricas de saúde da aplicação
- Implementar testes unitários no `FinanciamentoService`
- Adicionar suporte a simulação com FGTS e subsídio MCMV
