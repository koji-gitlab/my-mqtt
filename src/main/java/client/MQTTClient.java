package client;

import com.google.gson.Gson;
import model.Item;
import model.MessageWrapper;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.Repository;

public class MQTTClient {

    private final String broker = "tcp://mosquitto:1883";
    private final String topic = "iot/data";
    private final String clientId = "JavaClient";
    private MqttClient client;
    private final Repository<Item> repository;
    private final Logger logger = LoggerFactory.getLogger(MQTTClient.class);

    public MQTTClient(Repository<Item> repository) {
        this.repository = repository;
    }

    public void connect() throws MqttException {
        client = new MqttClient(broker, clientId);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                logger.error("Connection lost!", cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String messageContent = new String(message.getPayload());
                logger.info("Received message: {}", messageContent);
                processMessage(messageContent);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Not needed for subscriber
            }
        });
        client.connect();
        client.subscribe(topic);
        logger.info("Connected to broker and subscribed to topic.");
    }

    void processMessage(String messageContent) {
        Gson gson = new Gson();
        MessageWrapper messageWrapper = gson.fromJson(messageContent, MessageWrapper.class);

        String operation = messageWrapper.getOperation();
        Item item = messageWrapper.getData();

        logger.info("Processing operation: {}", operation);

        switch (operation.toUpperCase()) {
            case "CREATE":
                repository.create(item);
                logger.info("Created item with ID: {}", item.getId());
                break;
            case "READ":
                Item result = repository.read(item.getId());
                logger.info("Read item: {}", result);
                break;
            case "UPDATE":
                repository.update(item);
                logger.info("Updated item with ID: {}", item.getId());
                break;
            case "DELETE":
                repository.delete(item.getId());
                logger.info("Deleted item with ID: {}", item.getId());
                break;
            default:
                logger.warn("Unknown operation: {}", operation);
        }
    }

}
