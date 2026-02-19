$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$Token = ""

# 1. Login to get Token (for delete)
$body = @{ username="admin"; password="admin@123" } | ConvertTo-Json
# 1. Login to get Token
$body = @{ username="admin"; password="admin@123" } | ConvertTo-Json
try {
    $res = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $body -ContentType "application/json"
    $Token = $res.Headers["Authorization"][0] -replace "Bearer ", ""
    Write-Host "Got Token."
} catch {
    Write-Host "Login Failed: $_"
    exit
}

$AuthHeader = @{ "Authorization" = "Bearer $Token"; "Content-Type" = "application/json" }

# 2. Get All News and Delete
$all = Invoke-RestMethod -Uri "$BaseUrl/News/all"
if ($all) {
    foreach ($n in $all) {
        Invoke-RestMethod -Uri "$BaseUrl/News/delete/$($n.newsId)" -Method Delete -Headers $AuthHeader
        Write-Host "Deleted News ID: $($n.newsId)"
    }
}

# 3. Add Fresh News
$news = @{ newsTitle="Welcome to Computer Seekho"; newsDescription="New batches starting soon!"; newsUrl="http://test.com" } | ConvertTo-Json
Invoke-RestMethod -Uri "$BaseUrl/News/add" -Method Post -Headers $AuthHeader -Body $news
Write-Host "Added Fresh News."
