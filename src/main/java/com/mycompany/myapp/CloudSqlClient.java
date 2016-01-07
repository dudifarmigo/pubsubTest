package com.mycompany.myapp;


import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.logging.model.LogEntry;

/**
 * Created by David on 12/29/2015.
 */
public class CloudSqlClient implements AutoCloseable {
    private Connection connection;
    private Statement statement;
    private final String CLOUD_SQL_IP = "146.148.75.170";
    private final String PORT = "3306";
    private final String USER = "root";
    private final String PASSWORD = "farmigo123";
    private final String SCHEMA = "/request_log";
    private final int BATCH_SIZE = 1000;
    public static final int MY_SQL_OPERATION_RETRIES = 3;


    public CloudSqlClient() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + CLOUD_SQL_IP +":" + PORT + SCHEMA
                , USER, PASSWORD);
        statement = connection.createStatement();
    }

    public void insertBatchLogs(List<LogMessage> logMessageList) throws SQLException {
        String queryInsertLog = "INSERT INTO Log (Log_Date,Request_URL,Remote_Host,Severity,MS,Farm,Member,Admin,User_Agent,Nick_name,Log_Message,Id)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE MS=MS";
        PreparedStatement statement = connection.prepareStatement(queryInsertLog);
        int i = 0;
        int j = 1;
        for (LogMessage logMessage:logMessageList) {
            statement.setString(j++, logMessage.getDateTime());
            statement.setString(j++, logMessage.getResource());
            statement.setString(j++, logMessage.getIp());
            statement.setString(j++, logMessage.getSeverity() == null ? "" : logMessage.getSeverity());
            statement.setString(j++, logMessage.getLatency());
            statement.setString(j++, logMessage.getFarm());
            statement.setString(j++, logMessage.getMemberKey());
            statement.setString(j++, logMessage.getAdminKey());
            statement.setString(j++, logMessage.getUserAgent());
            statement.setString(j++, logMessage.getNickname());
            statement.setString(j++, logMessage.getLogMessage().replace("'", ""));
            statement.setString(j++, logMessage.getLogInsertId());
            statement.addBatch();

            j = 1;
            i++;

            if (i == BATCH_SIZE || i == logMessageList.size()) {
                statement.executeBatch();
            }
        }
        statement.clearParameters();
        System.out.println("Inserted into Log " +i + " records");


        //*************
        System.out.println(new Date() + " Before inserting records to KeysForLog");
        //*************

        i = 0;
        String queryInsertKeys = "INSERT INTO KeysForLog (Log_Id,DS_Key)"
                + " VALUES (?, ?)";
        statement = connection.prepareStatement(queryInsertKeys);
        for (LogMessage logMessage:logMessageList){
            if(logMessage.getKeysAsList() != null) {
                List<String> keyList = logMessage.getKeysAsList();
                for(String key:keyList){
                    statement.setString(1, logMessage.getLogInsertId());
                    statement.setString(2, key);
                    statement.addBatch();
                    i++;
                    if(i % BATCH_SIZE == 0){
                        statement.executeBatch();
                    }
                }
            }
        }
        if(i % BATCH_SIZE > 0){
            statement.executeBatch();
        }
        System.out.println("Inserted into KeysForLog " +i + " records");
        System.out.println(new Date() + " After inserting records to KeysForLog");
    }


    @Override
    public void close(){
        try {
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }

    }
}
