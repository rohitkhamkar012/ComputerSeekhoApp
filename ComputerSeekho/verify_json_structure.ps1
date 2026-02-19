$ErrorActionPreference = "Stop"

try {
    # 1. Login
    Write-Host "Authenticating..."
    $body = @{ username = "admin"; password = "admin@123" } | ConvertTo-Json
    $loginResponse = Invoke-WebRequest -Uri "http://localhost:8080/auth/signIn" -Method Post -ContentType "application/json" -Body $body
    $token = $loginResponse.Headers["Authorization"]
    if ($token -match "Bearer") { $token = $token -replace "Bearer ", "" }

    # 2. Get Enquiries
    Write-Host "Fetching Enquiries..."
    $headers = @{ "Authorization" = "Bearer $token" }
    $response = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Headers $headers -Method Get
    
    if ($response.Count -gt 0) {
        Write-Host "JSON_STRUCTURE:"
        $response[0] | ConvertTo-Json -Depth 2
    } else {
        Write-Host "No enquiries found. Creating one..."
        $newEnquiry = @{
            enquirerName = "Structure Test"
            enquirerMobile = "9998887776"
            enquirerEmailId = "structure@test.com"
            courseName = "JSON Check"
            enquirerQuery = "Checking keys"
        } | ConvertTo-Json
        Invoke-RestMethod -Uri "http://localhost:8080/enquiries/create" -Method Post -Headers $headers -Body $newEnquiry
        
        # Fetch again
        $response2 = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Headers $headers -Method Get
        Write-Host "JSON_STRUCTURE:"
        $response2[0] | ConvertTo-Json -Depth 2
    }

} catch {
    Write-Host "Error: $_"
    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        Write-Host "Server Error: $($reader.ReadToEnd())"
    }
}
