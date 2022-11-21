## POC - recovery mechanism in async integrated environment

The app presents an implementation of Outbox Pattern with Postgres and Debezium for Kafka

### To run the apps first run the dev environment:
- postgres databases
- Kafka server
- Debezium connect
by running the script.

```./startDevEnv.sh```

### Test the app by sending a post request to Coordinator:
```
{
    "message": "Some example message...",
    "author": "Some example author name..."
}
```
to: http://localhost:8080/api/v1/messages