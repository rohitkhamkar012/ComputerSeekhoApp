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

Test-Step "Use Course 1" {
    $courses = Invoke-RestMethod -Uri "$BaseUrl/course/getAll" -Method Get -Headers $global:headers
    $course1 = $courses | Where-Object { $_.courseId -eq 1 }
    if (-not $course1) { throw "Course 1 not found" }
    if (-not $course1.courseFee) { throw "Course 1 has no fee" }
    $global:courseFee = $course1.courseFee
    Write-Host " Course Fee: $($global:courseFee)"
}

Test-Step "Register Student" {
    $uniq = Get-Random
    $enqBody = @{
        enquirerName = "Pay Student $uniq"
        enquirerMobile = "888$uniq"
        enquirerEmailId = "pay$uniq@test.com"
        courseName = "Java"
        enquirerQuery = "Pay Test"
    } | ConvertTo-Json
    $enqRes = Invoke-RestMethod -Uri "$BaseUrl/enquiries/create" -Method Post -Body $enqBody -Headers $global:headers -ContentType "application/json"
    
    # Get ID
    $all = Invoke-RestMethod -Uri "$BaseUrl/enquiries/getAll" -Method Get -Headers $global:headers
    $enqId = $all[-1].enquiryId

    $stuBody = @{
        studentName = "Pay Student $uniq"
        studentEmail = "pay$uniq@test.com"
        studentMobile = "888$uniq"
        photoUrl = "/images/dummy.jpg"
        course = @{ courseId = 1 }
        batch = @{ batchId = 1 }
    } | ConvertTo-Json
    
    $global:student = Invoke-RestMethod -Uri "$BaseUrl/student/add/$enqId" -Method Post -Body $stuBody -Headers $global:headers -ContentType "application/json"
    
    if (-not $global:student.paymentDue) { 
        # allow fetch if return doesn't have it (though it should)
        Write-Host " (paymentDue miss in response, fetching...)"
        # fetch
    }
}

Test-Step "Make Payment" {
    $payBody = @{
        student = @{ studentId = $global:student.studentId }
        amount = $global:courseFee
        paymentDate = (Get-Date).ToString("yyyy-MM-dd")
        paymentTypeId = @{ paymentTypeId = 3 }
    } | ConvertTo-Json

    $payRes = Invoke-RestMethod -Uri "$BaseUrl/payment/add" -Method Post -Body $payBody -Headers $global:headers -ContentType "application/json"
    if ($payRes.message -ne "Payment Successful") { throw "Payment failed msg: $($payRes.message)" }
}
