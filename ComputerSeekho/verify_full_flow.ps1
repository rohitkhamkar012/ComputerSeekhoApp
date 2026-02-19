$ErrorActionPreference = "Stop"

function Assert-Success($Condition, $Message) {
    if ($Condition) { Write-Host "   [PASS] $Message" -ForegroundColor Green }
    else { Write-Error "   [FAIL] $Message" }
}

try {
    # 1. Login
    Write-Host "1. Authenticating..."
    $body = @{ username = "admin"; password = "admin@123" } | ConvertTo-Json
    $loginResponse = Invoke-WebRequest -Uri "http://localhost:8080/auth/signIn" -Method Post -ContentType "application/json" -Body $body
    $token = $loginResponse.Headers["Authorization"]
    if ($token -match "Bearer") { $token = $token -replace "Bearer ", "" }
    Assert-Success ($token.Length -gt 10) "Token retrieved"

    # 2. Add Enquiry
    Write-Host "2. Adding Test Enquiry..."
    $enquiry = @{
        enquirerName = "__TEST_USER_2__"
        enquirerMobile = "1234567890"
        enquirerEmailId = "test@example.com"
        courseName = "Integration Test"
        enquirerQuery = "Does this work?"
    } | ConvertTo-Json
    $headers = @{ "Authorization" = "Bearer $token"; "Content-Type" = "application/json" }
    
    try {
        $addResp = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/create" -Method Post -Headers $headers -Body $enquiry
        Assert-Success ($addResp.message -eq "Enquiry Added") "Enquiry creation API returned success"
    } catch {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        Write-Error "Add Failed: $($reader.ReadToEnd())"
    }

    # 3. List Enquiries
    Write-Host "3. fetching Enquiries..."
    $list = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Headers $headers -Method Get
    Assert-Success ($list.Count -gt 0) "Enquiries list not empty. Count: $($list.Count)"
    
    $found = $list | Where-Object { $_.enquirerName -eq "__TEST_USER_2__" }
    Assert-Success ($found -ne $null) "Found the test enquiry in the list"
    
    if ($found) {
        Write-Host "   -> Verified matching fields: enquirerName='$($found.enquirerName)', courseName='$($found.courseName)'"
        $found | ConvertTo-Json -Depth 1
    }

} catch {
    Write-Host "CRITICAL FAILURE: $_" -ForegroundColor Red
    if ($_.Exception.Response) {
         $stream = $_.Exception.Response.GetResponseStream()
         $reader = New-Object System.IO.StreamReader($stream)
         Write-Host "Backend Error: $($reader.ReadToEnd())"
    }
}
