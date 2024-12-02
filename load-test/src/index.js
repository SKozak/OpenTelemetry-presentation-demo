import http from 'k6/http';
import {sleep} from 'k6';
import {generateRequest} from "./generators/request";

export const options = {
    // A number specifying the number of VUs to run concurrently.
    vus: 20,
    // A string specifying the total duration of the test run.
    duration: '10m',
};


const submit = () => {

    // Application request
    const applicationRequest = generateRequest();
    const payload = JSON.stringify(applicationRequest);

    const url = 'http://localhost:8080/applications';

    let res = http.post(url, payload, {
        headers: { 'Content-Type': 'application/json' },
    });

    // Batch request
    const batchRequest = generateRequest();
    const batchPayload = JSON.stringify(batchRequest);

    const batchUrl = 'http://localhost:8080/batch-applications';

    http.post(batchUrl, batchPayload, {
        headers: { 'Content-Type': 'application/json' },
    });

    // Debezium
    const debeziumRequest = generateRequest();
    const debeziumPayload = JSON.stringify(debeziumRequest);

    const debeziumUrl = 'http://localhost:8080/applications/debezium';

    http.post(debeziumUrl, debeziumPayload, {
        headers: { 'Content-Type': 'application/json' },
    });

    let randomWaitTime = Math.floor(Math.random() * 10) + 1; // Random wait time between 1 and 10 seconds
    sleep(randomWaitTime); // Wait for random time
};

export default function () {
    submit();
}
