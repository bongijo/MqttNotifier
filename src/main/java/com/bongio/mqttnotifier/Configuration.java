package com.bongio.mqttnotifier;

public class Configuration {
    private final String topic;
    private final String broker;
    private final String clientId;
    private final String appName;
    private final String notificationTitle;
    private final String notificationMessage;

    public Configuration(String topic, String broker, String clientId, String appName, String notificationTitle, String notificationMessage) {
        this.topic = topic;
        this.broker = broker;
        this.clientId = clientId;
        this.appName = appName;
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
    }

    public String getTopic() {
        return topic;
    }

    public String getBroker() {
        return broker;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAppName() {
        return appName;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }
}
