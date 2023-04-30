# collect-service

curl --location --request GET 'http://localhost:8080/api/v1/total?year=2023&month=4&day=30'

curl --location --request POST 'http://localhost:8080/api/v1/message' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJteWtoYWlsb0Bzb21icmFpbmMuY29tIiwiZXhwIjoxNTk2MDY3MjAwLCJpYXQiOjE1NzgwOTYwMDB9.v1KcomG9w1hmvRWlAc-68GBE6-46K3CFAjdR0nk35yo' \
--data-raw '{
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
