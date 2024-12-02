#!/bin/sh
docker compose -f ./infrastructure-compose.yml down
docker compose -f ./otel-elk/docker-compose.yml down
docker compose down