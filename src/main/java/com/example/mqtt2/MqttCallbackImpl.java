package com.example.mqtt2;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 主要用来接收和处理订阅主题的消息
 */
public class MqttCallbackImpl implements MqttCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttCallbackImpl.class);

    private final MqttServer mqttServer;

    public MqttCallbackImpl(MqttServer mqttServer) {
        this.mqttServer = mqttServer;
    }


    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        LOGGER.info("---------------------连接断开，可以做重连");
//        mqttServer.subsribeConnect();

//        while (true){
//            try {
//                //如果没有发生异常说明连接成功，如果发生异常，则死循环
//                Thread.sleep(1000);
//                break;
//            }catch (Exception e){
//                continue;
//            }
//        }

    }

    /**
     * 发送消息，消息到达后处理方法
     * @param token
     */
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

    /**
     * 接收所订阅的主题的消息并处理
     * @param topic
     * @param message
     */
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        // 监听指定topic
        if (MqttServer.defaultTopicList.contains(topic)) {
            // subscribe后得到的消息会执行到这里面
            String result = new String(message.getPayload(), StandardCharsets.UTF_8);
            System.out.println("接收消息主题 : " + topic);
            System.out.println("接收消息Qos : " + message.getQos());
            System.out.println("接收消息内容 : " + result);
            //这里可以针对收到的消息做处理，比如持久化

            //逻辑判断
            mqttServer.receiveMsgCallback(topic, result);
        }

//        String msg = "msg - " + System.currentTimeMillis();
//        mqttServer.sendMQTTMessage(topic, msg, message.getQos());
//        String data = "messageArrived. 发送了一条主题是‘"+topic+"’，内容是:"+msg+"，消息级别 "+message.getQos();
//        System.out.println(msg);
    }

}
