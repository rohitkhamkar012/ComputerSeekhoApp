$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"
$AdminUser = "admin"
$AdminPass = "admin@123"

function Test-Endpoint {
    param (
        [string]$Name,
        [string]$Url,
        [string]$Method = "Get",
        [hashtable]$Headers = @{},
        [object]$Body = $null
    )

    Write-Host "Testing $Name ($Method $Url)..." -NoNewline
    try {
        $params = @{
            Uri = $Url
            Method = $Method
            Headers = $Headers
            ContentType = "application/json"
        }
        if ($Body) { $params.Body = ($Body | ConvertTo-Json -Depth 5) }

        # Use -SessionVariable to capture headers if needed, but Invoke-RestMethod returns the body.
        # To get headers, we might need Invoke-WebRequest.
        # But for the token, we need the response headers from Login.
        
        if ($Name -eq "Login") {
            $response = Invoke-WebRequest @params
            $token = $response.Headers["Authorization"]
            if ($token) {
                Write-Host " SUCCESS" -ForegroundColor Green
                 # Clean up 'Bearer ' if present twice, though usually it's just the value
                return $token
            } else {
                Write-Host " FAILED (No Token in Header)" -ForegroundColor Red
                return $null
            }
        } else {
            $response = Invoke-RestMethod @params
            Write-Host " SUCCESS" -ForegroundColor Green
            if ($response -is [System.Collections.IList]) {
                Write-Host "  count: $($response.Count)" -ForegroundColor Gray
            }
            return $response
        }
    } catch {
        Write-Host " FAILED" -ForegroundColor Red
        Write-Host "  Error: $_"
        if ($_.Exception.Response) {
             # Try to read error body
             try {
                $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
                $errBody = $reader.ReadToEnd()
                Write-Host "  Body: $errBody" -ForegroundColor Yellow
             } catch {}
        }
        return $null
    }
}

Write-Host "--- ComputerSeekho API Verification ---"

# 1. Login
$headers = @{}
$loginBody = @{ username = $AdminUser; password = $AdminPass }
$token = Test-Endpoint -Name "Login" -Url "$BaseUrl/auth/signIn" -Method "Post" -Body $loginBody

if (-not $token) {
    Write-Host "Stopping: Login failed." -ForegroundColor Red
    exit 1
}

if (-not $token.StartsWith("Bearer ")) {
    # If the server sends just the token, prepend Bearer. 
    # But usually standard is 'Bearer <token>'. Let's check.
    # Based on standard JWT filters, it might be just the token or with Bearer.
    # We will assume what we got is the value to send back.
    # Update: Common Spring filter adds "Bearer " prefix or expects it.
    # Let's assume the header value is correct as is.
}

$headers["Authorization"] = $token

# 2. Get Courses
Test-Endpoint -Name "Get All Courses" -Url "$BaseUrl/course/getAll" -Headers $headers

# 3. Get Enquiries
Test-Endpoint -Name "Get All Enquiries" -Url "$BaseUrl/enquiries/getAll" -Headers $headers

# 4. Get News
Test-Endpoint -Name "Get Latest News" -Url "$BaseUrl/News/latest" -Headers $headers

# 5. Get Placements
Test-Endpoint -Name "Get All Placements" -Url "$BaseUrl/placement/all" -Headers $headers

Write-Host "`n--- Verification Complete ---"
