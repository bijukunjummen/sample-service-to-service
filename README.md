# Service to Service Call Demonstration

A sample demonstrating service to service calls in a variety of scenarios:
1. Cloud Run on GCP
2. Kubernetes
3. Istio 
4. KNative

## Running it locally
Start a service application using the following command:

```shell
./gradlew -p sample-producer bootRun
```

Start a client to the service:
```shell
./gradlew -p sample-caller bootRun
```

## Testing

A call to the Producer application:
```shell
curl -X "POST" "http://localhost:8080/messages" \
     -H "Accept: application/json" \
     -H "Content-Type: application/json" \
     -d $'{
  "id": "1",
  "payload": "one",
  "delay": "1000"
}'
```

A call to the client to the Producer:
```shell
curl -X "POST" "http://localhost:8081/messages" \
     -H "Accept: application/json" \
     -H "Content-Type: application/json" \
     -d $'{
  "id": "1",
  "payload": "one",
  "delay": "1000"
}'
```