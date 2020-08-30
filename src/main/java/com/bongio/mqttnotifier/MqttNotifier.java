package com.bongio.mqttnotifier;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonEncodingException;
import com.squareup.moshi.Moshi;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class MqttNotifier implements MqttNotification {
    private static SystemTrayNotification systemTrayNotification = null;

    //Default values
    private static String appName = "Mqtt Notifier";
    private static String notificationTitle = "New Message";
    private static String notificationMessage = "";
    private static String clientId = "MqttNotifier";

    public static void main(String[] args) {
        try {
            systemTrayNotification = new SystemTrayNotification(appName);
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Start reading configuration file
        Configuration configuration = null;

        //Check if configuration file exists and if it's a well formed json file
        try {
            List<String> lines = Files.readAllLines(new File("./settings.json").toPath());
            StringBuilder jsonString = new StringBuilder();
            lines.forEach(jsonString::append);

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Configuration> jsonAdapter = moshi.adapter(Configuration.class).nonNull();
            configuration = jsonAdapter.nonNull().fromJson(jsonString.toString());
        } catch (NoSuchFileException e) {
            systemTrayNotification.displayError(appName, "No settings file found!\nPlease add an settings.json file");
            e.printStackTrace();
            System.exit(2);
        } catch (JsonEncodingException e) {
            systemTrayNotification.displayError(appName, "Setting file malformed");
            e.printStackTrace();
            System.exit(3);
        }
        catch (IOException e) {
            systemTrayNotification.displayError(appName, "Error reading configuration file");
            e.printStackTrace();
            System.exit(4);
        }

        //Check the presence of optional settings
        if(configuration.getAppName() != null) {
            appName = configuration.getAppName();
            systemTrayNotification.setToolTip(appName);
        }

        if(configuration.getNotificationTitle() != null)
            notificationTitle = configuration.getNotificationTitle();

        if(configuration.getNotificationMessage() != null)
            notificationMessage = configuration.getNotificationMessage();

        if(configuration.getClientId() != null)
            clientId = configuration.getClientId();

        //Check the presence of required settings
        try {
            if(configuration.getTopic() == null)
                throw new IllegalArgumentException("Missing a topic");
            else if(configuration.getBroker() == null)
                throw new IllegalArgumentException("Missing a broker");
        } catch (IllegalArgumentException e) {
            systemTrayNotification.displayError(appName, "Error in configuration file:\n" + e.getMessage());
            e.printStackTrace();
            System.exit(5);
        }
        //End reading configuration file

        try {
            MqttSubClient mqttSubClient = new MqttSubClient(configuration.getBroker(), clientId, configuration.getTopic());
            mqttSubClient.attach(new MqttNotifier());
            mqttSubClient.connectAndSub();
        } catch (MqttException | IllegalArgumentException e) {
            systemTrayNotification.displayError(appName, "Error during connection:\n" + e.getMessage());
            e.printStackTrace();
            System.exit(6);
        }

        systemTrayNotification.notify(appName, "Successful connection\nBroker: " + configuration.getBroker() + "\nTopic: " + configuration.getTopic());
    }

    @Override
    public void messageReceived(String topic, MqttMessage message) {
        systemTrayNotification.notify(notificationTitle, notificationMessage.isEmpty() ? message.toString() : notificationMessage);
    }
}
