import client.MQTTClient;
import model.Item;
import org.eclipse.paho.client.mqttv3.MqttException;
import repository.InMemoryRepository;
import repository.Repository;

public class Main {

    public static void main(String[] args) {
        Repository<Item> repository = new InMemoryRepository();
        MQTTClient mqttClient = new MQTTClient(repository);
        try {
            mqttClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
