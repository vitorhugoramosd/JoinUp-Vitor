# Guia de Testes com PowerShell - Sistema de Compra de Ingressos

## ⚠️ Problema no PowerShell

No PowerShell do Windows, `curl` é um **alias** para `Invoke-WebRequest`, que tem sintaxe diferente. Por isso você vê o erro:

```
Invoke-WebRequest : Não é possível localizar um parâmetro que coincida com o nome de parâmetro 'X'.
```

## ✅ Soluções

Você tem **3 opções** para usar curl no PowerShell:

---

## Opção 1: Usar `curl.exe` (Recomendado)

Use `curl.exe` explicitamente para chamar o curl real do Windows:

### GET - Listar Eventos
```powershell
curl.exe -X GET http://localhost:8080/api/events
```

### GET - Root
```powershell
curl.exe -X GET http://localhost:8080/
```

### POST - Cadastrar Usuário
```powershell
curl.exe -X POST http://localhost:8080/api/users/register `
  -H "Content-Type: application/json" `
  -d '{\"firstName\":\"João\",\"lastName\":\"Silva\",\"email\":\"joao@test.com\",\"password\":\"senha123456\"}'
```

### POST - Login
```powershell
curl.exe -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{\"email\":\"joao@test.com\",\"password\":\"senha123456\"}'
```

**💡 Capturar o Token (após o login acima):**
```powershell
# Salvar resposta do curl em variável e extrair o token
$loginResponse = curl.exe -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{\"email\":\"joao@test.com\",\"password\":\"senha123456\"}' | ConvertFrom-Json

$global:userToken = $loginResponse.accessToken
Write-Host "Token salvo: $global:userToken"
```

**⚠️ Nota:** Com `curl.exe`, é mais fácil capturar o token usando `Invoke-RestMethod` (veja Opção 2 abaixo). O `curl.exe` é melhor para comandos rápidos ou quando você conhece a sintaxe do curl.

---

## Opção 2: Usar `Invoke-RestMethod` (PowerShell Nativo)

`Invoke-RestMethod` é a melhor opção para APIs REST no PowerShell. Ele automaticamente converte JSON:

### GET - Listar Eventos
```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/events -Method GET
```

### GET - Root
```powershell
Invoke-RestMethod -Uri http://localhost:8080/ -Method GET
```

### POST - Cadastrar Usuário
```powershell
$body = @{
    firstName = "João"
    lastName = "Silva"
    email = "joao@test.com"
    password = "senha123456"
} | ConvertTo-Json

Invoke-RestMethod -Uri http://localhost:8080/api/users/register `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

### POST - Login
```powershell
$body = @{
    email = "joao@test.com"
    password = "senha123456"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri http://localhost:8080/api/auth/login `
  -Method POST `
  -ContentType "application/json" `
  -Body $body

# Salvar o token em variável global para usar em outras requisições
$global:userToken = $response.accessToken
Write-Host "Token salvo: $global:userToken"
```

**💡 Vantagem:** `Invoke-RestMethod` automaticamente converte JSON e é mais fácil de usar no PowerShell!

---

## Opção 3: Usar `Invoke-WebRequest` (PowerShell Nativo)

Funciona, mas você precisa extrair o JSON manualmente:

### GET - Listar Eventos
```powershell
$response = Invoke-WebRequest -Uri http://localhost:8080/api/events -Method GET
$response.Content | ConvertFrom-Json
```

### POST - Cadastrar Usuário
```powershell
$body = @{
    firstName = "João"
    lastName = "Silva"
    email = "joao@test.com"
    password = "senha123456"
} | ConvertTo-Json

Invoke-WebRequest -Uri http://localhost:8080/api/users/register `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

---

## 📝 Exemplos Completos para Todos os Endpoints

### 1. Visitantes (Sem Autenticação)

#### GET - Informações da API
```powershell
Invoke-RestMethod -Uri http://localhost:8080/ -Method GET
```

#### GET - Listar Eventos
```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/events -Method GET
```

#### GET - Ver Detalhes de Evento
```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/events/1 -Method GET
```

#### GET - Pesquisar Eventos
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/events/search?q=rock" -Method GET
```

---

### 2. Usuários - Cadastro e Autenticação

#### POST - Cadastrar Usuário
```powershell
$body = @{
    firstName = "João"
    lastName = "Silva"
    email = "joao@test.com"
    password = "senha123456"
} | ConvertTo-Json

Invoke-RestMethod -Uri http://localhost:8080/api/users/register `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

#### POST - Cadastrar Organizador
```powershell
$body = @{
    firstName = "Maria"
    lastName = "Santos"
    email = "maria@test.com"
    password = "senha123456"
    role = "ORGANIZER"
} | ConvertTo-Json

Invoke-RestMethod -Uri http://localhost:8080/api/users/register `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

#### POST - Login
```powershell
$body = @{
    email = "joao@test.com"
    password = "senha123456"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri http://localhost:8080/api/auth/login `
  -Method POST `
  -ContentType "application/json" `
  -Body $body

# Salvar token em variável
$global:userToken = $response.accessToken
Write-Host "Token salvo: $global:userToken"
```

---

### 3. Organizadores

#### POST - Criar Evento (Precisa de Token)
```powershell
$headers = @{
    Authorization = "Bearer $global:userToken"
    "Content-Type" = "application/json"
}

$body = @{
    name = "Festival de Música 2024"
    description = "O maior festival de música do ano"
    eventDate = "2024-12-25T20:00:00"
    location = "Parque Ibirapuera, São Paulo"
    ticketPrice = 150.00
    totalTickets = 5000
} | ConvertTo-Json

Invoke-RestMethod -Uri http://localhost:8080/api/events/organizer `
  -Method POST `
  -Headers $headers `
  -Body $body
```

#### GET - Dashboard do Organizador
```powershell
$headers = @{
    Authorization = "Bearer $global:userToken"
}

Invoke-RestMethod -Uri http://localhost:8080/api/dashboard/organizer/2 `
  -Method GET `
  -Headers $headers
```

---

### 4. Compras de Ingressos

#### POST - Comprar Ingressos (Precisa de Token)
```powershell
$headers = @{
    Authorization = "Bearer $global:userToken"
    "Content-Type" = "application/json"
}

$body = @{
    eventId = 1
    attendees = @(
        @{
            fullName = "João Silva"
            cpf = "12345678901"
            email = "joao@test.com"
            birthDate = "1990-05-15"
        },
        @{
            fullName = "Maria Silva"
            cpf = "98765432109"
            email = "maria@test.com"
            birthDate = "1992-08-20"
        }
    )
} | ConvertTo-Json -Depth 10

Invoke-RestMethod -Uri http://localhost:8080/api/tickets/purchase `
  -Method POST `
  -Headers $headers `
  -Body $body
```

#### GET - Obter Compra por Código
```powershell
$headers = @{
    Authorization = "Bearer $global:userToken"
}

Invoke-RestMethod -Uri "http://localhost:8080/api/tickets/purchase/PUR-1234567890-123" `
  -Method GET `
  -Headers $headers
```

#### GET - Listar Compras do Usuário
```powershell
$headers = @{
    Authorization = "Bearer $global:userToken"
}

Invoke-RestMethod -Uri http://localhost:8080/api/tickets/user/1 `
  -Method GET `
  -Headers $headers
```

---

## 🎯 Script Completo para Testar Tudo

Copie e cole este script no PowerShell para testar tudo em sequência:

```powershell
# ============================================
# Script de Teste Completo - Ticket System
# ============================================

Write-Host "=== TESTE 1: Cadastrar Usuário ===" -ForegroundColor Green
$registerBody = @{
    firstName = "João"
    lastName = "Silva"
    email = "joao@test.com"
    password = "senha123456"
} | ConvertTo-Json

$user = Invoke-RestMethod -Uri http://localhost:8080/api/users/register `
  -Method POST -ContentType "application/json" -Body $registerBody

Write-Host "Usuário criado: $($user.fullName) (ID: $($user.id))" -ForegroundColor Cyan

Write-Host "`n=== TESTE 2: Login ===" -ForegroundColor Green
$loginBody = @{
    email = "joao@test.com"
    password = "senha123456"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri http://localhost:8080/api/auth/login `
  -Method POST -ContentType "application/json" -Body $loginBody

$global:userToken = $loginResponse.accessToken
Write-Host "Token obtido: $($global:userToken.Substring(0, 20))..." -ForegroundColor Cyan

Write-Host "`n=== TESTE 3: Listar Eventos ===" -ForegroundColor Green
$events = Invoke-RestMethod -Uri http://localhost:8080/api/events -Method GET
Write-Host "Eventos encontrados: $($events.Count)" -ForegroundColor Cyan

Write-Host "`n=== TESTE 4: Criar Evento (se você é organizador) ===" -ForegroundColor Green
# Primeiro, cadastre um organizador e faça login como organizador
# Depois use o token do organizador aqui

Write-Host "`n✅ Testes concluídos!" -ForegroundColor Green
```

---

## 💡 Dicas Importantes

### 1. Salvar Token em Variável
```powershell
# Após fazer login
$global:userToken = $response.accessToken

# Usar em outras requisições
$headers = @{
    Authorization = "Bearer $global:userToken"
}
```

### 2. Ver Resposta Formatada
```powershell
$response | ConvertTo-Json -Depth 10 | Write-Host
```

### 3. Ver Código de Status
```powershell
$response = Invoke-WebRequest -Uri http://localhost:8080/api/events -Method GET
Write-Host "Status: $($response.StatusCode)"
```

### 4. Tratamento de Erros
```powershell
try {
    $response = Invoke-RestMethod -Uri http://localhost:8080/api/events -Method GET
    Write-Host "Sucesso!" -ForegroundColor Green
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
```

---

## 🚨 Resolução de Problemas

### Erro: "Não é possível localizar um parâmetro"
**Solução:** Use `curl.exe` ou `Invoke-RestMethod` em vez de `curl`

### Erro: "401 Unauthorized"
**Solução:** Verifique se o token está correto:
```powershell
Write-Host "Token atual: $global:userToken"
```

### Erro: "400 Bad Request"
**Solução:** Verifique o JSON do body:
```powershell
$body | ConvertTo-Json -Depth 10 | Write-Host
```

### Erro: "500 Internal Server Error" ao criar evento
**Solução:** Verifique se o token foi salvo após o login:
```powershell
# Verificar se o token existe
if ($null -eq $global:userToken) {
    Write-Host "Token não encontrado! Faça login novamente:" -ForegroundColor Red
    $body = @{
        email = "joao@test.com"
        password = "senha123456"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri http://localhost:8080/api/auth/login `
      -Method POST -ContentType "application/json" -Body $body
    $global:userToken = $response.accessToken
    Write-Host "Token salvo: $global:userToken" -ForegroundColor Green
} else {
    Write-Host "Token atual: $global:userToken" -ForegroundColor Green
}
```

---

## ✅ Resumo das Opções

| Comando | Vantagem | Quando Usar |
|---------|----------|-------------|
| `curl.exe` | Sintaxe igual ao curl Linux/Mac | Se você conhece curl |
| `Invoke-RestMethod` | Nativo PowerShell, auto-converte JSON | **Recomendado** |
| `Invoke-WebRequest` | Nativo PowerShell | Se precisar ver headers |

**Recomendação:** Use `Invoke-RestMethod` para APIs REST no PowerShell! 🚀

