package com.mycompany.myapp;



import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by David on 12/30/2015.
 */

public class LogMessage {
    @Key
    private String timestamp;
    @Key
    private int httpStatusCode;
    @Key
    private String resource;
    @Key
    private String ip;
    @Key
    private String latency;
    @Key
    private String severity;
    @Key
    private String userAgent;
    @Key
    private String nickname;
    @Key
    private String farm;
    @Key
    private String adminKey;
    @Key
    private String logMessage;
    @Key
    private String memberKey;
    @Key
    private String logInsertId;
    @Key
    private Set<String> keys;

    public LogMessage() {
    }

    public LogMessage(String timestamp, int httpStatusCode, String resource, String ip
            , String latency, String severity, String userAgent, String nickname
            , String farm, String adminKey, String logmessage, String memberKey, String logInsertId, Set<String> keys) {
        this.timestamp = timestamp;
        this.httpStatusCode = httpStatusCode;
        this.resource = resource;
        this.ip = ip;
        this.latency = latency;
        this.severity = severity;
        this.userAgent = userAgent;
        this.nickname = nickname;
        this.farm = farm;
        this.adminKey = adminKey;
        this.logMessage = logmessage;
        this.memberKey = memberKey;
        this.logInsertId = logInsertId;
        this.keys = keys;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public void setAdminKey(String adminKey) {
        this.adminKey = adminKey;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public void setMemberKey(String memberKey) {
        this.memberKey = memberKey;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getResource() {
        return this.resource;
    }


    public String getIp() {
        return ip;
    }


    public String getLatency() {
        return latency;
    }


    public String getSeverity() {
        return severity;
    }


    public String getUserAgent() {
        return userAgent;
    }


    public String getNickname() {
        return nickname;
    }


    public String getFarm() {
        return farm;
    }

    public String getAdminKey() {
        return adminKey;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public String getMemberKey() {
        return memberKey;
    }

    public String getLogInsertId() {
        return logInsertId;
    }

    public void setLogInsertId(String logInsertId) {
        this.logInsertId = logInsertId;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public List<String> getKeysAsList(){
        if(keys != null){
            return new ArrayList<>(keys);
        }
        return null;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

    public String getDateTime() {
        String dateTime = this.timestamp;
        try {
            com.google.appengine.repackaged.org.joda.time.DateTime d =
                    new com.google.appengine.repackaged.org.joda.time.DateTime(DateTime.parseRfc3339(dateTime));
            dateTime = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(d.toDate());
        }
        catch (Exception e){} //ignore
        return dateTime;
    }
}