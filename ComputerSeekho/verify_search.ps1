$ErrorActionPreference = "Stop"

try {
    # 1. Login
    Write-Host "Authenticating..."
    $body = @{ username = "admin"; password = "admin@123" } | ConvertTo-Json
    $loginResponse = Invoke-WebRequest -Uri "http://localhost:8080/auth/signIn" -Method Post -ContentType "application/json" -Body $body
    $token = $loginResponse.Headers["Authorization"] -replace "Bearer ", ""

    # 2. Search
    $mobile = "9998887776" # From previous step
    Write-Host "Searching for mobile: $mobile"
    $headers = @{ "Authorization" = "Bearer $token" }
    
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/search?mobile=$mobile" -Headers $headers -Method Get
        Write-Host "Search Success! Found: $($response.enquirerName)"
    } catch {
        Write-Host "Search Failed: $($_.Exception.Message)"
        if ($_.Exception.Response) {
             $stream = $_.Exception.Response.GetResponseStream()
             $reader = New-Object System.IO.StreamReader($stream)
             Write-Host "Body: $($reader.ReadToEnd())"
        }
    }

} catch {
    Write-Error $_
}
