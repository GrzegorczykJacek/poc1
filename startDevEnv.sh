#!/bin/sh

COMPOSE_UP='docker-compose up -d'

# Function starts dev environment
dockerComposeUp() {
  $COMPOSE_UP
}

echo '-----     The Dev Env is starting up...     -----'
echo '-----     Starting up docker services with: '$COMPOSE_UP
dockerComposeUp


echo '-----     Warming up for You... ;)'
sleep 10

# Create a kafka connectors
echo -e '-----     Create a connector and outbox event router for: apimessage'
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ -d '{
"name": "apimessage-outbox-connector",
"config": {
  "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
  "plugin.name": "pgoutput",
  "slot.name" : "coordinator",
  "topic.prefix":"coordinator",
  "tasks.max": "1",
  "database.hostname": "postgres",
  "database.port": "5432",
  "database.user": "postgres",
  "database.password": "admin",
  "database.dbname": "coordinator",
  "database.server.name": "localhost",
  "tombstones.on.delete": "false",
  "table.include.list": "coordinator.outbox_event",
  "transforms": "outbox",
  "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
  "transforms.outbox.table.fields.additional.placement" : "id:envelope:uuid"}
}'

sleep 4

echo -e '\n\n-----     Create a connector and outbox event router for: registration'
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ -d '{
"name": "registration-outbox-connector",
"config": {
  "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
  "plugin.name": "pgoutput",
  "slot.name" : "registrator",
  "topic.prefix":"registrator",
  "tasks.max": "1",
  "database.hostname": "postgres",
  "database.port": "5432",
  "database.user": "postgres",
  "database.password": "admin",
  "database.dbname" : "registrator",
  "database.server.name": "localhost",
  "tombstones.on.delete" : "false",
  "table.include.list" : "registrator.outbox_event",
  "transforms": "outbox",
  "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
  "transforms.outbox.table.fields.additional.placement" : "id:envelope:uuid"}
}'