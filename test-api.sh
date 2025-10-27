#!/bin/bash

echo "=== Testing API Endpoints ==="
echo ""

echo "1. Health Check"
curl -X GET http://localhost:8080/api/v1/health
echo -e "\n"

echo "2. Register User"
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User"
  }')
echo $REGISTER_RESPONSE
echo -e "\n"

echo "3. Login"
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }')
echo $LOGIN_RESPONSE

ACCESS_TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
REFRESH_TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"refreshToken":"[^"]*' | cut -d'"' -f4)
echo -e "\n"

echo "4. Create Simulation (with token)"
curl -X POST http://localhost:8080/api/v1/simulations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -d '{
    "params": "{\"amount\": 100000, \"term\": 12}"
  }'
echo -e "\n"

echo "5. Get Simulations (with token)"
curl -X GET http://localhost:8080/api/v1/simulations \
  -H "Authorization: Bearer $ACCESS_TOKEN"
echo -e "\n"

echo "6. Refresh Token"
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d "{
    \"refreshToken\": \"$REFRESH_TOKEN\"
  }"
echo -e "\n"

echo "7. Access Simulation without token (should fail with 401)"
curl -X GET http://localhost:8080/api/v1/simulations
echo -e "\n"

