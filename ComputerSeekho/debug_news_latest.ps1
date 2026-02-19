$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
try {
    Invoke-RestMethod -Uri "$BaseUrl/News/latest"
} catch {
    Write-Host "Status: $($_.Exception.Response.StatusCode)"
    $_.Exception.Response.GetResponseStream() | ForEach-Object { 
        $reader = New-Object System.IO.StreamReader($_)
        Write-Host "Body: $($reader.ReadToEnd())"
    }
}
