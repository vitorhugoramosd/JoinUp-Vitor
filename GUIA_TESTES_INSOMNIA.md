# Guia de Testes com Insomnia - Sistema de Compra de Ingressos

## 📋 Índice
1. [Configuração Inicial](#configuração-inicial)
2. [Variáveis de Ambiente](#variáveis-de-ambiente)
3. [Testes de Visitantes (Sem Autenticação)](#1-visitantes-sem-autenticação)
4. [Testes de Usuários](#2-usuários-com-autenticação)
5. [Testes de Organizadores](#3-organizadores)
6. [Coleção Completa](#coleção-completa)

---

## 🚀 Configuração Inicial

### 1. Criar um Ambiente (Environment)

1. No Insomnia, clique em **Manage Environments** (canto superior esquerdo)
2. Clique em **Create Environment**
3. Nome: `Ticket System - Local`
4. Adicione as seguintes variáveis:

```json
{
  "base_url": "http://localhost:8080",
  "api_url": "http://localhost:8080/api",
  "user_token": "",
  "organizer_token": "",
  "user_id": "",
  "organizer_id": "",
  "event_id": "",
  "purchase_code": ""
}
```

### 2. Ativar o Ambiente

Selecione `Ticket System - Local` no dropdown de ambientes (canto superior esquerdo).

---

## 🔧 Variáveis de Ambiente

As variáveis serão preenchidas automaticamente conforme você faz as requisições. Você pode usar:

- `{{ base_url }}` - URL base: http://localhost:8080
- `{{ api_url }}` - URL da API: http://localhost:8080/api
- `{{ user_token }}` - Token JWT do usuário comum
- `{{ organizer_token }}` - Token JWT do organizador
- `{{ user_id }}` - ID do usuário comum
- `{{ organizer_id }}` - ID do organizador
- `{{ event_id }}` - ID do evento criado
- `{{ purchase_code }}` - Código da compra

---

## 📝 Como Criar Requisições

Para cada requisição:
1. Clique em **New Request** (ou `Ctrl+N`)
2. Escolha o método HTTP (GET, POST, etc.)
3. Cole a URL
4. Configure Headers e Body conforme necessário
5. Clique em **Send**

---

## 🧪 Testes Detalhados

### 1. Visitantes (Sem Autenticação)

#### 1.1 GET - Informações da API (Root)
```
Method: GET
URL: {{ base_url }}/
```

**Headers:** (nenhum necessário)

**Response esperado:**
```json
{
  "message": "Sistema de Compra de Ingressos - API",
  "version": "1.0.0",
  "status": "running",
  "endpoints": { ... }
}
```

---

#### 1.2 GET - Listar Todos os Eventos
```
Method: GET
URL: {{ api_url }}/events
```

**Headers:** (nenhum necessário)

**Response esperado:**
```json
[]
```

*(Retorna lista vazia se não houver eventos ainda)*

---

#### 1.3 GET - Ver Detalhes de um Evento
```
Method: GET
URL: {{ api_url }}/events/{{ event_id }}
```

**Headers:** (nenhum necessário)

**Note:** Use `1` no lugar de `{{ event_id }}` se ainda não criou eventos.

---

#### 1.4 GET - Pesquisar Eventos por Nome
```
Method: GET
URL: {{ api_url }}/events/search?q=rock
```

**Headers:** (nenhum necessário)

**Query Parameters:**
- `q`: termo de busca (ex: "rock", "música", "festival")

---

### 2. Usuários (Com Autenticação)

#### 2.1 POST - Cadastrar Usuário Comum

```
Method: POST
URL: {{ api_url }}/users/register
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao@test.com",
  "password": "senha123456"
}
```

**Response esperado:**
```json
{
  "id": 1,
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao@test.com",
  "role": "USER",
  "fullName": "João Silva"
}
```

**💡 Dica:** Copie o `id` e salve em `{{ user_id }}` no ambiente.

---

#### 2.2 POST - Cadastrar Organizador

```
Method: POST
URL: {{ api_url }}/users/register
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "firstName": "Maria",
  "lastName": "Santos",
  "email": "maria@test.com",
  "password": "senha123456",
  "role": "ORGANIZER"
}
```

**Response esperado:**
```json
{
  "id": 2,
  "firstName": "Maria",
  "lastName": "Santos",
  "email": "maria@test.com",
  "role": "ORGANIZER",
  "fullName": "Maria Santos"
}
```

**💡 Dica:** Copie o `id` e salve em `{{ organizer_id }}` no ambiente.

---

#### 2.3 POST - Login (Usuário Comum)

```
Method: POST
URL: {{ api_url }}/auth/login
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "email": "joao@test.com",
  "password": "senha123456"
}
```

**Response esperado:**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvQHRlc3QuY29tIiwiYXV0aCI6IlVTRVIiLCJpYXQiOjE3MDAwMDAwMDAsImV4cCI6MTcwMDA4NjQwMH0...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao@test.com",
    "role": "USER",
    "fullName": "João Silva"
  }
}
```

**⚠️ IMPORTANTE:**
1. Copie o valor de `accessToken` da resposta
2. Vá em **Manage Environments**
3. Cole o token em `user_token`
4. Salve o ambiente

**💡 Dica Alternativa (Insomnia Chaining):**
Você pode configurar o Insomnia para salvar automaticamente:
1. Na aba **Tests** da requisição de login, adicione:
```javascript
const response = await insomnia.response.json();
if (response.accessToken) {
  insomnia.environment.set('user_token', response.accessToken);
  insomnia.environment.set('user_id', response.user.id);
}
```

---

#### 2.4 POST - Login (Organizador)

```
Method: POST
URL: {{ api_url }}/auth/login
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "email": "maria@test.com",
  "password": "senha123456"
}
```

**Response esperado:** (similar ao anterior, mas com role ORGANIZER)

**⚠️ IMPORTANTE:** Salve o token em `{{ organizer_token }}` e o ID em `{{ organizer_id }}`.

**💡 Dica:** Use o mesmo script de Tests, mas salve em `organizer_token`:
```javascript
const response = await insomnia.response.json();
if (response.accessToken) {
  insomnia.environment.set('organizer_token', response.accessToken);
  insomnia.environment.set('organizer_id', response.user.id);
}
```

---

#### 2.5 POST - Solicitar Recuperação de Senha

```
Method: POST
URL: {{ api_url }}/auth/password/reset/request
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "email": "joao@test.com"
}
```

**Response esperado:** `202 Accepted` (sem body)

**Note:** O email será enviado se configurado. Verifique os logs do servidor para obter o token de reset.

---

#### 2.6 POST - Confirmar Recuperação de Senha

```
Method: POST
URL: {{ api_url }}/auth/password/reset/confirm
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "token": "TOKEN_DO_RESET_AQUI",
  "newPassword": "novaSenha123456"
}
```

**Response esperado:** `200 OK` (sem body)

---

### 3. Organizadores

#### 3.1 POST - Criar Evento

```
Method: POST
URL: {{ api_url }}/events/organizer
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{ organizer_token }}
```

**Body (JSON):**
```json
{
  "name": "Festival de Música 2024",
  "description": "O maior festival de música do ano",
  "eventDate": "2024-12-25T20:00:00",
  "location": "Parque Ibirapuera, São Paulo",
  "ticketPrice": 150.00,
  "totalTickets": 5000
}
```

**Response esperado:**
```json
{
  "id": 1,
  "name": "Festival de Música 2024",
  "description": "O maior festival de música do ano",
  "eventDate": "2024-12-25T20:00:00",
  "location": "Parque Ibirapuera, São Paulo",
  "ticketPrice": 150.00,
  "totalTickets": 5000,
  "availableTickets": 5000,
  "organizerId": 2,
  "createdAt": "2024-01-27T10:00:00",
  "updatedAt": "2024-01-27T10:00:00"
}
```

**⚠️ IMPORTANTE:** 
- O token do organizador é necessário
- Copie o `id` do evento e salve em `{{ event_id }}`

**💡 Dica:** Use script de Tests para salvar automaticamente:
```javascript
const response = await insomnia.response.json();
if (response.id) {
  insomnia.environment.set('event_id', response.id.toString());
}
```

---

#### 3.2 GET - Dashboard do Organizador

```
Method: GET
URL: {{ api_url }}/dashboard/organizer/{{ organizer_id }}
```

**Headers:**
```
Authorization: Bearer {{ organizer_token }}
```

**Response esperado:**
```json
[
  {
    "id": 1,
    "name": "Festival de Música 2024",
    "description": "O maior festival de música do ano",
    "eventDate": "2024-12-25T20:00:00",
    "location": "Parque Ibirapuera, São Paulo",
    "ticketPrice": 150.00,
    "totalTickets": 5000,
    "availableTickets": 4980,
    "ticketsSold": 20,
    "totalRevenue": 3000.00,
    "totalPurchases": 10,
    "ticketsRemaining": 4980,
    "occupancyRate": 0.40
  }
]
```

---

### 4. Compras de Ingressos

#### 4.1 POST - Comprar Ingressos

```
Method: POST
URL: {{ api_url }}/tickets/purchase
```

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {{ user_token }}
```

**Body (JSON):**
```json
{
  "eventId": {{ event_id }},
  "attendees": [
    {
      "fullName": "João Silva",
      "cpf": "12345678901",
      "email": "joao@test.com",
      "birthDate": "1990-05-15"
    },
    {
      "fullName": "Maria Silva",
      "cpf": "98765432109",
      "email": "maria.silva@test.com",
      "birthDate": "1992-08-20"
    }
  ]
}
```

**Response esperado:**
```json
{
  "id": 1,
  "userId": 1,
  "eventId": 1,
  "quantity": 2,
  "totalAmount": 300.00,
  "status": "CONFIRMED",
  "purchaseCode": "PUR-1737982800000-123",
  "createdAt": "2024-01-27T10:30:00",
  "updatedAt": "2024-01-27T10:30:00",
  "tickets": [
    {
      "id": 1,
      "eventId": 1,
      "purchaseId": 1,
      "attendeeName": "João Silva",
      "attendeeCpf": "12345678901",
      "attendeeEmail": "joao@test.com",
      "attendeeBirthDate": "1990-05-15",
      "price": 150.00,
      "status": "ACTIVE",
      "ticketCode": "TKT-1737982800000-5678",
      "createdAt": "2024-01-27T10:30:00",
      "updatedAt": "2024-01-27T10:30:00"
    },
    {
      "id": 2,
      "eventId": 1,
      "purchaseId": 1,
      "attendeeName": "Maria Silva",
      "attendeeCpf": "98765432109",
      "attendeeEmail": "maria.silva@test.com",
      "attendeeBirthDate": "1992-08-20",
      "price": 150.00,
      "status": "ACTIVE",
      "ticketCode": "TKT-1737982800000-9012",
      "createdAt": "2024-01-27T10:30:00",
      "updatedAt": "2024-01-27T10:30:00"
    }
  ]
}
```

**⚠️ IMPORTANTE:**
- O token do usuário é necessário
- O `eventId` deve ser válido
- Copie o `purchaseCode` e salve em `{{ purchase_code }}`

**💡 Dica:** Use script de Tests:
```javascript
const response = await insomnia.response.json();
if (response.purchaseCode) {
  insomnia.environment.set('purchase_code', response.purchaseCode);
}
```

---

#### 4.2 GET - Obter Compra por Código

```
Method: GET
URL: {{ api_url }}/tickets/purchase/{{ purchase_code }}
```

**Headers:**
```
Authorization: Bearer {{ user_token }}
```

**Note:** Substitua `{{ purchase_code }}` pelo código retornado na compra (ex: "PUR-1737982800000-123")

---

#### 4.3 GET - Listar Compras do Usuário

```
Method: GET
URL: {{ api_url }}/tickets/user/{{ user_id }}
```

**Headers:**
```
Authorization: Bearer {{ user_token }}
```

**Response esperado:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "eventId": 1,
    "quantity": 2,
    "totalAmount": 300.00,
    "status": "CONFIRMED",
    "purchaseCode": "PUR-1737982800000-123",
    ...
  }
]
```

---

## 📦 Coleção Completa para Importar no Insomnia

Você pode criar uma coleção organizada no Insomnia. Aqui está a estrutura recomendada:

### Estrutura de Pastas:

```
📁 Ticket System API
  📁 01 - Public (Visitantes)
    📄 GET - Root (API Info)
    📄 GET - List Events
    📄 GET - Get Event by ID
    📄 GET - Search Events
  📁 02 - Auth (Usuários)
    📄 POST - Register User
    📄 POST - Register Organizer
    📄 POST - Login (User)
    📄 POST - Login (Organizer)
    📄 POST - Request Password Reset
    📄 POST - Confirm Password Reset
  📁 03 - Organizers
    📄 POST - Create Event
    📄 GET - Dashboard
  📁 04 - Tickets
    📄 POST - Purchase Tickets
    📄 GET - Get Purchase by Code
    📄 GET - List User Purchases
```

---

## 🎯 Scripts Úteis para Tests (Insomnia)

### Script para salvar token automaticamente (Login):

**Na aba "Tests" da requisição de Login, adicione:**

```javascript
const response = await insomnia.response.json();

if (response.accessToken) {
  // Determina se é user ou organizer baseado no role
  if (response.user.role === 'ORGANIZER') {
    insomnia.environment.set('organizer_token', response.accessToken);
    insomnia.environment.set('organizer_id', response.user.id.toString());
    console.log('✅ Organizer token saved!');
  } else {
    insomnia.environment.set('user_token', response.accessToken);
    insomnia.environment.set('user_id', response.user.id.toString());
    console.log('✅ User token saved!');
  }
}
```

### Script para salvar ID do evento (Create Event):

```javascript
const response = await insomnia.response.json();

if (response.id) {
  insomnia.environment.set('event_id', response.id.toString());
  console.log('✅ Event ID saved:', response.id);
}
```

### Script para salvar código de compra (Purchase Tickets):

```javascript
const response = await insomnia.response.json();

if (response.purchaseCode) {
  insomnia.environment.set('purchase_code', response.purchaseCode);
  console.log('✅ Purchase code saved:', response.purchaseCode);
}
```

---

## 🚨 Resolução de Problemas

### Erro 401 Unauthorized
**Problema:** Token inválido ou expirado
**Solução:** Faça login novamente e atualize o token no ambiente

### Erro 403 Forbidden
**Problema:** Token não tem permissão (role incorreto)
**Solução:** Use o token correto (user_token ou organizer_token)

### Erro 404 Not Found
**Problema:** URL incorreta ou recurso não existe
**Solução:** Verifique a URL e se o ID existe

### Erro 400 Bad Request
**Problema:** Dados inválidos no body
**Solução:** Verifique o JSON do body e validações

### Variáveis não funcionam
**Problema:** Variável não está salva ou ambiente não está ativo
**Solução:** Verifique se o ambiente está selecionado e se as variáveis estão salvas

---

## ✅ Checklist de Testes

Use este checklist para garantir que testou todas as funcionalidades:

- [ ] ✅ GET Root (/) - Informações da API
- [ ] ✅ GET List Events - Listar eventos
- [ ] ✅ GET Event by ID - Ver detalhes
- [ ] ✅ GET Search Events - Pesquisar
- [ ] ✅ POST Register User - Cadastrar usuário
- [ ] ✅ POST Register Organizer - Cadastrar organizador
- [ ] ✅ POST Login (User) - Login usuário
- [ ] ✅ POST Login (Organizer) - Login organizador
- [ ] ✅ POST Request Password Reset - Solicitar reset
- [ ] ✅ POST Create Event - Criar evento (organizador)
- [ ] ✅ GET Dashboard - Ver dashboard (organizador)
- [ ] ✅ POST Purchase Tickets - Comprar ingressos (usuário)
- [ ] ✅ GET Purchase by Code - Ver compra por código
- [ ] ✅ GET List User Purchases - Listar compras do usuário

---

**Boa sorte com os testes! 🚀**

Se tiver dúvidas, consulte o `GUIA_EXECUCAO_E_TESTES.md` para mais detalhes sobre os endpoints.

