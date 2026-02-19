$ErrorActionPreference = "Stop"
$Url = "http://localhost:8080/auth/signIn"
$Origin = "http://localhost:5173"

Write-Host "--- 1. Testing Login Response Headers ---"
$body = @{ username="admin"; password="admin@123" } | ConvertTo-Json

try {
    # Simulate Browser Request with Origin Header
    $res = Invoke-WebRequest -Uri $Url -Method Post -Body $body -ContentType "application/json" -Headers @{ "Origin" = $Origin }
    
    Write-Host "Status: $($res.StatusCode)"
    Write-Host "Headers:"
    $res.Headers.GetEnumerator() | Sort-Object Key | ForEach-Object {
        Write-Host "  $($_.Key): $($_.Value)"
    }

    if ($res.Headers["Authorization"]) {
        Write-Host "`nAuthorization Header IS PRESENT." -ForegroundColor Green
    } else {
        Write-Host "`nAuthorization Header IS MISSING!" -ForegroundColor Red
    }
} catch {
    Write-Host "Request Failed: $_"
}
