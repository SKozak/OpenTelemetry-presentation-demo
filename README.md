# Disclaimer
This repository is not intended to show best practices in software development. It is a example of using open telemetry
in microservices world. Showing how to manage situations where tracing is broken, and how to restore it if needed.

# About

This is sample project created for my presentation https://cupofcodes.pl/talks/ to show how to use open telemetry in microservices world. It includes:

**microservices** [docker-compose.yml](docker-compose.yml):
- `analyzes` ( utilizes kafka and kafka streams )
- `creditcards` ( Outbox/inbox pattern, postgresql, debezium )
- `fraudService` ( webflux, reactive mongo)
- `identityRegistry`
- `mockService` ( not traceable, only for show how trace looks like when service is not traceable)

**infrastructure** [infrastructure-compose.yml](infrastructure-compose.yml):
- `kafka`
- `mongo`
- `postgresql`
- `consul`
- `kafka-connect`
- `elasticsearch`

**observability** [docker-compose.yml](otel-elk/docker-compose.yml):
- `kibana`
- `apm-server`
- `otel-collector`


## Prerequisites
1. Docker and Docker Compose installed.
2. Java (minimum version 11).
3. Optionally, Node.js and k6 installed (if you not want to use docker image for lead tests).


## How to Build and Run

1. You should have docker and docker compose installed on your machine.
2. Run `mvn clean package jib:dockerBuild -DskipTests` in the root directory of the project.
3. By default, ELK_HOST and INFRASTRUCTURE_HOST is set to host.docker.internal (mac and windows) if you use docker in linux then uou should replace this in .env to your host's docker ip instead of host.docker.internal
4. Run scrip `./run-all.sh` in the root directory of the project.

## how to stop ?
Run `./stop-all.sh` in the root directory of the project.


## GUI
Open browser and go to http://localhost:5601/app/apm/services to see services in kibana


## How to generate load ?
After run infra and app, uou can generate load to see traces in two ways (manual and automatic):

### Manualy

use SWAGGER
http://localhost:8080/swagger-ui/index.html#/applications-resource/createApplication

to Post

valid body:
```json
{
  "client": {
    "name": "John Doe",
    "email": "johndoe@example.com",
    "pesel": "92431543123",
    "birthDate": "1985-05-01",
    "address": {
      "street": "123 Main St",
      "city": "Anytown",
      "country": "USA",
      "postalCode": "12345"
    },
    "income": 75000,
    "document": {
      "type": "ID_CARD",
      "number": "A1234567",
      "expiry": "2025-05-01"
    },
    "employment": {
      "employerName": "ABC Inc.",
      "jobTitle": "Software Developer",
      "startDate": "2018-01-01"
    }
  },
  "cardType": "Visa",
  "requestedCardLimit": 10000
}
```

### Automatic with docker

1. run 
```dockerfile
   docker build -t load-test-image .
```
2. run
```dockerfile
   docker run --rm load-test-image
```
### Automatic without docker 

1. Installed node on your machine. (https://nodejs.org/en/download/prebuilt-installer)
2. Installed k6 on your machine. (https://grafana.com/docs/k6/latest/set-up/install-k6/)
3. run `npm install` in the `load-test` directory.
4. build tests by run `npm pretest` in the `load-test` directory.
5. run load tests by run `npm test` in the `load-test` directory.

## Where I can find traces ?

Open browser and go to http://localhost:5601/app/apm/services to see services in kibana