# Script corrigido para criar evento com encoding UTF-8
# Solução para o erro: Invalid UTF-8 start byte 0xfa

# Configurar encoding UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$PSDefaultParameterValues['*:Encoding'] = 'utf8'

# Verificar se o token está definido
if (-not $global:organizadorToken) {
    Write-Host "❌ Erro: Token do organizador não encontrado!" -ForegroundColor Red
    Write-Host "Por favor, faça login primeiro como organizador." -ForegroundColor Yellow
    exit 1
}

# Criar o body do evento (evitar caracteres especiais problemáticos ou usar UTF-8 corretamente)
$eventoBody = @{
    name = "Festival de Musica 2025"
    description = "O maior festival de musica do ano com grandes atracoes"
    eventDate = "2025-12-25T20:00:00"
    location = "Parque Ibirapuera, Sao Paulo - SP"
    ticketPrice = 150.00
    totalTickets = 5000
}

# Converter para JSON com encoding UTF-8
$jsonBody = $eventoBody | ConvertTo-Json -Depth 10

# Garantir que o JSON está em UTF-8
$utf8 = New-Object System.Text.UTF8Encoding $false
$jsonBytes = $utf8.GetBytes($jsonBody)
$jsonBodyUtf8 = $utf8.GetString($jsonBytes)

# Headers com charset UTF-8
$headers = @{
    Authorization = "Bearer $global:organizadorToken"
    "Content-Type" = "application/json; charset=utf-8"
}

try {
    # Enviar requisição usando Invoke-WebRequest para melhor controle
    $response = Invoke-WebRequest `
        -Uri http://localhost:8080/api/events/organizer `
        -Method POST `
        -Headers $headers `
        -Body $jsonBytes `
        -ContentType "application/json; charset=utf-8" `
        -ErrorAction Stop
    
    Write-Host "✅ Evento criado com sucesso!" -ForegroundColor Green
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Cyan
    
    $evento = $response.Content | ConvertFrom-Json
    $evento | ConvertTo-Json -Depth 10
    
    # Guardar o ID do evento
    $global:eventoId = $evento.id
    Write-Host "`n✅ ID do evento salvo: $global:eventoId" -ForegroundColor Cyan
    
} catch {
    Write-Host "❌ Erro ao criar evento:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Yellow
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "`nResposta do servidor:" -ForegroundColor Yellow
        Write-Host $responseBody -ForegroundColor White
    }
}

