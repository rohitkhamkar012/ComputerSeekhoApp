$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"

Write-Host "--- 1. Testing Admin Login ---"
$body = @{ username="admin"; password="admin@123" } | ConvertTo-Json
try {
    $res = Invoke-RestMethod -Uri "$BaseUrl/auth/signIn" -Method Post -Body $body -ContentType "application/json"
    Write-Host "LOGIN SUCCESS!" -ForegroundColor Green
    Write-Host "Token: $($res.jwtToken)"
} catch {
    Write-Host "LOGIN FAILED!" -ForegroundColor Red
    Write-Host $_.Exception.Message
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        Write-Host "Body: $($reader.ReadToEnd())" -ForegroundColor Yellow
    }
}

Write-Host "`n--- 2. Testing Latest News ---"
try {
    $news = Invoke-RestMethod -Uri "$BaseUrl/News/latest"
    if ($news) {
        Write-Host "NEWS FOUND!" -ForegroundColor Green
        Write-Host "Title: $($news.newsTitle)"
        Write-Host "Desc: $($news.newsDescription)"
    } else {
        Write-Host "NEWS RETURNED NULL/EMPTY" -ForegroundColor Yellow
    }
} catch {
    Write-Host "NEWS FETCH FAILED!" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
