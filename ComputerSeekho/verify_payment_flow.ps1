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
        Write-Host "Details: $($_.Exception.Response.GetResponseStream() | %{ (New-Object System.IO.StreamReader($_)).ReadToEnd() })"
        exit 1
    }
}

# 1. Login
$headers = @{}
Test-Step "Login" {
    $loginBody = @{ username = $AdminUser; password = $AdminPass }
    $response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body ($loginBody | ConvertTo-Json) -ContentType "application/json"
    $headers["Authorization"] = $response.Headers["Authorization"]
    $headers["Content-Type"] = "application/json"
}

# 2. Create Dummy Enquiry
$enquiryId = 0
Test-Step "Create Enquiry" {
    $uniq = Get-Random
    $body = @{
        enquirerName = "Test User $uniq"
        enquirerMobile = "999$uniq"
        enquirerEmailId = "test$uniq@example.com"
        courseName = "Java Full Stack"
        enquirerQuery = "Test Query"
        enquiryStatus = "Open"
    }
    Invoke-RestMethod -Uri "$BaseUrl/enquiries/create" -Method Post -Body ($body | ConvertTo-Json) -Headers $headers
    
    # Fetch to get ID (assuming it's the last one or search by phone)
    $all = Invoke-RestMethod -Uri "$BaseUrl/enquiries/getAll" -Method Get -Headers $headers
    $enquiryId = $all[-1].enquiryId
}

# 3. Register Student (Simulate React Frontend call)
$studentId = 0
Test-Step "Register Student" {
    # Valid Course and Batch IDs needed. Assuming 1 and 1 exist.
    $body = @{
        studentName = "Test Student"
        studentEmail = "teststudent@example.com"
        studentMobile = "8888888888"
        studentAddress = "Test Address"
        studentGender = "Male"
        paymentDue = 45000.0 # Full Fee
        course = @{ courseId = 1 }
        batch = @{ batchId = 1 }
    }
    
    # Note: Backend now returns Student object!
    $student = Invoke-RestMethod -Uri "$BaseUrl/student/add/$enquiryId" -Method Post -Body ($body | ConvertTo-Json) -Headers $headers
    $studentId = $student.studentId
    
    if (-not $studentId) { throw "Student ID not returned!" }
}

# 4. Make Payment (Simulate Dummy Gateway)
Test-Step "Process Payment" {
    $body = @{
        student = @{ studentId = $studentId }
        amount = 45000.0
        paymentDate = (Get-Date).ToString("yyyy-MM-dd")
        paymentTypeId = @{ paymentTypeId = 3 } # Online
    }
    
    Invoke-RestMethod -Uri "$BaseUrl/payment/add" -Method Post -Body ($body | ConvertTo-Json) -Headers $headers
}

# 5. Verify Student Due is 0
Test-Step "Verify Due Balance" {
    $s = Invoke-RestMethod -Uri "$BaseUrl/student/get/$studentId" -Method Get -Headers $headers
    if ($s.paymentDue -eq 0) {
        Write-Host " Balance Cleared." -NoNewline
    } else {
        throw "Payment Due is $($s.paymentDue), expected 0."
    }
}

Write-Host "`nAll Tests Passed! Backend is Logic is Solid." -ForegroundColor Cyan
