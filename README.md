# collect-service

## Description

Collect service is responsible for collecting data from the data source and storing it in the database.

## Keycloak configuration for local development
KeyClock: http://localhost:8080/realms/collection-service/.well-known/openid-configuration

## Create user
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/login \
  --header 'Content-Type: application/json' \
  --data '{
	"email": "user2@test.com",
	"password": "12345"
}'
```

## Get token
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/login \
  --header 'Content-Type: application/json' \
  --data '{
	"email": "user2@test.com",
	"password": "12345"
}'
```

## Send message
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/message \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIyQHRlc3QuY29tIiwiYXV0aG9yaXRpZXMiOlsiVVNFUiJdLCJpYXQiOjE2ODkxNTMwNTQsImV4cCI6MTcxOTE1MzA1NH0.ZYJhDXllHWv-VcvYFAwZNxIAd4mJ5SGByh3gcNBH3Lw' \
  --header 'Content-Type: application/json' \
  --data '{
    "vendor_id": 1,
    "tpep_pickup_datetime": "2023-11-04T12:34:56.000Z",
    "tpep_dropoff_datetime": "2023-11-05T12:34:56.000Z",
    "passenger_count": "1",
    "trip_distance": "1.34",
    "rate_code_id": "1",
    "store_and_fwd_flag": "N",
    "pu_location_id": "238",
    "do_location_id": "236",
    "payment_type": "2",
    "fare_amount": "10",
    "extra": "0",
    "mta_tax": "0.5",
    "tip_amount": "0",
    "tolls_amount": "0",
    "improvement_surcharge": "0.3",
    "total_amount": "10.8"
}'
```
