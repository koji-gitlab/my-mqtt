# My MQTT Project

A simple setup for running an MQTT broker using Mosquitto and publishing messages to different topics. This project demonstrates how to use Docker with MQTT.

## Prerequisites

- [Docker](https://www.docker.com/products/docker-desktop)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Installation

1. **Clone the repository:**

    ```sh
    git clone https://github.com/koji-gitlab/my-mqtt.git
    cd my-mqtt
    ```

## Running the MQTT Broker

### Using Docker

1. **Run the Mosquitto container:**

    ```sh
    docker run -d --name mosquitto --network mqtt_network -p 1883:1883 eclipse-mosquitto
    ```

### Using Docker Compose

1. **Create a `docker-compose.yml` file (if it doesn't exist):**

    ```yaml
    version: '3.8'

    services:
      mosquitto:
        image: eclipse-mosquitto
        ports:
          - "1883:1883"
        networks:
          - mqtt_network
        
    networks:
      mqtt_network:
        driver: bridge
    ```

2. **Run Docker Compose:**

    ```sh
    docker-compose up -d
    ```

## Publishing Messages

### From Another Docker Container

1. **Publish a message to the topic:**

    ```sh
    docker run --rm --network mqtt_network eclipse-mosquitto mosquitto_pub -h mosquitto -t "iot/data" -m '{"id":1,"name":"Sensor1","value":45.67}'
    ```

2. **Subscribe to the topic in another container:**

    ```sh
    docker run --rm --network mqtt_network eclipse-mosquitto mosquitto_sub -h mosquitto -t "iot/data"
    ```

### From the Host Machine

1. **Install Mosquitto clients:**

   On macOS:
    ```sh
    brew install mosquitto
    ```

   On Linux (Debian-based):
    ```sh
    sudo apt-get install mosquitto-clients
    ```

2. **Publish a message from the host:**

    ```sh
    mosquitto_pub -h localhost -t "iot/data" -m '{"id":1,"name":"Sensor1","value":45.67}'
    ```

3. **Subscribe to the topic from the host:**

    ```sh
    mosquitto_sub -h localhost -t "iot/data"
    ```

## Usage Examples

### Java Client Example

#### Publish a Message using Java

```java
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPublisher {
    public static void main(String[] args) {
        String broker = "tcp://mosquitto:1883";
        String topic = "iot/data";
        String content = "{\"id\":1,\"name\":\"Sensor1\",\"value\":45.67}";
        int qos = 2;

        try {
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
            client.connect();
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(topic, message);
            client.disconnect();
            System.out.println("Message published");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
```

#### Subscribe to a Topic using Java

```java
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSubscriber {
    public static void main(String[] args) {
        String broker = "tcp://mosquitto:1883";
        String topic = "iot/data";

        try {
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Received message: " + new String(message.getPayload()) + " on topic " + topic);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used for subscribing
                }
            });
            client.connect();
            client.subscribe(topic);
            System.out.println("Subscribed to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
```

## Contributing

1. Fork the repository
2. Create a new branch (`git checkout -b feature-branch`)
3. Make your changes
4. Commit your changes (`git commit -am 'Add new feature'`)
5. Push to the branch (`git push origin feature-branch`)
6. Create a new Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
