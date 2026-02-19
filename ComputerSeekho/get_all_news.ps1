$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
try {
    $res = Invoke-RestMethod -Uri "$BaseUrl/News/all"
    Write-Host "Count: $($res.Count)"
    $res | ConvertTo-Json
} catch {
    Write-Host "Failed: $_"
}
