$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

# Login
$loginBody = @{ username = $AdminUser; password = $AdminPass } | ConvertTo-Json
$response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body $loginBody -ContentType "application/json"
$headers = @{ "Authorization" = $response.Headers["Authorization"] }

# Get All Payment Types
Write-Host "Fetching Payment Types..."
$types = Invoke-RestMethod -Uri "$BaseUrl/paymentTypes/getAll" -Method Get -Headers $headers

$types | Format-Table

# Check for ID 3
$type3 = $types | Where-Object { $_.paymentTypeId -eq 3 }

if (-not $type3) {
    Write-Host "Payment Type ID 3 not found. Creating it..."
    # We can't easily force ID, but let's try adding types until we get 3? 
    # Or just add "Online Transfer" and see what ID it gets.
    # Usually auto-increment.
    
    $newType = @{ paymentType = "Online Transfer"; paymentTypeDesc = "Credit/Debit Card or UPI" } | ConvertTo-Json
    Invoke-RestMethod -Uri "$BaseUrl/paymentTypes/add" -Method Post -Body $newType -Headers $headers -ContentType "application/json"
    
    # Fetch again to confirm
    $types = Invoke-RestMethod -Uri "$BaseUrl/paymentTypes/getAll" -Method Get -Headers $headers
    $types | Format-Table
} else {
    Write-Host "Payment Type ID 3 exists: $($type3.paymentType)" -ForegroundColor Green
}
