# 1. Fetch Admin Staff
$staffUsername = "admin"
# Assuming there is an endpoint to get staff by username or similar. 
# If not, we might have to use getAll and filter.
$staffResponse = Invoke-RestMethod -Uri "http://localhost:8080/staff/getAll" -Method Get
$adminStaff = $staffResponse | Where-Object { $_.staffUsername -eq $staffUsername }

if (-not $adminStaff) {
    Write-Error "Admin staff not found!"
    exit
}

Write-Host "Found Admin Staff ID: $($adminStaff.staffId)"

# 2. Fetch Enquiry 7
$enquiryId = 7
$enquiry = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getid/$enquiryId" -Method Get

if (-not $enquiry) {
    Write-Error "Enquiry $enquiryId not found!"
    exit
}

Write-Host "Fetched Enquiry for: $($enquiry.enquirerName)"

# 3. Assign Staff
$enquiry.staff = $adminStaff

# 4. Update Enquiry
# Convert to JSON with depth to ensure nested objects are included if needed
$jsonPayload = $enquiry | ConvertTo-Json -Depth 5

try {
    Invoke-RestMethod -Uri "http://localhost:8080/enquiries/update/$enquiryId" -Method Put -Body $jsonPayload -ContentType "application/json"
    Write-Host "Success! Assigned Enq $enquiryId to $($adminStaff.staffUsername)"
} catch {
    Write-Error "Failed to update: $_"
}
