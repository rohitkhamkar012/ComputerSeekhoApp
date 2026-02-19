$ErrorActionPreference = "Stop"
try {
    Write-Host "Authenticating..."
    $body = @{ username = "admin"; password = "admin@123" } | ConvertTo-Json
    $loginResponse = Invoke-WebRequest -Uri "http://localhost:8080/auth/signIn" -Method Post -ContentType "application/json" -Body $body
    $token = $loginResponse.Headers["Authorization"] -replace "Bearer ", ""
    
    Write-Host "Fetching Enquiries..."
    $headers = @{ "Authorization" = "Bearer $token" }
    $response = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Headers $headers -Method Get
    
    Write-Host "Count: $($response.Count)"
    if ($response.Count -gt 0) {
        Write-Host "First Item JSON:"
        $response[0] | ConvertTo-Json -Depth 5
    } else {
        Write-Host "No enquiries found."
    }
} catch {
    Write-Error "Failed: $_"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        Write-Host "Error Body: $($reader.ReadToEnd())"
    }
}
