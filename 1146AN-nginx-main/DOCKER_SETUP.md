# 🐳 Configuração Docker - Sistema de Eventos

## Pré-requisitos

- Docker Desktop instalado e rodando
- Portas disponíveis: 5432, 5433, 5434 (PostgreSQL), 5050 (PgAdmin), 8080, 8084, 8085, 8761

## 📦 Bancos de Dados Configurados

| Serviço | Banco | Porta | Usuário | Senha |
|---------|-------|-------|---------|-------|
| Auth Service | auth_db | 5432 | auth_user | auth_pass |
| Event Service | events_db | 5433 | events_user | events_pass |
| Ticket Service | tickets_db | 5434 | tickets_user | tickets_pass |

## 🚀 Como Rodar

### 1. Iniciar apenas os bancos de dados

```bash
cd 1146AN-nginx-main
docker-compose up -d postgres-auth postgres-events postgres-tickets pgadmin
```

### 2. Iniciar todos os serviços

```bash
docker-compose up -d
```

### 3. Ver logs dos serviços

```bash
# Todos os serviços
docker-compose logs -f

# Apenas um serviço específico
docker-compose logs -f auth-service
docker-compose logs -f postgres-auth
```

### 4. Parar os serviços

```bash
docker-compose down
```

### 5. Parar e remover volumes (limpar dados)

```bash
docker-compose down -v
```

## 🔍 PgAdmin - Interface Gráfica

Acesse: http://localhost:5050

**Login:**
- Email: `admin@eventos.com`
- Senha: `admin123`

### Conectar aos bancos de dados no PgAdmin:

1. Clique em "Add New Server"
2. Na aba "General": Nome = "Auth Database"
3. Na aba "Connection":
   - Host: `postgres-auth`
   - Port: `5432`
   - Database: `auth_db`
   - Username: `auth_user`
   - Password: `auth_pass`

Repita para os outros bancos (postgres-events, postgres-tickets).

## 🔗 Conectar com DBeaver

### Auth Database:
- Host: `localhost`
- Port: `5432`
- Database: `auth_db`
- Username: `auth_user`
- Password: `auth_pass`

### Events Database:
- Host: `localhost`
- Port: `5433`
- Database: `events_db`
- Username: `events_user`
- Password: `events_pass`

### Tickets Database:
- Host: `localhost`
- Port: `5434`
- Database: `tickets_db`
- Username: `tickets_user`
- Password: `tickets_pass`

## 📡 Endpoints dos Serviços

| Serviço | Porta | URL |
|---------|-------|-----|
| Gateway | 8080 | http://localhost:8080 |
| Auth Service | 8084 | http://localhost:8084 |
| Ticket Service | 8085 | http://localhost:8085 |
| Service Discovery (Eureka) | 8761 | http://localhost:8761 |
| PgAdmin | 5050 | http://localhost:5050 |

## 🧪 Testar a Configuração

### 1. Verificar se os containers estão rodando:

```bash
docker ps
```

Você deve ver:
- postgres-auth
- postgres-events
- postgres-tickets
- pgadmin
- service-discovery
- auth-service
- gateway-service
- ticket-service

### 2. Verificar saúde dos bancos:

```bash
docker-compose ps
```

Os bancos devem estar com status "healthy".

### 3. Testar Auth Service:

```bash
# Registrar novo usuário
curl -X POST http://localhost:8084/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123",
    "role": "USER"
  }'

# Login
curl -X POST http://localhost:8084/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

## 🛠️ Troubleshooting

### Erro "port already in use"

```bash
# Verificar qual processo está usando a porta
netstat -ano | findstr :5432

# Parar containers que possam estar rodando
docker-compose down
```

### Limpar tudo e começar do zero

```bash
docker-compose down -v
docker system prune -a
docker-compose up -d
```

### Ver logs de erro

```bash
docker-compose logs auth-service
docker-compose logs postgres-auth
```

## 📝 Perfis de Configuração

### Local (Desenvolvimento sem Docker)
- Profile: `local`
- Banco: H2 (em memória)
- Console H2: http://localhost:8080/h2-console

### Docker (Produção/Teste)
- Profile: `docker`
- Banco: PostgreSQL
- PgAdmin: http://localhost:5050

## 🔄 Reconstruir Imagens

Se você modificar o código dos serviços, reconstrua as imagens:

```bash
docker-compose up -d --build
```

Ou para um serviço específico:

```bash
docker-compose up -d --build auth-service
```
