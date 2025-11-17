# Roteiro de Testes - Apresentação ao Professor
## Sistema de Compra de Ingressos

**Data:** [Preencher com a data da apresentação]  
**Aluno:** [Seu Nome]  
**Professor:** [Nome do Professor]  
**Disciplina:** [Nome da Disciplina]

---

## 📋 Objetivo

Este roteiro demonstra o funcionamento completo do sistema de compra de ingressos através de testes manuais dos endpoints da API REST, validando todas as funcionalidades implementadas.

---

## 🔧 Pré-requisitos

### 1. Verificar se o Sistema Está Rodando

**O QUE FAZER:** Antes de começar, certifique-se de que o sistema está em execução.

**COMO FAZER:**
```powershell
# Abra o PowerShell no diretório do projeto e execute:
./mvnw.cmd spring-boot:run
```

**O QUE MOSTRAR AO PROFESSOR:**
- Mostrar o terminal com a mensagem: `Started TicketSystemApplication`
- Explicar: "O sistema está rodando na porta 8080"

### 2. Verificar a Conectividade

**O QUE FAZER:** Testar se a API está respondendo.

**COMO FAZER:**
```powershell
Invoke-RestMethod -Uri http://localhost:8080/ -Method GET
```

**O QUE MOSTRAR AO PROFESSOR:**
- Mostrar a resposta no terminal
- Explicar: "Este é o endpoint raiz da API, confirmando que o servidor está funcionando"

**RESPOSTA ESPERADA:**
```json
{
  "message": "Sistema de Compra de Ingressos - API",
  "version": "1.0.0",
  "status": "running",
  "endpoints": {
    "public": "/api/events, /api/users/register, /api/auth/login",
    "protected": "/api/tickets/** (requires authentication)",
    "organizer": "/api/events/organizer, /api/dashboard/** (requires ORGANIZER role)"
  }
}
```

---

## 🎯 PARTE 1: Endpoints Públicos (Sem Autenticação)

### TESTE 1.1: Listar Todos os Eventos

**OBJETIVO:** Demonstrar que visitantes podem visualizar eventos sem autenticação.

**O QUE DIZER AO PROFESSOR:**
"Vou demonstrar que qualquer pessoa pode visualizar os eventos disponíveis, sem necessidade de login. Este é um endpoint público."

**COMO FAZER:**
```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/events -Method GET
```

**O QUE MOSTRAR:**
- Mostrar o resultado no terminal
- Explicar: "Inicialmente a lista está vazia ([]), pois ainda não criamos nenhum evento"

**RESPOSTA ESPERADA:**
```json
[]
```

**PONTO IMPORTANTE A MENCIONAR:**
- "Este endpoint não requer autenticação, seguindo o princípio de que eventos são públicos"

---

### TESTE 1.2: Ver Detalhes de um Evento (quando não existe)

**OBJETIVO:** Demonstrar tratamento de erro quando evento não existe.

**O QUE DIZER AO PROFESSOR:**
"Vou tentar acessar um evento que não existe para mostrar o tratamento de erro."

**COMO FAZER:**
```powershell
try {
    Invoke-RestMethod -Uri http://localhost:8080/api/events/1 -Method GET
} catch {
    Write-Host "Erro esperado: $_" -ForegroundColor Yellow
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
}
```

**O QUE MOSTRAR:**
- Mostrar o erro retornado
- Explicar: "O sistema retorna um erro 404 (Not Found) quando o evento não existe"

**PONTO IMPORTANTE A MENCIONAR:**
- "O sistema tem tratamento adequado de erros"

---

### TESTE 1.3: Pesquisar Eventos

**OBJETIVO:** Demonstrar funcionalidade de busca.

**O QUE DIZER AO PROFESSOR:**
"O sistema possui uma funcionalidade de busca que permite pesquisar eventos por nome."

**COMO FAZER:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/events/search?q=rock" -Method GET
```

**O QUE MOSTRAR:**
- Mostrar que retorna lista vazia (ainda não há eventos)
- Explicar: "A busca funciona, mas não retorna resultados porque ainda não criamos eventos"

**PONTO IMPORTANTE A MENCIONAR:**
- "A busca é case-insensitive e parcial (busca parcial no nome)"

---

## 👤 PARTE 2: Cadastro e Autenticação

### TESTE 2.1: Cadastrar Usuário Comum

**OBJETIVO:** Demonstrar cadastro de novo usuário.

**O QUE DIZER AO PROFESSOR:**
"Agora vou cadastrar um novo usuário. O sistema valida o email e criptografa a senha usando BCrypt."

**COMO FAZER:**
```powershell
$registerBody = @{
    firstName = "João"
    lastName = "Silva"
    email = "joao.teste@example.com"
    password = "senha123456"
} | ConvertTo-Json

$usuario = Invoke-RestMethod -Uri http://localhost:8080/api/users/register `
  -Method POST `
  -ContentType "application/json" `
  -Body $registerBody

Write-Host "✅ Usuário criado com sucesso!" -ForegroundColor Green
$usuario | ConvertTo-Json
```

**O QUE MOSTRAR:**
- Mostrar a resposta JSON no terminal
- Destacar o ID do usuário criado
- Explicar: "O sistema retorna os dados do usuário criado, SEM a senha (por segurança)"

**RESPOSTA ESPERADA:**
```json
{
  "id": 1,
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao.teste@example.com",
  "role": "USER",
  "fullName": "João Silva"
}
```

**PONTOS IMPORTANTES A MENCIONAR:**
- "A senha é criptografada antes de ser armazenada no banco de dados"
- "O email é validado para evitar duplicatas"
- "O role padrão é USER, mas pode ser especificado como ORGANIZER"

---

### TESTE 2.2: Tentar Cadastrar Usuário com Email Duplicado

**OBJETIVO:** Demonstrar validação de email duplicado.

**O QUE DIZER AO PROFESSOR:**
"Vou tentar cadastrar outro usuário com o mesmo email para mostrar a validação."

**COMO FAZER:**
```powershell
try {
    $registerBody2 = @{
        firstName = "Maria"
        lastName = "Santos"
        email = "joao.teste@example.com"  # Mesmo email
        password = "outrasenha123"
    } | ConvertTo-Json

    Invoke-RestMethod -Uri http://localhost:8080/api/users/register `
      -Method POST `
      -ContentType "application/json" `
      -Body $registerBody2
} catch {
    Write-Host "❌ Erro esperado: Email já cadastrado" -ForegroundColor Yellow
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
}
```

**O QUE MOSTRAR:**
- Mostrar o erro HTTP 409 (Conflict)
- Explicar: "O sistema impede cadastro de emails duplicados"

**PONTO IMPORTANTE A MENCIONAR:**
- "O sistema retorna HTTP 409 CONFLICT com mensagem clara sobre o problema"

---

### TESTE 2.3: Cadastrar Organizador

**OBJETIVO:** Demonstrar cadastro de usuário com role de ORGANIZER.

**O QUE DIZER AO PROFESSOR:**
"Agora vou cadastrar um organizador, que terá permissões para criar eventos."

**COMO FAZER:**
```powershell
$organizadorBody = @{
    firstName = "Maria"
    lastName = "Santos"
    email = "maria.organizadora@example.com"
    password = "senha123456"
    role = "ORGANIZER"
} | ConvertTo-Json

$organizador = Invoke-RestMethod -Uri http://localhost:8080/api/users/register `
  -Method POST `
  -ContentType "application/json" `
  -Body $organizadorBody

Write-Host "✅ Organizador criado com sucesso!" -ForegroundColor Green
$organizador | ConvertTo-Json

# Guardar o ID do organizador
$global:organizadorId = $organizador.id
```

**O QUE MOSTRAR:**
- Mostrar que o role é "ORGANIZER"
- Destacar o ID do organizador (geralmente 2)
- Explicar: "Organizadores têm permissões especiais para criar e gerenciar eventos"

**RESPOSTA ESPERADA:**
```json
{
  "id": 2,
  "firstName": "Maria",
  "lastName": "Santos",
  "email": "maria.organizadora@example.com",
  "role": "ORGANIZER",
  "fullName": "Maria Santos"
}
```

---

### TESTE 2.4: Login como Usuário Comum

**OBJETIVO:** Demonstrar autenticação e obtenção de token JWT.

**O QUE DIZER AO PROFESSOR:**
"Agora vou fazer login com o usuário comum. O sistema usa JWT (JSON Web Token) para autenticação."

**COMO FAZER:**
```powershell
$loginBody = @{
    email = "joao.teste@example.com"
    password = "senha123456"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri http://localhost:8080/api/auth/login `
  -Method POST `
  -ContentType "application/json" `
  -Body $loginBody

# Salvar o token para uso posterior
$global:userToken = $loginResponse.accessToken

Write-Host "✅ Login realizado com sucesso!" -ForegroundColor Green
Write-Host "Token JWT obtido (primeiros 30 caracteres): $($global:userToken.Substring(0, 30))..." -ForegroundColor Cyan
$loginResponse | ConvertTo-Json
```

**O QUE MOSTRAR:**
- Mostrar a resposta completa com o token
- Explicar: "O token JWT é necessário para acessar endpoints protegidos"
- Mostrar os dados do usuário retornados

**RESPOSTA ESPERADA:**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvLnRlc3RlQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIiLCJ1c2VySWQiOjEsImlhdCI6MTc2MzQxODkyNSwiZXhwIjoxNzYzNTA1MzI1fQ...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao.teste@example.com",
    "role": "USER",
    "fullName": "João Silva"
  }
}
```

**PONTOS IMPORTANTES A MENCIONAR:**
- "O token expira em 24 horas (86400 segundos)"
- "O token contém informações do usuário (email, role, userId)"
- "Tokens são assinados digitalmente para garantir integridade"

---

### TESTE 2.5: Login como Organizador

**OBJETIVO:** Demonstrar login de organizador e obter token com role ORGANIZER.

**O QUE DIZER AO PROFESSOR:**
"Agora vou fazer login como organizador para obter um token com permissões de ORGANIZER."

**COMO FAZER:**
```powershell
$loginOrganizador = @{
    email = "maria.organizadora@example.com"
    password = "senha123456"
} | ConvertTo-Json

$organizadorLogin = Invoke-RestMethod -Uri http://localhost:8080/api/auth/login `
  -Method POST `
  -ContentType "application/json" `
  -Body $loginOrganizador

# Salvar o token do organizador
$global:organizadorToken = $organizadorLogin.accessToken

Write-Host "✅ Login do organizador realizado!" -ForegroundColor Green
Write-Host "Token JWT do organizador obtido!" -ForegroundColor Cyan
$organizadorLogin.user | ConvertTo-Json
```

**O QUE MOSTRAR:**
- Mostrar que o role é "ORGANIZER"
- Explicar: "Este token terá permissões para criar eventos"

**PONTO IMPORTANTE A MENCIONAR:**
- "Cada role tem permissões diferentes no sistema"

---

### TESTE 2.6: Tentar Login com Senha Incorreta

**OBJETIVO:** Demonstrar validação de credenciais.

**O QUE DIZER AO PROFESSOR:**
"Vou tentar fazer login com a senha errada para mostrar a validação de credenciais."

**COMO FAZER:**
```powershell
try {
    $loginErrado = @{
        email = "joao.teste@example.com"
        password = "senhaERRADA"
    } | ConvertTo-Json

    Invoke-RestMethod -Uri http://localhost:8080/api/auth/login `
      -Method POST `
      -ContentType "application/json" `
      -Body $loginErrado
} catch {
    Write-Host "❌ Erro esperado: Credenciais inválidas" -ForegroundColor Yellow
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
}
```

**O QUE MOSTRAR:**
- Mostrar o erro HTTP 401 (Unauthorized)
- Explicar: "O sistema valida as credenciais antes de emitir o token"

**PONTO IMPORTANTE A MENCIONAR:**
- "A senha é verificada usando BCrypt, comparando o hash armazenado"

---

## 🎪 PARTE 3: Funcionalidades de Organizador

### TESTE 3.1: Criar Evento (como Organizador)

**OBJETIVO:** Demonstrar criação de evento por organizador autenticado.

**O QUE DIZER AO PROFESSOR:**
"Agora vou criar um evento usando o token do organizador. Apenas usuários com role ORGANIZER podem criar eventos."

**COMO FAZER:**
```powershell
$headers = @{
    Authorization = "Bearer $global:organizadorToken"
    "Content-Type" = "application/json"
}

$eventoBody = @{
    name = "Festival de Música 2024"
    description = "O maior festival de música do ano com grandes atrações"
    eventDate = "2024-12-25T20:00:00"
    location = "Parque Ibirapuera, São Paulo - SP"
    ticketPrice = 150.00
    totalTickets = 5000
} | ConvertTo-Json

$evento = Invoke-RestMethod -Uri http://localhost:8080/api/events/organizer `
  -Method POST `
  -Headers $headers `
  -Body $eventoBody

Write-Host "✅ Evento criado com sucesso!" -ForegroundColor Green
$evento | ConvertTo-Json

# Guardar o ID do evento
$global:eventoId = $evento.id
```

**O QUE MOSTRAR:**
- Mostrar a resposta completa com todos os dados do evento
- Destacar que o `organizerId` é o ID do organizador logado
- Explicar: "O sistema automaticamente associa o evento ao organizador que criou"

**RESPOSTA ESPERADA:**
```json
{
  "id": 1,
  "name": "Festival de Música 2024",
  "description": "O maior festival de música do ano com grandes atrações",
  "eventDate": "2024-12-25T20:00:00",
  "location": "Parque Ibirapuera, São Paulo - SP",
  "ticketPrice": 150.00,
  "totalTickets": 5000,
  "availableTickets": 5000,
  "organizerId": 2,
  "createdAt": "2024-01-27T10:00:00",
  "updatedAt": "2024-01-27T10:00:00"
}
```

**PONTOS IMPORTANTES A MENCIONAR:**
- "O evento foi criado e está disponível para venda"
- "O `availableTickets` inicialmente é igual ao `totalTickets`"
- "Timestamps são gerados automaticamente"

---

### TESTE 3.2: Tentar Criar Evento Sem Autenticação

**OBJETIVO:** Demonstrar que endpoints protegidos requerem autenticação.

**O QUE DIZER AO PROFESSOR:**
"Vou tentar criar um evento sem token para mostrar a proteção de endpoints."

**COMO FAZER:**
```powershell
try {
    $eventoBody2 = @{
        name = "Evento Teste"
        description = "Teste"
        eventDate = "2024-12-31T20:00:00"
        location = "Teste"
        ticketPrice = 100.00
        totalTickets = 100
    } | ConvertTo-Json

    Invoke-RestMethod -Uri http://localhost:8080/api/events/organizer `
      -Method POST `
      -ContentType "application/json" `
      -Body $eventoBody2
} catch {
    Write-Host "❌ Erro esperado: Não autenticado" -ForegroundColor Yellow
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
}
```

**O QUE MOSTRAR:**
- Mostrar o erro HTTP 401 (Unauthorized)
- Explicar: "O sistema protege endpoints sensíveis, exigindo autenticação"

**PONTO IMPORTANTE A MENCIONAR:**
- "Segurança é implementada através de Spring Security com JWT"

---

### TESTE 3.3: Tentar Criar Evento como Usuário Comum

**OBJETIVO:** Demonstrar controle de acesso baseado em roles.

**O QUE DIZER AO PROFESSOR:**
"Vou tentar criar um evento usando o token de um usuário comum (não organizador) para mostrar o controle de acesso."

**COMO FAZER:**
```powershell
try {
    $headersUser = @{
        Authorization = "Bearer $global:userToken"
        "Content-Type" = "application/json"
    }

    $eventoBody3 = @{
        name = "Evento Teste"
        description = "Teste"
        eventDate = "2024-12-31T20:00:00"
        location = "Teste"
        ticketPrice = 100.00
        totalTickets = 100
    } | ConvertTo-Json

    Invoke-RestMethod -Uri http://localhost:8080/api/events/organizer `
      -Method POST `
      -Headers $headersUser `
      -Body $eventoBody3
} catch {
    Write-Host "❌ Erro esperado: Acesso negado - usuário não é organizador" -ForegroundColor Yellow
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
}
```

**O QUE MOSTRAR:**
- Mostrar o erro HTTP 403 (Forbidden)
- Explicar: "Apenas usuários com role ORGANIZER ou ADMIN podem criar eventos"

**PONTO IMPORTANTE A MENCIONAR:**
- "O sistema implementa controle de acesso baseado em roles (RBAC)"

---

### TESTE 3.4: Verificar que o Evento Foi Criado (Endpoint Público)

**OBJETIVO:** Confirmar que o evento criado aparece na listagem pública.

**O QUE DIZER AO PROFESSOR:**
"Agora vou verificar se o evento que criamos aparece na listagem pública de eventos."

**COMO FAZER:**
```powershell
$eventos = Invoke-RestMethod -Uri http://localhost:8080/api/events -Method GET
Write-Host "✅ Total de eventos disponíveis: $($eventos.Count)" -ForegroundColor Green
$eventos | ConvertTo-Json -Depth 5
```

**O QUE MOSTRAR:**
- Mostrar que o evento criado aparece na lista
- Explicar: "Eventos criados por organizadores ficam imediatamente disponíveis publicamente"

---

### TESTE 3.5: Ver Detalhes do Evento Criado

**OBJETIVO:** Demonstrar endpoint de detalhes do evento.

**O QUE DIZER AO PROFESSOR:**
"Vou buscar os detalhes completos do evento que criamos."

**COMO FAZER:**
```powershell
$eventoDetalhes = Invoke-RestMethod -Uri "http://localhost:8080/api/events/$global:eventoId" -Method GET
Write-Host "✅ Detalhes do evento ID $global:eventoId" -ForegroundColor Green
$eventoDetalhes | ConvertTo-Json -Depth 5
```

**O QUE MOSTRAR:**
- Mostrar todos os detalhes do evento
- Destacar as informações importantes (data, local, preço, ingressos disponíveis)

---

### TESTE 3.6: Pesquisar Evento

**OBJETIVO:** Demonstrar funcionalidade de busca funcionando com eventos reais.

**O QUE DIZER AO PROFESSOR:**
"Agora vou demonstrar a funcionalidade de busca, pesquisando pelo nome do evento."

**COMO FAZER:**
```powershell
Write-Host "`n🔍 Buscando por 'Festival'..." -ForegroundColor Cyan
$busca1 = Invoke-RestMethod -Uri "http://localhost:8080/api/events/search?q=Festival" -Method GET
$busca1 | ConvertTo-Json -Depth 5

Write-Host "`n🔍 Buscando por 'Música'..." -ForegroundColor Cyan
$busca2 = Invoke-RestMethod -Uri "http://localhost:8080/api/events/search?q=Música" -Method GET
$busca2 | ConvertTo-Json -Depth 5

Write-Host "`n🔍 Buscando por 'rock' (não deve encontrar nada)..." -ForegroundColor Cyan
$busca3 = Invoke-RestMethod -Uri "http://localhost:8080/api/events/search?q=rock" -Method GET
Write-Host "Resultados: $($busca3.Count)" -ForegroundColor Cyan
```

**O QUE MOSTRAR:**
- Mostrar que a busca encontra o evento quando usa termos corretos
- Mostrar que retorna lista vazia quando não encontra
- Explicar: "A busca é case-insensitive e busca parcial no nome"

---

### TESTE 3.7: Dashboard do Organizador

**OBJETIVO:** Demonstrar dashboard com estatísticas dos eventos do organizador.

**O QUE DIZER AO PROFESSOR:**
"Agora vou mostrar o dashboard do organizador, que exibe estatísticas de seus eventos, incluindo vendas e receita."

**COMO FAZER:**
```powershell
$headers = @{
    Authorization = "Bearer $global:organizadorToken"
}

$dashboard = Invoke-RestMethod -Uri "http://localhost:8080/api/dashboard/organizer/$global:organizadorId" `
  -Method GET `
  -Headers $headers

Write-Host "✅ Dashboard do Organizador (ID: $global:organizadorId)" -ForegroundColor Green
$dashboard | ConvertTo-Json -Depth 10
```

**O QUE MOSTRAR:**
- Mostrar as estatísticas do dashboard
- Explicar: "O dashboard mostra vendas, receita, ingressos vendidos, etc."
- Notar: "Inicialmente, não há vendas porque ainda não compramos ingressos"

**RESPOSTA ESPERADA (inicial):**
```json
[
  {
    "id": 1,
    "name": "Festival de Música 2024",
    "eventDate": "2024-12-25T20:00:00",
    "ticketPrice": 150.00,
    "totalTickets": 5000,
    "availableTickets": 5000,
    "ticketsSold": 0,
    "totalRevenue": 0.00,
    "totalPurchases": 0,
    "occupancyRate": 0.00
  }
]
```

**PONTO IMPORTANTE A MENCIONAR:**
- "O dashboard é exclusivo para organizadores e mostra dados agregados"

---

## 🎫 PARTE 4: Compra de Ingressos

### TESTE 4.1: Criar Mais um Evento (para ter opções)

**OBJETIVO:** Criar um segundo evento para ter mais opções na demonstração.

**O QUE DIZER AO PROFESSOR:**
"Vou criar um segundo evento para ter mais dados na demonstração."

**COMO FAZER:**
```powershell
$headers = @{
    Authorization = "Bearer $global:organizadorToken"
    "Content-Type" = "application/json"
}

$evento2Body = @{
    name = "Show de Rock Nacional"
    description = "Grandes bandas de rock nacional se apresentam juntas"
    eventDate = "2024-11-15T19:00:00"
    location = "Allianz Parque, São Paulo - SP"
    ticketPrice = 200.00
    totalTickets = 3000
} | ConvertTo-Json

$evento2 = Invoke-RestMethod -Uri http://localhost:8080/api/events/organizer `
  -Method POST `
  -Headers $headers `
  -Body $evento2Body

Write-Host "✅ Segundo evento criado!" -ForegroundColor Green
$global:evento2Id = $evento2.id
```

---

### TESTE 4.2: Comprar Ingressos (como Usuário Autenticado)

**OBJETIVO:** Demonstrar processo de compra de ingressos.

**O QUE DIZER AO PROFESSOR:**
"Agora vou demonstrar a compra de ingressos. Um usuário autenticado pode comprar múltiplos ingressos para um evento, informando os dados dos participantes."

**COMO FAZER:**
```powershell
$headers = @{
    Authorization = "Bearer $global:userToken"
    "Content-Type" = "application/json"
}

$compraBody = @{
    eventId = $global:eventoId
    attendees = @(
        @{
            fullName = "João Silva"
            cpf = "12345678901"
            email = "joao.teste@example.com"
            birthDate = "1990-05-15"
        },
        @{
            fullName = "Maria Silva"
            cpf = "98765432109"
            email = "maria.silva@example.com"
            birthDate = "1992-08-20"
        }
    )
} | ConvertTo-Json -Depth 10

$compra = Invoke-RestMethod -Uri http://localhost:8080/api/tickets/purchase `
  -Method POST `
  -Headers $headers `
  -Body $compraBody

Write-Host "✅ Compra realizada com sucesso!" -ForegroundColor Green
Write-Host "Código da compra: $($compra.purchaseCode)" -ForegroundColor Cyan
Write-Host "Total pago: R$ $($compra.totalAmount)" -ForegroundColor Cyan
Write-Host "Quantidade de ingressos: $($compra.quantity)" -ForegroundColor Cyan

# Guardar código da compra
$global:codigoCompra = $compra.purchaseCode

$compra | ConvertTo-Json -Depth 10
```

**O QUE MOSTRAR:**
- Mostrar toda a resposta da compra
- Destacar:
  - O código da compra (purchaseCode)
  - O total pago
  - A quantidade de ingressos
  - Os códigos dos ingressos (ticketCode) gerados
  - O status CONFIRMED

**RESPOSTA ESPERADA:**
```json
{
  "id": 1,
  "userId": 1,
  "eventId": 1,
  "quantity": 2,
  "totalAmount": 300.00,
  "status": "CONFIRMED",
  "purchaseCode": "PUR-1234567890-123",
  "createdAt": "2024-01-27T10:30:00",
  "updatedAt": "2024-01-27T10:30:00",
  "tickets": [
    {
      "id": 1,
      "eventId": 1,
      "purchaseId": 1,
      "attendeeName": "João Silva",
      "attendeeCpf": "12345678901",
      "attendeeEmail": "joao.teste@example.com",
      "attendeeBirthDate": "1990-05-15",
      "price": 150.00,
      "status": "ACTIVE",
      "ticketCode": "TKT-1234567890-5678",
      "createdAt": "2024-01-27T10:30:00"
    },
    {
      "id": 2,
      "eventId": 1,
      "purchaseId": 1,
      "attendeeName": "Maria Silva",
      "attendeeCpf": "98765432109",
      "attendeeEmail": "maria.silva@example.com",
      "attendeeBirthDate": "1992-08-20",
      "price": 150.00,
      "status": "ACTIVE",
      "ticketCode": "TKT-1234567890-9012",
      "createdAt": "2024-01-27T10:30:00"
    }
  ]
}
```

**PONTOS IMPORTANTES A MENCIONAR:**
- "Cada ingresso recebe um código único (ticketCode)"
- "O sistema reduz automaticamente a quantidade de ingressos disponíveis"
- "Cada participante deve ter seus dados completos (nome, CPF, email, data de nascimento)"
- "O total é calculado automaticamente (quantidade × preço do ingresso)"

---

### TESTE 4.3: Verificar Redução de Ingressos Disponíveis

**OBJETIVO:** Confirmar que a compra reduziu os ingressos disponíveis.

**O QUE DIZER AO PROFESSOR:**
"Vou verificar se o evento teve seus ingressos disponíveis reduzidos após a compra."

**COMO FAZER:**
```powershell
$eventoAtualizado = Invoke-RestMethod -Uri "http://localhost:8080/api/events/$global:eventoId" -Method GET
Write-Host "Ingressos disponíveis antes: 5000" -ForegroundColor Yellow
Write-Host "Ingressos disponíveis agora: $($eventoAtualizado.availableTickets)" -ForegroundColor Green
Write-Host "Ingressos vendidos: $($eventoAtualizado.totalTickets - $eventoAtualizado.availableTickets)" -ForegroundColor Cyan
```

**O QUE MOSTRAR:**
- Mostrar que `availableTickets` diminuiu de 5000 para 4998
- Explicar: "O sistema atualiza automaticamente a disponibilidade após cada compra"

---

### TESTE 4.4: Buscar Compra por Código

**OBJETIVO:** Demonstrar consulta de compra por código único.

**O QUE DIZER AO PROFESSOR:**
"O sistema permite consultar uma compra usando o código único gerado. Isso é útil para verificação de ingressos."

**COMO FAZER:**
```powershell
$headers = @{
    Authorization = "Bearer $global:userToken"
}

$compraPorCodigo = Invoke-RestMethod -Uri "http://localhost:8080/api/tickets/purchase/$global:codigoCompra" `
  -Method GET `
  -Headers $headers

Write-Host "✅ Compra encontrada pelo código: $global:codigoCompra" -ForegroundColor Green
$compraPorCodigo | ConvertTo-Json -Depth 10
```

**O QUE MOSTRAR:**
- Mostrar todos os detalhes da compra
- Destacar: "Este endpoint permite verificar compras sem conhecer o ID interno"

**PONTO IMPORTANTE A MENCIONAR:**
- "O código é único e pode ser usado para verificação em portarias de eventos"

---

### TESTE 4.5: Listar Todas as Compras do Usuário

**OBJETIVO:** Demonstrar listagem de histórico de compras.

**O QUE DIZER AO PROFESSOR:**
"Vou mostrar como um usuário pode ver todas as suas compras."

**COMO FAZER:**
```powershell
$headers = @{
    Authorization = "Bearer $global:userToken"
}

# Obter o ID do usuário do token (ou usar 1 diretamente)
$comprasUsuario = Invoke-RestMethod -Uri "http://localhost:8080/api/tickets/user/1" `
  -Method GET `
  -Headers $headers

Write-Host "✅ Total de compras do usuário: $($comprasUsuario.Count)" -ForegroundColor Green
$comprasUsuario | ConvertTo-Json -Depth 10
```

**O QUE MOSTRAR:**
- Mostrar a lista de compras
- Explicar: "Cada usuário pode ver apenas suas próprias compras"

---

### TESTE 4.6: Fazer Segunda Compra

**OBJETIVO:** Criar mais dados para o dashboard.

**O QUE DIZER AO PROFESSOR:**
"Vou fazer uma segunda compra para termos mais dados no dashboard."

**COMO FAZER:**
```powershell
$headers = @{
    Authorization = "Bearer $global:userToken"
    "Content-Type" = "application/json"
}

$compra2Body = @{
    eventId = $global:evento2Id
    attendees = @(
        @{
            fullName = "João Silva"
            cpf = "12345678901"
            email = "joao.teste@example.com"
            birthDate = "1990-05-15"
        }
    )
} | ConvertTo-Json -Depth 10

$compra2 = Invoke-RestMethod -Uri http://localhost:8080/api/tickets/purchase `
  -Method POST `
  -Headers $headers `
  -Body $compra2Body

Write-Host "✅ Segunda compra realizada!" -ForegroundColor Green
Write-Host "Evento: Show de Rock Nacional" -ForegroundColor Cyan
Write-Host "Valor: R$ $($compra2.totalAmount)" -ForegroundColor Cyan
```

---

### TESTE 4.7: Ver Dashboard Atualizado

**OBJETIVO:** Demonstrar dashboard com dados reais de vendas.

**O QUE DIZER AO PROFESSOR:**
"Agora vou mostrar o dashboard novamente, agora com dados de vendas e receita."

**COMO FAZER:**
```powershell
$headers = @{
    Authorization = "Bearer $global:organizadorToken"
}

$dashboardAtualizado = Invoke-RestMethod -Uri "http://localhost:8080/api/dashboard/organizer/$global:organizadorId" `
  -Method GET `
  -Headers $headers

Write-Host "`n📊 DASHBOARD DO ORGANIZADOR" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green

foreach ($evento in $dashboardAtualizado) {
    Write-Host "`nEvento: $($evento.name)" -ForegroundColor Cyan
    Write-Host "  Total de ingressos: $($evento.totalTickets)" -ForegroundColor White
    Write-Host "  Vendidos: $($evento.ticketsSold)" -ForegroundColor Green
    Write-Host "  Disponíveis: $($evento.availableTickets)" -ForegroundColor Yellow
    Write-Host "  Receita total: R$ $($evento.totalRevenue)" -ForegroundColor Green
    Write-Host "  Taxa de ocupação: $([math]::Round($evento.occupancyRate, 2))%" -ForegroundColor Cyan
}

$dashboardAtualizado | ConvertTo-Json -Depth 10
```

**O QUE MOSTRAR:**
- Mostrar as estatísticas atualizadas
- Destacar:
  - Ingressos vendidos
  - Receita total
  - Taxa de ocupação
- Explicar: "O dashboard fornece uma visão completa do desempenho dos eventos"

---

### TESTE 4.8: Tentar Comprar Ingressos Sem Autenticação

**OBJETIVO:** Demonstrar que compra requer autenticação.

**O QUE DIZER AO PROFESSOR:**
"Vou tentar comprar ingressos sem estar autenticado para mostrar a proteção."

**COMO FAZER:**
```powershell
try {
    $compraBodySemAuth = @{
        eventId = $global:eventoId
        attendees = @(
            @{
                fullName = "Teste"
                cpf = "00000000000"
                email = "teste@example.com"
                birthDate = "2000-01-01"
            }
        )
    } | ConvertTo-Json -Depth 10

    Invoke-RestMethod -Uri http://localhost:8080/api/tickets/purchase `
      -Method POST `
      -ContentType "application/json" `
      -Body $compraBodySemAuth
} catch {
    Write-Host "❌ Erro esperado: Não autenticado" -ForegroundColor Yellow
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
}
```

**O QUE MOSTRAR:**
- Mostrar o erro HTTP 401
- Explicar: "Apenas usuários autenticados podem comprar ingressos"

---

## 🔍 PARTE 5: Validações e Casos de Erro

### TESTE 5.1: Tentar Comprar Mais Ingressos do que Disponível

**OBJETIVO:** Demonstrar validação de disponibilidade.

**O QUE DIZER AO PROFESSOR:**
"Vou tentar comprar mais ingressos do que há disponíveis para mostrar a validação de estoque."

**COMO FAZER:**
```powershell
# Primeiro, verificar quantos ingressos ainda estão disponíveis
$eventoCheck = Invoke-RestMethod -Uri "http://localhost:8080/api/events/$global:eventoId" -Method GET
$disponiveis = $eventoCheck.availableTickets

Write-Host "Ingressos disponíveis: $disponiveis" -ForegroundColor Yellow

# Tentar comprar mais do que disponível
try {
    $headers = @{
        Authorization = "Bearer $global:userToken"
        "Content-Type" = "application/json"
    }

    # Criar lista de participantes maior que o disponível
    $muitosParticipantes = @()
    for ($i = 1; $i -le ($disponiveis + 10); $i++) {
        $muitosParticipantes += @{
            fullName = "Participante $i"
            cpf = "0000000000$i"
            email = "participante$i@example.com"
            birthDate = "2000-01-01"
        }
    }

    $compraErroBody = @{
        eventId = $global:eventoId
        attendees = $muitosParticipantes
    } | ConvertTo-Json -Depth 10

    Invoke-RestMethod -Uri http://localhost:8080/api/tickets/purchase `
      -Method POST `
      -Headers $headers `
      -Body $compraErroBody
} catch {
    Write-Host "❌ Erro esperado: Ingressos insuficientes" -ForegroundColor Yellow
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
}
```

**O QUE MOSTRAR:**
- Mostrar o erro retornado
- Explicar: "O sistema valida a disponibilidade antes de processar a compra"

---

### TESTE 5.2: Validar Dados Obrigatórios no Cadastro

**OBJETIVO:** Demonstrar validação de campos obrigatórios.

**O QUE DIZER AO PROFESSOR:**
"Vou tentar cadastrar um usuário sem preencher todos os campos obrigatórios."

**COMO FAZER:**
```powershell
try {
    $cadastroIncompleto = @{
        firstName = "Teste"
        # lastName faltando
        email = "teste@example.com"
        password = "123"
    } | ConvertTo-Json

    Invoke-RestMethod -Uri http://localhost:8080/api/users/register `
      -Method POST `
      -ContentType "application/json" `
      -Body $cadastroIncompleto
} catch {
    Write-Host "❌ Erro esperado: Validação de campos" -ForegroundColor Yellow
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
}
```

**O QUE MOSTRAR:**
- Mostrar o erro HTTP 400 (Bad Request)
- Explicar: "O sistema valida todos os campos obrigatórios usando Bean Validation"

---

## 📊 RESUMO FINAL

### TESTE FINAL: Listar Tudo que Foi Criado

**O QUE DIZER AO PROFESSOR:**
"Vou fazer um resumo final mostrando tudo que foi criado e testado no sistema."

**COMO FAZER:**
```powershell
Write-Host "`n╔════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   RESUMO DA DEMONSTRAÇÃO                ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════╝" -ForegroundColor Cyan

Write-Host "`n📋 EVENTOS DISPONÍVEIS:" -ForegroundColor Green
$todosEventos = Invoke-RestMethod -Uri http://localhost:8080/api/events -Method GET
foreach ($evt in $todosEventos) {
    Write-Host "  • $($evt.name) - R$ $($evt.ticketPrice) - $($evt.availableTickets) disponíveis" -ForegroundColor White
}

Write-Host "`n👤 USUÁRIOS CRIADOS:" -ForegroundColor Green
Write-Host "  • João Silva (USER) - joao.teste@example.com" -ForegroundColor White
Write-Host "  • Maria Santos (ORGANIZER) - maria.organizadora@example.com" -ForegroundColor White

Write-Host "`n🎫 COMPRAS REALIZADAS:" -ForegroundColor Green
$headers = @{ Authorization = "Bearer $global:userToken" }
$compras = Invoke-RestMethod -Uri "http://localhost:8080/api/tickets/user/1" -Method GET -Headers $headers
foreach ($comp in $compras) {
    Write-Host "  • Código: $($comp.purchaseCode) - R$ $($comp.totalAmount) - $($comp.quantity) ingressos" -ForegroundColor White
}

Write-Host "`n✅ TODOS OS TESTES FORAM EXECUTADOS COM SUCESSO!" -ForegroundColor Green
```

---

## 📝 CHECKLIST DE FUNCIONALIDADES DEMONSTRADAS

Ao final da apresentação, você deve ter demonstrado:

### Endpoints Públicos ✅
- [ ] GET /api/events - Listar eventos
- [ ] GET /api/events/{id} - Detalhes do evento
- [ ] GET /api/events/search?q={termo} - Pesquisar eventos
- [ ] GET / - Informações da API

### Cadastro e Autenticação ✅
- [ ] POST /api/users/register - Cadastrar usuário
- [ ] POST /api/users/register - Cadastrar organizador
- [ ] POST /api/auth/login - Login
- [ ] Validação de email duplicado
- [ ] Validação de credenciais

### Funcionalidades de Organizador ✅
- [ ] POST /api/events/organizer - Criar evento
- [ ] GET /api/dashboard/organizer/{id} - Dashboard
- [ ] Proteção de endpoints (autenticação)
- [ ] Controle de acesso (roles)

### Compra de Ingressos ✅
- [ ] POST /api/tickets/purchase - Comprar ingressos
- [ ] GET /api/tickets/purchase/{code} - Buscar compra por código
- [ ] GET /api/tickets/user/{id} - Listar compras do usuário
- [ ] Atualização automática de disponibilidade

### Validações e Segurança ✅
- [ ] Proteção de endpoints autenticados
- [ ] Controle de acesso baseado em roles
- [ ] Validação de campos obrigatórios
- [ ] Validação de disponibilidade de ingressos

---

## 💡 DICAS PARA A APRESENTAÇÃO

1. **Preparação:**
   - Teste todos os comandos ANTES da apresentação
   - Tenha o sistema rodando e funcionando
   - Prepare os dados de teste (emails, nomes, etc.)

2. **Durante a Apresentação:**
   - Fale claramente sobre cada funcionalidade
   - Mostre as respostas no terminal
   - Explique os pontos importantes de cada teste
   - Demonstre erros intencionais para mostrar validações

3. **Pontos a Destacar:**
   - Arquitetura em camadas (Domain, Application, Infrastructure)
   - Segurança (JWT, BCrypt, Spring Security)
   - Validações e tratamento de erros
   - Códigos únicos para ingressos e compras
   - Dashboard com estatísticas em tempo real

4. **Se Algo Der Errado:**
   - Tenha os comandos copiados e prontos para colar
   - Se houver erro, explique que é esperado (nos testes de erro)
   - Se o sistema não responder, verifique se está rodando

---

## 📚 INFORMAÇÕES TÉCNICAS PARA REFERÊNCIA

### Tecnologias Utilizadas
- **Backend:** Spring Boot 3.x
- **Banco de Dados:** H2 (desenvolvimento) / PostgreSQL (produção)
- **Autenticação:** JWT (JSON Web Token)
- **Segurança:** Spring Security, BCrypt
- **Validação:** Bean Validation (Jakarta Validation)

### Arquitetura
- **Domain-Driven Design (DDD)**
- **Clean Architecture**
- **Separation of Concerns**
- **Dependency Injection**

### Padrões Implementados
- Repository Pattern
- Use Case Pattern
- DTO Pattern
- Factory Pattern

---

**Boa sorte com a apresentação! 🚀**

