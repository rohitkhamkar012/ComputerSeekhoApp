$ErrorActionPreference = "Stop"

try {
    Write-Host "Authenticating..."
    $body = @{ username = "admin"; password = "admin@123" } | ConvertTo-Json
    $loginResponse = Invoke-WebRequest -Uri "http://localhost:8080/auth/signIn" -Method Post -ContentType "application/json" -Body $body
    $token = $loginResponse.Headers["Authorization"] -replace "Bearer ", ""
    Write-Host "Login Success."

    Write-Host "Creating Enquiry..."
    $enquiry = @{
        enquirerName = "Test User"
        enquirerMobile = "9876543210"
        enquirerEmailId = "test@test.com"
        courseName = "Test Course"
        enquirerQuery = "Test Query"
    } | ConvertTo-Json

    $headers = @{ "Authorization" = "Bearer $token"; "Content-Type" = "application/json" }
    
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/create" -Method Post -Headers $headers -Body $enquiry
        Write-Host "Success! Response: $($response | ConvertTo-Json -Depth 5)"
    } catch {
        Write-Host "Failed to create enquiry. Status: $($_.Exception.Response.StatusCode)"
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        Write-Host "Error Response: $($reader.ReadToEnd())"
    }

} catch {
    Write-Error "Script Failed: $_"
}
