# Guia de Execução e Testes - Sistema de Compra de Ingressos

## 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- ✅ **Java 17** ou superior
  ```bash
  java -version
  ```

- ✅ **Maven 3.6+** ou superior
  ```bash
  mvn -version
  ```

- ✅ **Git** (opcional, se precisar clonar o repositório)

---

## 🚀 Como Executar o Projeto

### Opção 1: Usando Maven Wrapper (Recomendado)

```bash
# Windows
./mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Opção 2: Usando Maven direto

```bash
mvn spring-boot:run
```

### Opção 3: Compilar e executar manualmente

```bash
# Compilar o projeto
mvn clean install

# Executar o JAR gerado
java -jar target/ticket-system-0.0.1-SNAPSHOT.jar
```

---

## ✅ Verificar se o projeto está rodando

Após executar, você deve ver mensagens como:

```
Started TicketSystemApplication in X.XXX seconds
```

O servidor estará disponível em: **http://localhost:8080**

---

## 🗄️ Acessar o H2 Console (Banco de Dados)

1. Abra o navegador e acesse: **http://localhost:8080/h2-console**

2. Preencha os campos:
   - **JDBC URL:** `jdbc:h2:mem:ticketdb`
   - **Username:** `sa`
   - **Password:** (deixe em branco)

3. Clique em **Connect**

---

## 🧪 Como Testar os Endpoints

Você pode testar usando:
- **cURL** (linha de comando) - **⚠️ No PowerShell use `curl.exe` ou `Invoke-RestMethod`**
- **Postman** (interface gráfica)
- **Thunder Client** (extensão VS Code)
- **Insomnia**

**⚠️ IMPORTANTE - PowerShell do Windows:**
No PowerShell, `curl` é um alias para `Invoke-WebRequest`. Use uma das opções:
- `curl.exe` (curl real do Windows)
- `Invoke-RestMethod` (recomendado - veja `GUIA_TESTES_POWERSHELL.md`)

---

## 📝 Exemplos de Testes por Funcionalidade

### 1. Visitantes (Sem Autenticação)

#### 1.1 Listar todos os eventos
```bash
curl -X GET http://localhost:8080/api/events
```

#### 1.2 Ver detalhes de um evento
```bash
# Substitua {id} pelo ID de um evento existente
curl -X GET http://localhost:8080/api/events/1
```

#### 1.3 Pesquisar eventos por nome
```bash
curl -X GET "http://localhost:8080/api/events/search?q=rock"
```

---

### 2. Usuários - Cadastro e Autenticação

#### 2.1 Cadastrar novo usuário
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao@example.com",
    "password": "senha123456",
    "role": "USER"
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao@example.com",
  "role": "USER",
  "fullName": "João Silva"
}
```

#### 2.2 Cadastrar organizador
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Maria",
    "lastName": "Santos",
    "email": "maria@example.com",
    "password": "senha123456",
    "role": "ORGANIZER"
  }'
```

#### 2.3 Fazer login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123456"
  }'
```

**Resposta esperada:**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao@example.com",
    "role": "USER",
    "fullName": "João Silva"
  }
}
```

**⚠️ IMPORTANTE:** Guarde o `accessToken` para usar nas requisições autenticadas!

#### 2.4 Solicitar recuperação de senha
```bash
curl -X POST http://localhost:8080/api/auth/password/reset/request \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com"
  }'
```

**Nota:** O email será enviado (se configurado) ou você pode verificar os logs para obter o token.

#### 2.5 Confirmar recuperação de senha
```bash
curl -X POST http://localhost:8080/api/auth/password/reset/confirm \
  -H "Content-Type: application/json" \
  -d '{
    "token": "TOKEN_AQUI",
    "newPassword": "novaSenha123456"
  }'
```

---

### 3. Organizadores - Criar Eventos

#### 3.1 Criar evento (necessita autenticação de ORGANIZER)
```bash
curl -X POST http://localhost:8080/api/events/organizer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "name": "Show de Rock 2024",
    "description": "O maior show de rock do ano",
    "eventDate": "2024-12-31T20:00:00",
    "location": "Arena Anhembi, São Paulo",
    "ticketPrice": 150.00,
    "totalTickets": 5000
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "name": "Show de Rock 2024",
  "description": "O maior show de rock do ano",
  "eventDate": "2024-12-31T20:00:00",
  "location": "Arena Anhembi, São Paulo",
  "ticketPrice": 150.00,
  "totalTickets": 5000,
  "availableTickets": 5000,
  "organizerId": 2,
  "createdAt": "2024-01-27T10:00:00",
  "updatedAt": "2024-01-27T10:00:00"
}
```

#### 3.2 Ver dashboard do organizador
```bash
# Substitua {organizerId} pelo ID do organizador
curl -X GET http://localhost:8080/api/dashboard/organizer/2 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta esperada:**
```json
[
  {
    "id": 1,
    "name": "Show de Rock 2024",
    "description": "O maior show de rock do ano",
    "eventDate": "2024-12-31T20:00:00",
    "location": "Arena Anhembi, São Paulo",
    "ticketPrice": 150.00,
    "totalTickets": 5000,
    "availableTickets": 4950,
    "ticketsSold": 50,
    "totalRevenue": 7500.00,
    "totalPurchases": 25,
    "ticketsRemaining": 4950,
    "occupancyRate": 1.00
  }
]
```

---

### 4. Usuários - Comprar Ingressos

#### 4.1 Comprar ingressos (necessita autenticação)
```bash
curl -X POST http://localhost:8080/api/tickets/purchase \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "eventId": 1,
    "attendees": [
      {
        "fullName": "João Silva",
        "cpf": "12345678901",
        "email": "joao@example.com",
        "birthDate": "1990-05-15"
      },
      {
        "fullName": "Maria Silva",
        "cpf": "98765432109",
        "email": "maria@example.com",
        "birthDate": "1992-08-20"
      }
    ]
  }'
```

**Resposta esperada:**
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
      "attendeeEmail": "joao@example.com",
      "attendeeBirthDate": "1990-05-15",
      "price": 150.00,
      "status": "ACTIVE",
      "ticketCode": "TKT-1234567890-5678",
      "createdAt": "2024-01-27T10:30:00",
      "updatedAt": "2024-01-27T10:30:00"
    },
    {
      "id": 2,
      "eventId": 1,
      "purchaseId": 1,
      "attendeeName": "Maria Silva",
      "attendeeCpf": "98765432109",
      "attendeeEmail": "maria@example.com",
      "attendeeBirthDate": "1992-08-20",
      "price": 150.00,
      "status": "ACTIVE",
      "ticketCode": "TKT-1234567890-9012",
      "createdAt": "2024-01-27T10:30:00",
      "updatedAt": "2024-01-27T10:30:00"
    }
  ]
}
```

#### 4.2 Obter compra por código
```bash
curl -X GET http://localhost:8080/api/tickets/purchase/PUR-1234567890-123 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

#### 4.3 Listar compras do usuário
```bash
# Substitua {userId} pelo ID do usuário
curl -X GET http://localhost:8080/api/tickets/user/1 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 🔧 Configuração de Email (Opcional)

Para usar a funcionalidade de recuperação de senha e confirmação de compras por email:

### Gmail (Exemplo)

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-app
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**⚠️ Nota:** Para Gmail, você precisa criar uma "Senha de App" em:
- Google Account → Security → 2-Step Verification → App passwords

### Alternativa: Variáveis de Ambiente

```bash
# Linux/Mac
export MAIL_USERNAME=seu-email@gmail.com
export MAIL_PASSWORD=sua-senha-app

# Windows (PowerShell)
$env:MAIL_USERNAME="seu-email@gmail.com"
$env:MAIL_PASSWORD="sua-senha-app"

# Windows (CMD)
set MAIL_USERNAME=seu-email@gmail.com
set MAIL_PASSWORD=sua-senha-app
```

**Se o email não estiver configurado:** O sistema ainda funcionará, mas os emails não serão enviados. Você verá logs de erro, mas o sistema continuará operacional.

---

## 📋 Fluxo Completo de Teste

### Passo a passo para testar todas as funcionalidades:

1. **Cadastrar usuário comum:**
   ```bash
   curl -X POST http://localhost:8080/api/users/register \
     -H "Content-Type: application/json" \
     -d '{"firstName":"João","lastName":"Silva","email":"joao@test.com","password":"senha123456"}'
   ```

2. **Cadastrar organizador:**
   ```bash
   curl -X POST http://localhost:8080/api/users/register \
     -H "Content-Type: application/json" \
     -d '{"firstName":"Maria","lastName":"Santos","email":"maria@test.com","password":"senha123456","role":"ORGANIZER"}'
   ```

3. **Fazer login como organizador:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"maria@test.com","password":"senha123456"}'
   ```
   
   **Guarde o `accessToken`!**

4. **Criar evento (com token do organizador):**
   ```bash
   curl -X POST http://localhost:8080/api/events/organizer \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer TOKEN_DO_ORGANIZADOR" \
     -d '{"name":"Festival de Música","description":"Grande festival","eventDate":"2024-12-25T20:00:00","location":"Parque Ibirapuera","ticketPrice":100.00,"totalTickets":1000}'
   ```
   
   **Guarde o `id` do evento criado!**

5. **Listar eventos (público):**
   ```bash
   curl -X GET http://localhost:8080/api/events
   ```

6. **Ver detalhes do evento:**
   ```bash
   curl -X GET http://localhost:8080/api/events/1
   ```

7. **Fazer login como usuário comum:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"joao@test.com","password":"senha123456"}'
   ```
   
   **Guarde o `accessToken`!**

8. **Comprar ingressos (com token do usuário):**
   ```bash
   curl -X POST http://localhost:8080/api/tickets/purchase \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer TOKEN_DO_USUARIO" \
     -d '{"eventId":1,"attendees":[{"fullName":"João Silva","cpf":"12345678901","email":"joao@test.com","birthDate":"1990-01-01"}]}'
   ```

9. **Ver dashboard do organizador:**
   ```bash
   curl -X GET http://localhost:8080/api/dashboard/organizer/2 \
     -H "Authorization: Bearer TOKEN_DO_ORGANIZADOR"
   ```

---

## 🐛 Resolução de Problemas

### Erro: "Port 8080 already in use"
**Solução:** Altere a porta em `application.properties`:
```properties
server.port=8081
```

### Erro: "Token inválido ou expirado"
**Solução:** Faça login novamente para obter um novo token.

### Erro: "Email já cadastrado"
**Solução:** Use outro email ou limpe o banco H2 (o banco é recriado a cada reinicialização em modo `create-drop`).

### Erro de conexão com banco de dados
**Solução:** Verifique se o H2 está configurado corretamente. O banco é em memória e é recriado a cada execução.

---

## 📚 Recursos Adicionais

- **H2 Console:** http://localhost:8080/h2-console
- **API Base URL:** http://localhost:8080/api
- **Documentação:** Veja o README.md para mais detalhes

---

## ✅ Checklist de Testes

Use este checklist para garantir que testou todas as funcionalidades:

- [ ] Listar eventos (público)
- [ ] Ver detalhes de evento (público)
- [ ] Pesquisar eventos (público)
- [ ] Cadastrar usuário
- [ ] Cadastrar organizador
- [ ] Login como usuário
- [ ] Login como organizador
- [ ] Solicitar recuperação de senha
- [ ] Criar evento (organizador)
- [ ] Ver dashboard (organizador)
- [ ] Comprar ingressos (usuário)
- [ ] Ver compra por código
- [ ] Listar compras do usuário

---

**Boa sorte com os testes! 🚀**

