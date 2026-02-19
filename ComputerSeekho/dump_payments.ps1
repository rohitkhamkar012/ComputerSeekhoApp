$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

$loginBody = @{ username = $AdminUser; password = $AdminPass } | ConvertTo-Json
$response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $loginBody -ContentType "application/json"
$headers = @{ "Authorization" = $response.Headers["Authorization"] }

Write-Host "Fetching ALL Payments..."
$payments = Invoke-RestMethod -Uri "$BaseUrl/payment/getAll" -Method Get -Headers $headers
$payments | Format-Table -AutoSize
