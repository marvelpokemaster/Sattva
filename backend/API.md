# Utsavam V1 API Documentation

Base URL: `https://utsavam-backend.utsavam-api.workers.dev`

## Authentication
All endpoints under the "Authenticated" section require a valid Firebase ID Token passed in the Authorization header.
`Authorization: Bearer <FIREBASE_ID_TOKEN>`

## Public Endpoints

### 1. Health Check
`GET /api/v1/health`
Returns the status of the worker and available endpoints.

### 2. List Gaushalas
`GET /api/v1/catalog/gaushalas`
Query Parameters:
- `city` (optional): Filter gaushalas by city/location name.

Response:
```json
{
  "gaushalas": [ { "id": "...", "name": "...", ... } ],
  "count": 1
}
```

### 3. List Animals
`GET /api/v1/catalog/animals`
Query Parameters:
- `gaushalaId` (optional): Filter animals belonging to a specific gaushala.

Response:
```json
{
  "animals": [ { "id": "...", "name": "...", "gaushalaId": "..." } ],
  "count": 1
}
```

### 4. Welfare Statistics
`GET /api/v1/welfare`
Returns system-wide impact statistics.

Response:
```json
{
  "totalRescued": 450,
  "activeSanctuaries": 1,
  "totalMealsServed": 13500
}
```

### 5. AI Assistant
`POST /api/v1/ai/ask`
Request Body:
```json
{
  "query": "What are the benefits of feeding cows?"
}
```
Response:
```json
{
  "response": "..."
}
```

## Authenticated Endpoints

### 6. Get Profile
`GET /api/v1/profile`
Returns the authenticated user's profile information.

Response:
```json
{
  "profile": {
    "id": "uid...",
    "displayName": "Devotee",
    "city": "Mumbai"
  }
}
```

### 7. Update Profile
`PUT /api/v1/profile`
Request Body:
```json
{
  "displayName": "New Name",
  "city": "Pune"
}
```
Response:
```json
{
  "success": true
}
```

### 8. List Donations
`GET /api/v1/donations`
Returns all seva contributions for the authenticated user.

Response:
```json
{
  "donations": [
    {
      "id": "donation_id",
      "targetType": "GAUSHALA",
      "amountRupees": 500,
      "paymentStatus": "PENDING"
    }
  ]
}
```

### 9. Create Pending Donation
`POST /api/v1/donations`
Request Body:
```json
{
  "targetType": "GAUSHALA",
  "targetId": "gaushala_id",
  "targetName": "Shri Krishna Gaushala",
  "amountRupees": 500,
  "sevaCategory": "Fodder"
}
```
Response:
```json
{
  "id": "generated_donation_id",
  "status": "PENDING"
}
```
