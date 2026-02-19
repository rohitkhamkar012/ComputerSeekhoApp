$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

# Login
$loginBody = @{ username = $AdminUser; password = $AdminPass }
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body ($loginBody | ConvertTo-Json) -ContentType "application/json"
    $token = $response.Headers["Authorization"]
    Write-Host "Login Success"
} catch {
    Write-Host "Login Failed"
    exit 1
}

# Get Students
try {
    $students = Invoke-RestMethod -Uri "$BaseUrl/student/getAll" -Method Get -Headers @{ Authorization = $token }
    
    if ($students) {
        if ($students -is [System.Collections.IList]) {
             Write-Host "Found $($students.Count) students."
             # Output first student to check structure
             if ($students.Count -gt 0) {
                 Write-Host "First Student:"
                 Write-Host ($students[0] | ConvertTo-Json -Depth 5)
             }
        } else {
             Write-Host "Found 1 student (Object)."
             Write-Host ($students | ConvertTo-Json -Depth 5)
        }
    } else {
        Write-Host "No students returned (Array is null or empty)."
    }
} catch {
    Write-Host "Failed to fetch students. Error: $_"
    if ($_.Exception.Response) {
         $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
         Write-Host "Body: $($reader.ReadToEnd())"
    }
}
