#Dockerfile to build a k6 container with Node.js and npm installed for running load tests
FROM grafana/k6:0.55.0
USER root

RUN apk add --no-cache nodejs npm

WORKDIR /load-test

COPY load-test /load-test
RUN chown -R k6:k6 /load-test
USER k6

RUN npm install && npm run pretest

ENTRYPOINT ["/bin/sh", "-c"]
CMD ["npm run test"]
