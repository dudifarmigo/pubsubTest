package com.mycompany.myapp;


import com.google.api.services.pubsub.Pubsub;
import com.google.api.services.pubsub.model.*;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

/**
 * Created by David on 12/29/2015.
 */
public class PubSubSubscriptionClient {
    private final String PROJECT_ID = "farmigo-automation-3";
    //private final String SUBSCRIPTION_NAME = "projects/farmigo-automation-3/subscriptions/output-request-log";
    private final String SUBSCRIPTION_NAME = "projects/farmigo-automation-3/subscriptions/output-request-log-for-testing";
    private List<ReceivedMessage> receivedMessages;
    private final int BATCH_SIZE = 1000;
    private Pubsub client;

    public PubSubSubscriptionClient() throws IOException {
        client = PubsubUtils.getClient();
    }

    public void pullMessages() throws IOException {
        PullRequest pullRequest = new PullRequest()
                .setReturnImmediately(true)
                .setMaxMessages(BATCH_SIZE);

        PullResponse pullResponse;
        pullResponse = client.projects().subscriptions()
                .pull(SUBSCRIPTION_NAME, pullRequest)
                .execute();
        receivedMessages = pullResponse.getReceivedMessages();
    }

    public void acknowledge(List<String> ackIds) throws IOException {
        AcknowledgeRequest ackRequest = new AcknowledgeRequest();
        ackRequest.setAckIds(ackIds);
        client.projects().subscriptions()
                .acknowledge(SUBSCRIPTION_NAME, ackRequest)
                .execute();
    }

    public List<ReceivedMessage> getReceivedMessages() {
        return receivedMessages;
    }
}
