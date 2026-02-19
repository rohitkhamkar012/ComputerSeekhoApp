$ErrorActionPreference = "Stop"

try {
    Write-Host "1. Authenticating as Admin..."
    $body = @{
        username = "admin"
        password = "admin@123"
    } | ConvertTo-Json

    $loginResponse = Invoke-WebRequest -Uri "http://localhost:8080/auth/signIn" -Method Post -ContentType "application/json" -Body $body
    
    $token = $loginResponse.Headers["Authorization"]
    if (-not $token) { throw "No Authorization Header returned" }
    $token = $token -replace "Bearer ", ""
    Write-Host "   -> Login Success."

    Write-Host "2. Fetching Enquiries..."
    $headers = @{ "Authorization" = "Bearer $token" }
    try {
        $enquiries = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Headers $headers -Method Get
        Write-Host "   -> Success! Found $($enquiries.Count) enquiries."
        if ($enquiries.Count -gt 0) {
            Write-Host "   -> Sample Data: $($enquiries[0] | ConvertTo-Json -Depth 1)"
        }
    } catch {
        Write-Error "   -> Failed to fetch enquiries. Status: $($_.Exception.Response.StatusCode)"
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $body = $reader.ReadToEnd()
        Write-Host "   -> Response Body: $body"
        if ($body -eq "") { Write-Host "   -> Body is EMPTY." }
    }

} catch {
    Write-Error "Verification Failed: $_"
    if ($_.Exception.Response) {
         $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
         Write-Host "   -> Global Error Body: $($reader.ReadToEnd())"
    }
}
