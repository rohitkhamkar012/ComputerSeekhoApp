$response = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Method Get
$enquiry = $response | Where-Object { $_.enquiryId -eq 7 }

if ($enquiry) {
    Write-Host "Updating Enquiry 7..."
    # We might need an endpoint to update staff or just update the enquiry
    # Let's try to update it using the update endpoint if available, but usually we need a specific 'assign' endpoint or update content.
    # If there is no assign endpoint, we might have to do it via database or verify if the update enpoint handles staff.
    
    # Actually, simpler to just inspect the controller first to see how to assign staff.
    # But for now, let's just log what we found.
    Write-Host "Enquiry Found: $($enquiry.enquirerName)"
    Write-Host "Current Staff: $($enquiry.staff)"
} else {
    Write-Host "Enquiry 7 not found."
}
