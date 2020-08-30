# Mqtt Notifier 🔔

Mqtt Notifier is a simple windows application that subscribes to a topic on a **MQTT** broker and **notifies** incoming messages via **windows notifications**.

<img title="" src="notification.gif" alt="" width="484">

<Insert notification example image>

## Getting Started

### Prerequisites

[Download](https://www.oracle.com/java/technologies/javase-jre8-downloads.html) and install the latest `JRE 8` (Java Runtime Environment) version.

### Installing

- Download the jar executable (`MqttNotifier.jar`) and the sample configuration file and (`sample.settings.json`) of the [latest version](https://github.com/bongijo/MqttNotifier/releases/latest)

- Place them in the same directory anywhere on your machine

- (Optional) Create a shortcut to `MqttNotifier.jar` and move it to `%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup` to have `Mqtt Notifier` run on boot

### Setup configuration file

When the application starts up, it will search for a `settings.json` file in the same directory. So, in order to use the application, rename `sample.settings.json` to`settings.json` and insert the following parameters:

##### Required parameters

| Parameter | Description                                                                | Example               |
|:---------:|:-------------------------------------------------------------------------- |:---------------------:|
| `broker`  | String value needed to connect to the MQTT broker [ *protocol://ip:port* ] | `tcp://hostname:1883` |
| `topic`   | Topic name to which the application must subscribe                         | `topicName`           |

##### Optional parameters

| Parameter             | Description                                                                                    | Default value                                  |
|:---------------------:|:---------------------------------------------------------------------------------------------- |:----------------------------------------------:|
| `clientId`            | MQTT client id for the application                                                             | `MqttNotifier`                                 |
| `appName`             | The text displayed as *tooltip* in the system tray and as *title* in any INFO or ERROR message | `Mqtt Notifier`                                |
| `notificationTitle`   | The notification title of any incoming message from the subscribed topic                       | `New Message`                                  |
| `notificationMessage` | The message notification of any incoming message from the subscribed topic                     | The received message's payload as string value |

Example:

```json
{
  "topic": "doorbell",
  "broker": "tcp://my-broker:1883",
  "clientId": "DoorbellNotifier",
  "appName": "Doorbell Notifier",
  "notificationTitle": "DOORBELL!",
  "notificationMessage": "dlin dlon"
}
```

## Adapt it for your purpose

If this application is too generic for your purpose, you can just do some little changes to the source code and then build it.

For example:

* In case of simple changes, you can modify the behaviour of  the `messaggeReceived` method in the `MqttNotifier` class

* If your project gets too complex according to the *observer pattern*, you can create another class that implements the `MqttNotification` interface
  
  ```java
  public class MyClass implements MqttNotification {
      ....
  
      @Override
      public void messageReceived(String topic, MqttMessage message) {
          // What you want do when a new message is received
      }
  }
  ```
  
  and connect it to `MqttSubClient` through the `attach()` method
  
  ```java
  MyClass myClass = new MyClass();
  mqttSubClient.attach(myClass);
  ```

        In this way your method will be called anytime there is a new messagge.

## Roadmap

In future releases I would like to:

* make it multi-platform

* add a feature to subscribe to multiple topics (configurable through the configuration file)

* enable login to broker with username and password

## Built With

* [Eclipse Paho Java Client](https://www.eclipse.org/paho/index.php?page=clients/java/index.php) - MQTT client library

* [Moshi](https://github.com/square/moshi) - JSON library

* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is released under the [MIT License](https://github.com/bongijo/MqttNotifier/blob/master/LICENSE)
