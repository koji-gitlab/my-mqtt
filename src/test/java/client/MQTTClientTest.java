package client;

import model.Item;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.Repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MQTTClientTest {

    @Mock
    private Repository<Item> repository;

    private MQTTClient mqttClient;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mqttClient = new MQTTClient(repository);
    }

    @Test
    public void testCreateOperation() {
        Item item = new Item();
        item.setId(1);
        item.setName("Sensor1");
        item.setValue(45.67);

        String payload = "{\"operation\":\"CREATE\",\"data\":{\"id\":1,\"name\":\"Sensor1\",\"value\":45.67}}";

        MqttMessage message = new MqttMessageBuilder().setPayload(payload).build();

        // Simulate message arrival
        mqttClient.processMessage(new String(message.getPayload()));

        // Verify that the repository's create method was called with the correct item
        verify(repository, times(1)).create(item);
    }

    @Test
    public void testReadOperation() {
        Item item = new Item();
        item.setId(1);
        item.setName("Sensor1");
        item.setValue(45.67);

        when(repository.read(1)).thenReturn(item);

        String payload = "{\"operation\":\"READ\",\"data\":{\"id\":1,\"name\":\"\",\"value\":0}}";

        MqttMessage message = new MqttMessageBuilder().setPayload(payload).build();

        // Simulate message arrival
        mqttClient.processMessage(new String(message.getPayload()));

        // Verify that the repository's read method was called with the correct id
        verify(repository, times(1)).read(1);
    }

    @Test
    public void testUpdateOperation() {
        Item item = new Item();
        item.setId(1);
        item.setName("SensorUpdated");
        item.setValue(46.00);

        String payload = "{\"operation\":\"UPDATE\",\"data\":{\"id\":1,\"name\":\"SensorUpdated\",\"value\":46.00}}";

        MqttMessage message = new MqttMessageBuilder().setPayload(payload).build();

        // Simulate message arrival
        mqttClient.processMessage(new String(message.getPayload()));

        // Capture the argument passed to the update method
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(repository, times(1)).update(itemCaptor.capture());
        Item capturedItem = itemCaptor.getValue();

        // Assert that the captured item is as expected
        assertEquals(item, capturedItem);
    }

    @Test
    public void testDeleteOperation() {
        String payload = "{\"operation\":\"DELETE\",\"data\":{\"id\":1,\"name\":\"\",\"value\":0}}";

        MqttMessage message = new MqttMessageBuilder().setPayload(payload).build();

        // Simulate message arrival
        mqttClient.processMessage(new String(message.getPayload()));

        // Verify that the repository's delete method was called with the correct id
        verify(repository, times(1)).delete(1);
    }

    @Test
    public void testUnknownOperation() {
        String payload = "{\"operation\":\"UNKNOWN\",\"data\":{\"id\":1,\"name\":\"Sensor1\",\"value\":45.67}}";

        MqttMessage message = new MqttMessageBuilder().setPayload(payload).build();

        // Simulate message arrival
        mqttClient.processMessage(new String(message.getPayload()));

        // Unknown operation should not call any repository methods
        verifyNoInteractions(repository);
    }

}
