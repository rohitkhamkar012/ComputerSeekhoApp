$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

# Login
$loginBody = @{ username = $AdminUser; password = $AdminPass } | ConvertTo-Json
$response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $loginBody -ContentType "application/json"
$headers = @{ "Authorization" = $response.Headers["Authorization"] }

# Get All Students
Write-Host "Fetching Students..."
$students = Invoke-RestMethod -Uri "$BaseUrl/student/getAll" -Method Get -Headers $headers

$kiran = $students | Where-Object { $_.studentName -eq "Kiran" }

if ($kiran) {
    Write-Host "Student 'Kiran' FOUND:" -ForegroundColor Green
    $kiran | Format-List
    
    # Check Payments
    Write-Host "Fetching Payments..."
    $payments = Invoke-RestMethod -Uri "$BaseUrl/payment/getAll" -Method Get -Headers $headers
    $kiranPay = $payments | Where-Object { $_.student.studentName -eq "Kiran" }
    
    if ($kiranPay) {
        Write-Host "Payment for 'Kiran' FOUND:" -ForegroundColor Green
        $kiranPay | Format-List
    } else {
        Write-Host "Payment for 'Kiran' NOT FOUND" -ForegroundColor Red
    }

} else {
    Write-Host "Student 'Kiran' NOT FOUND" -ForegroundColor Red
}
