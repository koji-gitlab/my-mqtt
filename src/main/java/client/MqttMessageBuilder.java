package client;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttMessageBuilder {

    private byte[] payload;

    public MqttMessageBuilder setPayload(String payload) {
        this.payload = payload.getBytes();
        return this;
    }

    public MqttMessage build() {
        return new MqttMessage(payload);
    }

}
