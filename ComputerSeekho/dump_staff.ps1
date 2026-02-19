$response = Invoke-RestMethod -Uri "http://localhost:8080/staff/getAll" -Method Get
$response | Select-Object staffId, staffName, staffEmail, staffUsername, staffRole | Format-Table -AutoSize
