$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"

Write-Host "1. Authenticating..."
$body = @{ username="admin"; password="admin@123" } | ConvertTo-Json
try {
    # Use Invoke-WebRequest to get headers clearly
    $res = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $body -ContentType "application/json"
    
    # Extract Token - Case Insensitive lookup
    $Headers = $res.Headers
    $Token = ""
    if ($Headers["Authorization"]) { $Token = $Headers["Authorization"][0] }
    elseif ($Headers["authorization"]) { $Token = $Headers["authorization"][0] }
    
    if (-not $Token) { throw "No Token in Headers" }
    $Token = $Token -replace "Bearer ", ""
    Write-Host "Success. Token found."
} catch {
    Write-Host "LOGIN FAILED: $_"
    exit
}

$AuthHeader = @{ "Authorization" = "Bearer $Token"; "Content-Type" = "application/json" }

Write-Host "2. Deleting Junk News (ID 11, 12)..."
try {
    Invoke-RestMethod -Uri "$BaseUrl/News/delete/12" -Method Delete -Headers $AuthHeader -ErrorAction SilentlyContinue
    Invoke-RestMethod -Uri "$BaseUrl/News/delete/11" -Method Delete -Headers $AuthHeader -ErrorAction SilentlyContinue 
    Write-Host "Deleted junk."
} catch {
    Write-Host "Delete warning: $_"
}

Write-Host "3. Verifying Home Page Data..."
$latest = Invoke-RestMethod -Uri "$BaseUrl/News/latest"
Write-Host "Latest News Title: $($latest.newsTitle)"
if (-not $latest.newsTitle) {
    Write-Host "WARNING: Data is empty?"
}
