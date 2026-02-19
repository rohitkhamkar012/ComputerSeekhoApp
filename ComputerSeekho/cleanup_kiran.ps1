$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

# Login
$loginBody = @{ username = $AdminUser; password = $AdminPass } | ConvertTo-Json
$response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $loginBody -ContentType "application/json"
$headers = @{ "Authorization" = $response.Headers["Authorization"] }

# Get All Students
$students = Invoke-RestMethod -Uri "$BaseUrl/student/getAll" -Method Get -Headers $headers
$kiran = $students | Where-Object { $_.studentName -eq "Kiran" }

if ($kiran) {
    Write-Host "Deleting Student 'Kiran' (ID: $($kiran.studentId))..."
    Invoke-RestMethod -Uri "$BaseUrl/student/delete/$($kiran.studentId)" -Method Delete -Headers $headers
    Write-Host "Deleted." -ForegroundColor Green
} else {
    Write-Host "Student 'Kiran' not found."
}
