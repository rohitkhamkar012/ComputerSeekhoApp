$response = Invoke-RestMethod -Uri "http://localhost:8080/enquiries/getAll" -Method Get
$response | Select-Object enquiryId, enquirerName, enquiryIsActive, enquiryStatus | Format-Table
