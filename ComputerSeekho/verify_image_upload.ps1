$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

function Test-Step {
    param($Name, $Block)
    Write-Host "Wrapper: $Name..." -NoNewline
    try {
        & $Block
        Write-Host " [OK]" -ForegroundColor Green
    } catch {
        Write-Host " [FAILED]" -ForegroundColor Red
        Write-Host "Error: $_"
        if ($_.Exception.Response) {
             $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
             Write-Host "Response: $($reader.ReadToEnd())"
        }
        exit 1
    }
}

Test-Step "Authentication" {
    $loginBody = @{ username = $AdminUser; password = $AdminPass } | ConvertTo-Json
    $response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $loginBody -ContentType "application/json"
    $global:headers = @{ "Authorization" = $response.Headers["Authorization"] }
}

Test-Step "Upload Image" {
    $filePath = "d:\Gravity\ComputerSeekho\ComputerSeekho\dummy.jpg"
    $fileBytes = [System.IO.File]::ReadAllBytes($filePath)
    $boundary = [System.Guid]::NewGuid().ToString()
    $LF = "`r`n"
    
    $bodyLines = (
        "--$boundary",
        "Content-Disposition: form-data; name=`"file`"; filename=`"dummy.jpg`"",
        "Content-Type: image/jpeg",
        "",
        [System.Text.Encoding]::GetEncoding('iso-8859-1').GetString($fileBytes),
        "--$boundary--"
    ) -join $LF

    $uploadRes = Invoke-WebRequest -Uri "$BaseUrl/image/upload" -Method Post -Body $bodyLines -ContentType "multipart/form-data; boundary=$boundary" -Headers $global:headers
    $json = $uploadRes.Content | ConvertFrom-Json
    $global:uploadedUrl = $json.url
    Write-Host " Uploaded to: $global:uploadedUrl"
    if (-not $global:uploadedUrl.StartsWith("images/")) { throw "Invalid URL response" }
}

# Need an enquiry first
Test-Step "Setup Enquiry" {
    $uniq = Get-Random
    $body = @{
        enquirerName = "Image User $uniq"
        enquirerMobile = "777$uniq"
        enquirerEmailId = "img$uniq@example.com"
        courseName = "Java"
        enquirerQuery = "Img Test"
    } | ConvertTo-Json
    $res = Invoke-RestMethod -Uri "$BaseUrl/enquiries/create" -Method Post -Body $body -Headers $global:headers -ContentType "application/json"
    
    $all = Invoke-RestMethod -Uri "$BaseUrl/enquiries/getAll" -Method Get -Headers $global:headers
    $global:enquiryId = $all[-1].enquiryId
}

Test-Step "Register Student with Image" {
    $body = @{
        studentName = "Image Student"
        studentEmail = "imgstudent@test.com"
        studentMobile = "7778889990"
        photoUrl = $global:uploadedUrl
        course = @{ courseId = 1 }
        batch = @{ batchId = 1 }
        paymentDue = 50000
    } | ConvertTo-Json
    
    $student = Invoke-RestMethod -Uri "$BaseUrl/student/add/$global:enquiryId" -Method Post -Body $body -Headers $global:headers -ContentType "application/json"
    if ($student.photoUrl -ne $global:uploadedUrl) { throw "Photo URL mismatch in DB" }
}

Write-Host "Image Upload & Registration Verified!" -ForegroundColor Cyan
