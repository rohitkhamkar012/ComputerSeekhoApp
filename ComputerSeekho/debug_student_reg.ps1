$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"

function Get-Token {
    $body = @{ username="admin"; password="admin@123" } | ConvertTo-Json
    $resp = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $body -ContentType "application/json"
    return ($resp.Headers["Authorization"] -replace "Bearer ", "")
}

try {
    $Token = Get-Token
    $Headers = @{ "Authorization" = "Bearer $Token"; "Content-Type" = "application/json" }

    # Setup Data
    $Rand = Get-Random
    $CourseName = "Debug Course $Rand"
    $BatchName = "Debug Batch $Rand"
    $Mobile = "$(Get-Random -Minimum 6000000000 -Maximum 6999999999)"
    
    # 1. Add Course
    try {
        Invoke-RestMethod -Uri "$BaseUrl/course/add" -Method Post -Headers $Headers -Body (@{ courseName=$CourseName; courseDuration=30; courseFee=5000.0; courseIsActive=$true } | ConvertTo-Json) | Out-Null
        $res = Invoke-RestMethod -Uri "$BaseUrl/course/getAll" -Headers $Headers
        $c = $res | Where-Object { $_.courseName -eq $CourseName } | Select-Object -First 1
        
        # DEBUG
        "C is: $c" | Set-Content "debug_step1_c.txt"
        "C Type: $($c.GetType().Name)" | Add-Content "debug_step1_c.txt"
        
        $CourseId = [int]$c.courseId
        "Course Created: $CourseId" | Set-Content "debug_step1.txt"
    } catch { throw "Step 1 Failed: $_" }

    # 2. Add Batch
    try {
        Invoke-RestMethod -Uri "$BaseUrl/batch/add" -Method Post -Headers $Headers -Body (@{ batchName=$BatchName; batchStartTime="2026-01-01"; batchEndTime="2026-12-31"; course=@{courseId=$CourseId}; batchIsActive=$true } | ConvertTo-Json) | Out-Null
        $res = Invoke-RestMethod -Uri "$BaseUrl/batch/all" -Headers $Headers
        $b = $res | Where-Object { $_.batchName -eq $BatchName } | Select-Object -First 1
        $BatchId = [int]$b.batchId
        "Batch Created: $BatchId" | Set-Content "debug_step2.txt"
    } catch { throw "Step 2 Failed: $_" }

    # 3. Create Enquiry
    try {
        Invoke-RestMethod -Uri "$BaseUrl/enquiries/create" -Method Post -Headers $Headers -Body (@{ enquirerName="Debug Student"; enquirerMobile=$Mobile; enquirerEmailId="debug@test.com"; courseName=$CourseName; enquirerQuery="Test" } | ConvertTo-Json) | Out-Null
        $res = Invoke-RestMethod -Uri "$BaseUrl/enquiries/search?mobile=$Mobile" -Headers $Headers
        if ($res -is [array]) { $res = $res | Select-Object -First 1 }
        $EnquiryId = [int]$res.enquiryId
        "Enquiry Created: $EnquiryId" | Set-Content "debug_step3.txt"
    } catch { throw "Step 3 Failed: $_" }

    # 4. Register Student
    try {
        Write-Output "Registering Student with EnquiryId: $EnquiryId, CourseId: $CourseId, BatchId: $BatchId"
        
        $body = @{
            studentId=0; 
            studentName="Debug Student";
            studentEmail="debug@test.com";
            studentMobile=$Mobile;
            studentAddress="Debug Addr";
            studentGender="Male";
            paymentDue=5000.0;
            course=@{ courseId=$CourseId };
            batch=@{ batchId=$BatchId }
        } | ConvertTo-Json
        
        $body | Set-Content "payload_dump.txt"
        
        Invoke-RestMethod -Uri "$BaseUrl/student/add/$EnquiryId" -Method Post -Headers $Headers -Body $body
        Write-Output "SUCCESS!"
        "SUCCESS" | Set-Content "debug_step4.txt"

    } catch {
        Write-Output "FAILED!"
        "FAILED: $($_.Exception.Message)" | Set-Content "debug_step4_error.txt"
        if ($_.Exception.Response) {
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $errBody = $reader.ReadToEnd()
            $errBody | Set-Content "debug_step4_body.txt"
            Write-Output "Body: $errBody"
        }
    }

} catch {
    Write-Output "GLOBAL FAIL: $_"
}
