# 1. Create Public Enquiry (No Auth Header)
$body = @{
    enquirerName = "PublicUser_Test"
    enquirerMobile = "9998887776"
    enquirerEmailId = "public@test.com"
    enquirerQuery = "Testing auto-assign"
    courseName = "CDAC"
    enquiryDate = (Get-Date).ToString("yyyy-MM-dd")
} | ConvertTo-Json

Write-Host "Creating Public Enquiry..."
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/create" -Method Post -Body $body -ContentType "application/json"
    Write-Host "Enquiry Created: $($response.message)"
} catch {
    Write-Error "Failed to create enquiry: $_"
    exit
}

# 2. Verify Assignment
# We need to fetch the enquiry. Since we don't have the ID returned explicitly in a friendly way (just message), let's search by mobile.
Start-Sleep -Seconds 2

Write-Host "Verifying Assignment..."
try {
    $enquiry = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/search?mobile=9998887776" -Method Get
    
    if ($enquiry) {
        Write-Host "Enquiry Found: $($enquiry.enquirerName)"
        if ($enquiry.staff.staffUsername -eq "admin") {
            Write-Host "SUCCESS: Enquiry assigned to 'admin'" -ForegroundColor Green
        } else {
            Write-Host "FAILURE: Enquiry assigned to '$($enquiry.staff.staffUsername)'" -ForegroundColor Red
        }
    } else {
        Write-Error "Enquiry not found!"
    }
} catch {
    Write-Error "Failed to fetch enquiry: $_"
}
