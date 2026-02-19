$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

# Login
$loginBody = @{ username = $AdminUser; password = $AdminPass }
$response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body ($loginBody | ConvertTo-Json) -ContentType "application/json"
$token = $response.Headers["Authorization"]

# Helper to Headers
$headers = @{ Authorization = $token; "Content-Type" = "application/json" }

# 1. Fetch Student 12
try {
    $student = Invoke-RestMethod -Uri "$BaseUrl/student/get/12" -Method Get -Headers $headers
    Write-Host "fetched Student 12: $($student.studentName) (Batch: $($student.batch.batchId))"
} catch {
    Write-Host "Student 12 not found. Trying Student 2."
    $student = Invoke-RestMethod -Uri "$BaseUrl/student/get/2" -Method Get -Headers $headers
}

# 2. Payload
# Note: Placement.java has "studentID", "recruiterID", "batch" fields.
# But JSON serialization might expect "studentID" or "studentId" based on getters.
# Lombok @Data generates getStudentID(). So JSON field is "studentID".
$payload = @{
    studentID = @{ studentId = $student.studentId }
    recruiterID = @{ recruiterId = 1 }
    batch = @{ batchId = $student.batch.batchId }
}

Write-Host "Sending Payload:"
Write-Host ($payload | ConvertTo-Json -Depth 5)

# 3. Post Placement
try {
    $res = Invoke-RestMethod -Uri "$BaseUrl/placement/add" -Method Post -Body ($payload | ConvertTo-Json -Depth 5) -Headers $headers
    Write-Host "Success!" -ForegroundColor Green
    Write-Host $res
} catch {
    Write-Host "Failed!" -ForegroundColor Red
    Write-Host $_
    if ($_.Exception.Response) {
         $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
         Write-Host "Body: $($reader.ReadToEnd())" -ForegroundColor Yellow
    }
}
