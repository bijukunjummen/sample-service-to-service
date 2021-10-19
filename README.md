# Spring Boot app on Knative serving

A sample demonstrating using https://github.com/knative/serving[knative serving] for a https://spring.io/projects/spring-boot[Spring Boot 2] app deployment


## Running it locally

Start app using the following command:

```sh
./gradlew -p applications/sample-boot-knative-app clean bootRun
```

## Testing
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


= Deploying to Kubernetes using Knative serving

* Install Knative using the instructions https://github.com/knative/docs/blob/master/install/README.md[here]
* Run the following commands

[source, bash]
----
cd knative
kubectl apply -f service.yml
----

== Testing

[source, bash]
----
export GATEWAY_URL=$(echo $(minikube ip):$(kubectl get svc knative-ingressgateway -n istio-system -o 'jsonpath={.spec.ports[?(@.port==80)].nodePort}'))
export APP_DOMAIN=$(kubectl get services.serving.knative.dev sample-boot-knative-service  -o="jsonpath={.status.domain}")
----

With Httpie:

[source, bash]
----
http http://${GATEWAY_URL}/messages Host:"${APP_DOMAIN}" id=1 payload=test delay=100
----

Or with Curl:

[source, bash]
----
curl -X "POST" "http://${GATEWAY_URL}/messages" \
     -H "Accept: application/json" \
     -H "Content-Type: application/json" \
     -H "Host: ${APP_DOMAIN}" \
     -d $'{
  "id": "1",
  "payload": "one",
  "delay": "300"
}'
----

Load Test:
[source, bash]
----
./gradlew -p applications/load-scripts  -DTARGET_URL=http://${GATEWAY_URL} knativeLoad -DHOST_HEADER=${APP_DOMAIN} -DSIM_USERS=20
----


