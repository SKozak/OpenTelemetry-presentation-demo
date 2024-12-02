#!/bin/sh
export INFRASTRUCTURE_HOST=host.docker.internal #if you use docker in linux then this should be your host's ip instead of host.docker.internal
export ELK_HOST=host.docker.internal #if you use docker in linux then this should be your host's ip instead of host.docker.internal
docker compose -f ./infrastructure-compose.yml up -d
docker compose -f ./otel-elk/docker-compose.yml up -d

until curl -s -I http://localhost:5601 | grep -q 'HTTP/1.1 302 Found'
do
  echo "waiting for kibana to start"
  sleep 1
done
echo "kibana ready"

echo "Starting connector"
curl -X DELETE http://localhost:8083/connectors/outbox-connector-simple

curl -X POST \
     -H "Content-Type: application/json" \
     --data '
     {
       "name": "outbox-connector-simple",
       "config": {
         "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
         "database.hostname": "db",
         "database.port": "5432",
         "database.user": "postgres",
         "database.password": "testPassword",
         "database.dbname": "postgres",
         "database.server.name": "dbserver1",
         "table.include.list": "public.debezium_outbox_events",
         "topic.prefix": "outbox",
         "plugin.name": "pgoutput",
         "transforms": "outbox",
         "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
         "transforms.outbox.table.fields.additional.placement": "trace:header:traceparent",
         "transforms.outbox.table.expand.json.payload": "true",
         "key.converter": "org.apache.kafka.connect.storage.StringConverter",
         "value.converter": "org.apache.kafka.connect.json.JsonConverter",
         "value.converter.schemas.enable": "false"
       }
     }
     ' \
     http://localhost:8083/connectors -w "\n"

echo "connector starterd"

echo "starting applications"

docker compose up -d
docker compose logs -f