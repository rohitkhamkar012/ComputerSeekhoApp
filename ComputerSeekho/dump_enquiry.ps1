$ErrorActionPreference = "Stop"
try {
    # Login
    $body = @{ username = "admin"; password = "admin@123" } | ConvertTo-Json
    $loginResp = Invoke-WebRequest -Uri "http://localhost:8080/auth/signIn" -Method Post -ContentType "application/json" -Body $body
    $token = $loginResp.Headers["Authorization"] -replace "Bearer ", ""

    # Get Enquiries
    $headers = @{ "Authorization" = "Bearer $token" }
    $procs = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Headers $headers -Method Get
    
    if ($procs.Count -gt 0) {
        Write-Host "JSON_OUTPUT_START"
        $procs[0] | ConvertTo-Json -Depth 5
        Write-Host "JSON_OUTPUT_END"
    } else {
        Write-Host "No items found to verify structure."
    }
} catch {
    Write-Error $_
}
