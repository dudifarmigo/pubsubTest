package com.mycompany.myapp;

import java.util.Date;
import java.util.List;

/**
 * Created by David on 12/29/2015.
 */
public class PubSubToCloudSql {

    public static void main(String[] args) throws Exception {
        PubSubSubscriptionClient pubSubSubscriptionClient = new PubSubSubscriptionClient();
        while (true) {
            pubSubSubscriptionClient.pullMessages();
            if(pubSubSubscriptionClient.getReceivedMessages() == null){
                System.out.println("Sleep 5 seconds, no new message");
                Thread.sleep(5000);
                continue;
            }
            System.out.println(pubSubSubscriptionClient.getReceivedMessages().size());

            System.out.println(new Date() + " Before insertLogs");
            List<String> ackIds = LogsToSql.insertLogs(pubSubSubscriptionClient.getReceivedMessages());
            System.out.println(new Date() + " After insertLogs");


            // Acknowledgement deadline configured to 600 second
            if (ackIds != null)
                pubSubSubscriptionClient.acknowledge(ackIds);
            Thread.sleep(1000);
        }
    }
}

