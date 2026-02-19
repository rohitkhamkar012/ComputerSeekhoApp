$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$body = @{ username="admin"; password="admin@123" } | ConvertTo-Json
try {
    $res = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $body -ContentType "application/json"
    $Token = $res.Headers["Authorization"][0] -replace "Bearer ", ""
    $AuthHeader = @{ "Authorization" = "Bearer $Token"; "Content-Type" = "application/json" }
    
    try {
        Invoke-RestMethod -Uri "$BaseUrl/News/delete/12" -Method Delete -Headers $AuthHeader
        Write-Host "Deleted 12."
    } catch {
        Write-Host "Delete Failed."
        $_.Exception.Response.GetResponseStream() | ForEach-Object { 
            $reader = New-Object System.IO.StreamReader($_)
            Write-Host $reader.ReadToEnd()
        }
    }
} catch {
    Write-Host "Auth Failed"
}
