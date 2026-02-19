$ErrorActionPreference = "Stop"
try {
    $body = @{ username = "admin"; password = "admin@123" } | ConvertTo-Json
    $loginResp = Invoke-WebRequest -Uri "http://localhost:8080/auth/signIn" -Method Post -ContentType "application/json" -Body $body
    $token = $loginResp.Headers["Authorization"] -replace "Bearer ", ""

    $list = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Headers @{ "Authorization" = "Bearer $token" } -Method Get
    Write-Host "Enquiry Count: $($list.Count)"
    if ($list.Count -gt 0) {
        Write-Host "Latest Enquiry: $($list[$list.Count-1].enquirerName) - $($list[$list.Count-1].courseName)"
    }
} catch {
    Write-Error $_
}
