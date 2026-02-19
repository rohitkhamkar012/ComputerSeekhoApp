$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$LogFile = "test_report.txt"
"SYSTEM TEST REPORT - $(Get-Date)" | Out-File $LogFile

function Test-Step($Name, $ScriptBlock) {
    Write-Host "TEST: $Name..." -NoNewline
    try {
        & $ScriptBlock
        Write-Host " [PASS]" -ForegroundColor Green
        "PASS: $Name" | Out-File $LogFile -Append
    } catch {
        Write-Host " [FAIL]" -ForegroundColor Red
        Write-Host "    Error: $($_.Exception.Message)"
        "FAIL: $Name - $($_.Exception.Message)" | Out-File $LogFile -Append
        if ($_.Exception.Response) {
             $stream = $_.Exception.Response.GetResponseStream()
             $reader = New-Object System.IO.StreamReader($stream)
             $body = $reader.ReadToEnd()
             Write-Host "    Response: $body"
        }
        # Continue testing other parts? No, usually stop on fail.
        # throw $_
    }
}

# 1. PUBLIC ACCESS
Test-Step "Get News" { Invoke-RestMethod -Uri "$BaseUrl/News/latest" }
Test-Step "Get Courses" { Invoke-RestMethod -Uri "$BaseUrl/course/getAll" }
Test-Step "Get Faculty" { Invoke-RestMethod -Uri "$BaseUrl/staff/getAll" }
Test-Step "Submit Contact Form" { 
    $body = @{ enquirerName="Test Visitor"; email="visitor@test.com"; enquiryMessage="Hello World"; mobile="9876543210" } | ConvertTo-Json
    Invoke-RestMethod -Uri "$BaseUrl/getInTouch/add" -Method Post -Body $body -ContentType "application/json"
}

# 2. ADMIN AUTH
$Token = ""
Test-Step "Admin Login" {
    $body = @{ username="admin"; password="admin@123" } | ConvertTo-Json
    $resp = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $body -ContentType "application/json"
    $t = $resp.Headers["Authorization"]
    if ($t -match "Bearer") { $t = $t -replace "Bearer ", "" }
    $global:Token = $t
    if ($Token.Length -lt 10) { throw "Invalid Token" }
}

$Headers = @{ "Authorization" = "Bearer $Token"; "Content-Type" = "application/json" }

# 3. MASTERS
$Rand = Get-Random -Minimum 1000 -Maximum 9999
$CourseName = "E2E Course $Rand"
Test-Step "Add New Course" {
    $body = @{ courseName=$CourseName; courseDuration=30; courseFee=5000; courseIsActive=$true } | ConvertTo-Json
    Invoke-RestMethod -Uri "$BaseUrl/course/add" -Method Post -Headers $Headers -Body $body
}

$CourseId = 0
Test-Step "Get Course ID" {
    $res = Invoke-RestMethod -Uri "$BaseUrl/course/getAll"
    $c = $res | Where-Object { $_.courseName -eq $CourseName } | Select-Object -First 1
    $global:CourseId = [int]$c.courseId
    Write-Host "DEBUG: Course ID: $CourseId"
}

Test-Step "Create Batch" {
     $body = @{ 
         batchName="E2E Batch"; 
         batchStartTime="2026-01-01"; 
         batchEndTime="2026-12-31"; 
         course=@{courseId=$CourseId}; 
         batchIsActive=$true 
     } | ConvertTo-Json
     Invoke-RestMethod -Uri "$BaseUrl/batch/add" -Method Post -Headers $Headers -Body $body
}
$BatchId = 0
Test-Step "Get Batch ID" {
    $res = Invoke-RestMethod -Uri "$BaseUrl/batch/all" -Headers $Headers
    $b = $res | Where-Object { $_.batchName -eq "E2E Batch" } | Select-Object -First 1
    $global:BatchId = [int]$b.batchId
    Write-Host "DEBUG: Batch ID: $BatchId"
}

# 4. ENQUIRY CYCLE
$EnquiryMobile = "$(Get-Random -Minimum 6000000000 -Maximum 6999999999)"
Test-Step "Create Enquiry" {
    $body = @{ 
        enquirerName="E2E Student"; 
        enquirerMobile=$EnquiryMobile; 
        enquirerEmailId="e2e@student.com"; 
        courseName=$CourseName; 
        enquirerQuery="Admission?" 
    } | ConvertTo-Json
    Invoke-RestMethod -Uri "$BaseUrl/enquiries/create" -Method Post -Headers $Headers -Body $body
}

$EnquiryId = 0
Test-Step "Search Enquiry (New API)" {
    $res = Invoke-RestMethod -Uri "$BaseUrl/enquiries/search?mobile=$EnquiryMobile" -Headers $Headers
    # Search might return list if duplicates exist, take first
    if ($res -is [array]) { $res = $res | Select-Object -First 1 }
    
    if ($res.enquirerName -ne "E2E Student") { throw "Name mismatch" }
    $global:EnquiryId = [int]$res.enquiryId
    Write-Host "DEBUG: Captured ID: $EnquiryId"
}

Test-Step "Update Follow-up" {
    Invoke-RestMethod -Uri "$BaseUrl/enquiries/updateMessage/$EnquiryId" -Method Put -Headers $Headers -Body "Called, interested."
}

# 5. STUDENT REGISTRATION
Test-Step "Register Student" {
    if ($EnquiryId -eq 0) { throw "No Enquiry ID" }
    $body = @{
        studentId=0;
        studentName="E2E Student";
        studentEmail="e2e@student.com";
        studentMobile=$EnquiryMobile;
        studentAddress="Test Address";
        studentGender="Male";
        paymentDue=5000.0;
        course=@{ courseId=$CourseId };
        batch=@{ batchId=$BatchId }
    } | ConvertTo-Json
    Invoke-RestMethod -Uri "$BaseUrl/student/add/$EnquiryId" -Method Post -Headers $Headers -Body $body
}

# 6. CLOSURE VERIFICATION
Test-Step "Verify Enquiry Closed" {
    $res = Invoke-RestMethod -Uri "$BaseUrl/enquiries/getid/$EnquiryId" -Headers $Headers
    if ($res.enquiryIsActive -eq $true) { throw "Enquiry should be inactive" }
}

Write-Host "ALL TESTS COMPLETED. See test_report.txt" -ForegroundColor Cyan
