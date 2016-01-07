package com.mycompany.myapp;



import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.pubsub.model.PubsubMessage;
import com.google.api.services.pubsub.model.ReceivedMessage;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 12/31/2015.
 */
public class LogsToSql {
    public static List<String> insertLogs(List<ReceivedMessage> receivedMessages) throws ClassNotFoundException {
        List<String> ackIds = new ArrayList<>();
        List<LogMessage> logMessageList = parseLog(receivedMessages, ackIds);
        if(logMessageList == null){
            return ackIds;
        }
        int retry = CloudSqlClient.MY_SQL_OPERATION_RETRIES;
        while (retry > 0) {
            try (CloudSqlClient cloudSqlClient = new CloudSqlClient()) {
                cloudSqlClient.insertBatchLogs(logMessageList);
                retry = 0;
                return ackIds;
            } catch (SQLException e) {
                e.printStackTrace();
                retry--;
            }
        }
        return null;
    }

    private static List<LogMessage> parseLog(List<ReceivedMessage> receivedMessages, List<String> ackIds){
        List<LogMessage> logMessageList = new ArrayList<>();
        for (ReceivedMessage receivedMessage:receivedMessages){
            PubsubMessage pubsubMessage = receivedMessage.getMessage();
            if (pubsubMessage != null && pubsubMessage.decodeData() != null) {
                String log = null;
                try {
                    log = new String(pubsubMessage.decodeData(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    continue;
                }
                LogMessage logMessage = null;
                try {
                    JsonParser parser = new JacksonFactory().createJsonParser(log);
                    logMessage = parser.parse(LogMessage.class);
                    logMessageList.add(logMessage);
                    ackIds.add(receivedMessage.getAckId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return logMessageList;
    }

}
