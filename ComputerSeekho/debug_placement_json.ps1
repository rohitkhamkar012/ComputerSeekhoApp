$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

# Login
$loginBody = @{ username = $AdminUser; password = $AdminPass }
$response = Invoke-WebRequest -Uri "$BaseUrl/auth/signIn" -Method Post -Body ($loginBody | ConvertTo-Json) -ContentType "application/json"
$token = $response.Headers["Authorization"]

# Get Placements
$placements = Invoke-RestMethod -Uri "$BaseUrl/placement/all" -Method Get -Headers @{ Authorization = $token }

# Output First Placement JSON
if ($placements) {
    if ($placements -is [System.Collections.IList]) {
        Write-Host "Found $($placements.Count) placements."
        $first = $placements[0]
        Write-Host ($first | ConvertTo-Json -Depth 5)
    } else {
        Write-Host "Found 1 placement."
        Write-Host ($placements | ConvertTo-Json -Depth 5)
    }
} else {
    Write-Host "No placements found."
}
