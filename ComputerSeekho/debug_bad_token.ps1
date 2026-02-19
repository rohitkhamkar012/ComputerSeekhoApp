$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
try {
    # Send Garbage Token
    $headers = @{ "Authorization" = "Bearer GARBAGE_TOKEN_123" }
    Invoke-RestMethod -Uri "$BaseUrl/News/latest" -Headers $headers
} catch {
    Write-Host "Status: $($_.Exception.Response.StatusCode)"
    $_.Exception.Response.GetResponseStream() | ForEach-Object { 
        $reader = New-Object System.IO.StreamReader($_)
        Write-Host "Body: $($reader.ReadToEnd())"
    }
}
