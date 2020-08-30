package com.bongio.mqttnotifier;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;

public class MqttSubClient implements MqttCallback {
    private List<MqttNotification> mqttNotifications = new ArrayList<>();
    private String broker;
    private String clientId;
    private String topic;

    public MqttSubClient(String broker, String clientId, String topic) {
        this.broker = broker;
        this.clientId = clientId;
        this.topic = topic;
    }

    public void connectAndSub() throws MqttException {
        MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        client.connect(connOpts);
        client.setCallback(this);
        client.subscribe(topic);
    }

    public void attach(MqttNotification mqttNotification) {
        mqttNotifications.add(mqttNotification);
    }

    public void detach(MqttNotification mqttNotification) {
        mqttNotifications.remove(mqttNotification);
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        for (MqttNotification mqttNotification : mqttNotifications) {
            mqttNotification.messageReceived(s, mqttMessage);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
