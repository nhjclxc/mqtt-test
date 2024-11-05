package com.example.mqtttest.consumer;

import com.example.mqtttest.config.MqttProviderConfig;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttConsumerCallBack implements MqttCallback{

    @Autowired
    private MqttProviderConfig providerClient;

    /**
     * 客户端断开连接的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Consumer 与服务器断开连接，可重连");
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(String.format("Consumer 接收消息主题 : %s",topic));
        System.out.println(String.format("Consumer 接收消息Qos : %d",message.getQos()));
        System.out.println(String.format("Consumer 接收消息内容 : %s",new String(message.getPayload())));
        System.out.println(String.format("Consumer 接收消息retained : %b",message.isRetained()));


        providerClient.publish(1, true, topic, "new String(message.getPayload())");
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println(String.format("Consumer 接收消息成功"));
    }
}
