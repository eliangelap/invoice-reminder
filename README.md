# Invoice Reminder

Serviço Spring Boot que verifica faturas ativas em uma base MySQL e envia, por WhatsApp, lembretes de vencimento ou de atraso. A execução é automática e diária; não há endpoints de negócio neste repositório.

## Como funciona

Todos os dias às **08:00**, o `InvoiceCheckerService`:

1. Localiza faturas ativas (`status = 1`) vencidas nos prazos configurados.
2. Localiza faturas ativas que vencerão nos prazos configurados.
3. Ignora faturas cujo campo `cancelOverdueReminders` seja `1`.
4. Busca o cliente, os itens da fatura e o nome da empresa.
5. Monta uma mensagem em português, incluindo número, vencimento, valor, serviços e link de pagamento.
6. Envia a mensagem a `MESSAGES_WHATSAPP_URL` por `POST`, com autenticação Bearer.

Os prazos padrão são:

| Situação | Propriedade | Dias em relação à data atual |
| --- | --- | --- |
| A vencer | `billing.days-send-reminder-before` | 10 e 5 dias antes |
| Vencida | `billing.days-send-reminder-after` | no dia do vencimento e 2, 5 e 10 dias depois |

Os valores podem ser alterados nas propriedades do Spring usando a sintaxe de lista, por exemplo `{7, 3, 1}`.

> A rotina apenas consulta e envia as mensagens: ela não persiste no código um marcador de envio da fatura. Portanto, cada execução considera novamente as faturas que correspondem aos prazos configurados.

## Stack

- Java 21
- Spring Boot 3.4.3
- Spring Web, Data JPA, Security, Scheduling e Actuator
- MySQL Connector/J
- Maven Wrapper
- Springdoc OpenAPI / Swagger UI
- Docker e Docker Compose

## Modelo de dados consultado

O serviço lê as tabelas existentes da aplicação de faturamento:

| Tabela | Uso |
| --- | --- |
| `tblinvoices` | Faturas, vencimento, status, valor, hash e indicador de cancelamento de lembretes |
| `tblclients` | Empresa/cliente e número de telefone para envio |
| `tblitemable` | Itens de uma fatura (`relType = invoice`) usados na descrição dos serviços |
| `tbloptions` | Valor de `companyname`, exibido na assinatura da mensagem |

## Pré-requisitos

- JDK 21
- Uma instância MySQL acessível com as tabelas acima
- Um serviço HTTP de envio de WhatsApp compatível com o payload abaixo

```json
{
  "number": "telefone-do-cliente",
  "body": "texto do lembrete",
  "saveOnTicket": true,
  "linkPreview": true
}
```

## Configuração

Defina as variáveis de ambiente abaixo antes de iniciar o serviço. Não inclua segredos em arquivos versionados.

| Variável | Obrigatória | Descrição |
| --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | Sim | Perfil Spring ativo, por exemplo `dev` ou `prod` |
| `SPRING_DATASOURCE_URL` | Sim | URL JDBC do MySQL, por exemplo `jdbc:mysql://host:3306/billing` |
| `SPRING_DATASOURCE_USERNAME` | Sim | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | Sim | Senha do banco |
| `MESSAGES_WHATSAPP_URL` | Sim | URL que recebe o `POST` da mensagem |
| `MESSAGES_WHATSAPP_TOKEN` | Sim | Token enviado como `Authorization: Bearer <token>` |
| `INVOICE_BASE_URL` | Sim | URL do link da fatura, contendo `{invoiceId}` e `{invoiceHash}` |

Exemplo de `INVOICE_BASE_URL`:

```text
https://faturas.exemplo.com/invoice/{invoiceId}/{invoiceHash}
```

Ao usar Docker Compose, crie um arquivo `.env` local com essas variáveis. Ele é carregado pelo `docker-compose.yml` e não deve ser enviado ao repositório.

## Execução local

No PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE = 'dev'
$env:SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3306/billing'
$env:SPRING_DATASOURCE_USERNAME = 'usuario'
$env:SPRING_DATASOURCE_PASSWORD = 'senha'
$env:MESSAGES_WHATSAPP_URL = 'https://whatsapp.exemplo.com/messages'
$env:MESSAGES_WHATSAPP_TOKEN = 'token'
$env:INVOICE_BASE_URL = 'https://faturas.exemplo.com/invoice/{invoiceId}/{invoiceHash}'
.\mvnw.cmd spring-boot:run
```

Em macOS/Linux, use `./mvnw spring-boot:run` após exportar as mesmas variáveis.

## Testes e build

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```

O projeto possui atualmente um teste de carregamento do contexto Spring. Como ele inicializa a aplicação, execute os testes com as variáveis de configuração disponíveis para o perfil selecionado.

## Docker

Com o arquivo `.env` configurado:

```powershell
docker compose up --build -d
```

O contêiner usa o fuso `America/Sao_Paulo`, grava a saída em `./logs` e reinicia automaticamente, exceto quando parado manualmente.

Para acompanhar os logs:

```powershell
docker compose logs -f invoice-reminder
```

## Observabilidade e documentação da API

O servidor escuta a porta `8080` e utiliza o prefixo `/api`.

- Saúde/Actuator: `http://localhost:8080/api/actuator/health`
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- OpenAPI: `http://localhost:8080/api/v3/api-docs`

Esses caminhos são liberados pelo filtro de autenticação. Nos demais caminhos, a ausência do cabeçalho `Authorization` é bloqueada; o token JWT é apenas decodificado para extrair os campos, sem validação de assinatura. A expiração só é verificada quando o token possui `subject`, e a cadeia de segurança também permite todas as requisições. Assim, essa implementação não deve ser considerada um controle de acesso robusto.

## Estrutura do código

```text
src/main/java/br/dev/eliangela/invoice_reminder/
├── config/          # propriedades, segurança, filtro HTTP e OpenAPI
├── core/
│   ├── model/       # entidades JPA e repositórios
│   ├── service/     # agendamento e JWT
│   └── usecase/     # busca de faturas, texto e envio WhatsApp
├── util/            # formatação de moeda e datas
└── web/error/       # exceções e tratamento padronizado de erros
```

## Licença

Não há licença declarada no repositório.
