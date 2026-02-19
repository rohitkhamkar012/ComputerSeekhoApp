$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

# Login
$loginBody = @{ username = $AdminUser; password = $AdminPass } | ConvertTo-Json
$response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $loginBody -ContentType "application/json"
$headers = @{ "Authorization" = $response.Headers["Authorization"] }

# Minimal Create - Testing specific field failures
Write-Host "Sending Empty Body..."
try {
    Invoke-RestMethod -Uri "$BaseUrl/enquiries/create" -Method Post -Body "{}" -Headers $headers -ContentType "application/json"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        Write-Host "Response: $($reader.ReadToEnd())"
    }
}
