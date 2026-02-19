# Fetch enquiries for admin
$url = "http://localhost:8080/enquiries/getbystaff/Admin"
$outFile = "debug_admin_output.txt"
"Fetching from: $url" | Out-File $outFile

try {
    $response = Invoke-RestMethod -Uri $url -Method Get
    "Count: $($response.Count)" | Out-File $outFile -Append
    
    if ($response.Count -gt 0) {
        "First Enquiry Data:" | Out-File $outFile -Append
        $response[0] | ConvertTo-Json -Depth 2 | Out-File $outFile -Append
        
        "enquiryStatus: '$($response[0].enquiryStatus)'" | Out-File $outFile -Append
        "enquiryIsActive: '$($response[0].enquiryIsActive)'" | Out-File $outFile -Append
    } else {
        "No enquiries found for user 'admin'." | Out-File $outFile -Append
    }
} catch {
    "Failed to fetch: $_" | Out-File $outFile -Append
}
