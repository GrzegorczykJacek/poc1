## POC - Outbox pattern implementation with CDC Debezium tool (with a simple log trace id example)

The app presents an implementation of Outbox Pattern with Postgres and Debezium for Kafka.
It also shows a simple example of logging trace ids through http calls with spring cloud (sleuth and open feign)

### To run the apps first run the dev environment:
- Postgres databases
- Kafka server
- Debezium connect
- akhq

by running the script:

``` bash
./startDevEnv.sh
```

### Test the (outbox pattern) app by sending a post request to Coordinator (using the request providev in 'http' folder):
```
{
    "message": "Some example message...",
    "author": "Some example author name..."
}
```
to: http://localhost:8080/api/v1/messages

### Test the (log tracing) app by sending a post request to http-handler-1 and see that the same trace id (passed by the calls of http-handlers 1 and 2) is present in the logs of http-handler-3:
```
{
    "message": "Some example message...",
    "author": "Some example author name..."
}
```
to: http://localhost:8084/api/v1/messages