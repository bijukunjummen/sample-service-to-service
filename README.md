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

A simple call to the Producer application:

```shell
curl -v -X "POST" "http://localhost:8080/producer/messages" \
     -H "Accept: application/json" \
     -H "Content-Type: application/json" \
     -d $'{
  "id": "1",
  "payload": "one",
  "delay": "1000",
  "responseCode": 200
}'
```

A call to the caller, which inturn dispatches to the producer:

```shell
curl -X "POST" "http://localhost:8081/caller/messages" \
     -H "Accept: application/json" \
     -H "Content-Type: application/json" \
     -d $'{
  "id": "1",
  "payload": "one",
  "delay": "1000"
}'
```