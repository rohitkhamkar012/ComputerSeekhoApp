$response = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Method Get
Write-Host "Total Enquiries: $($response.Count)"
$active = $response | Where-Object { $_.enquiryIsActive -eq $true }
Write-Host "Active Enquiries: $($active.Count)"
$adminEnquiries = $active | Where-Object { $_.staff.staffUsername -eq 'admin' }
Write-Host "Active Enquiries assigned to 'admin': $($adminEnquiries.Count)"
$active | Select-Object enquiryId, enquirerName, enquiryIsActive, @{Name="Staff";Expression={$_.staff.staffUsername}} | Format-Table
