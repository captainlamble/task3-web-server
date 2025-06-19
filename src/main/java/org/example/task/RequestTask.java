package org.example.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class RequestTask implements Task<String> {
    private static final Logger logger = LogManager.getLogger(RequestTask.class);
    private final String clientId;
    private final String requestData;

    public RequestTask(String clientId, String requestData) {
        this.clientId = clientId;
        this.requestData = requestData;
    }

    @Override
    public String call() throws Exception {
        logger.info("fetching clients request {}: {}", clientId, requestData);

        TimeUnit.MILLISECONDS.sleep(200 + (int)(Math.random() * 300));

        String response = "response: " + requestData;
        logger.info("clients request {} fetched: {}", clientId, requestData);
        return response;
    }
}