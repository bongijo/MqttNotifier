package com.bongio.mqttnotifier;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MqttNotification {
    public void messageReceived(String topic, MqttMessage message);
}
