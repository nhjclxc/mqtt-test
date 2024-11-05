package com.example.mqtt2;

import com.example.mqtt2.config.MqttConfig;
import com.example.mqtt2.config.MqttConnect;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * 发布端：主要实现发布消息和订阅主题，接收消息在回调类PushCallback中
 * 要发布消息的时候只需要调用sendMQTTMessage方法就OK了
 * <a href="https://blog.csdn.net/f4233441/article/details/121829743">参考</a>
 */
@Service
public class MqttServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttServer.class);

    /**
     * 发布者客户端对象
     * 这里订阅者和发布者的MqttClient对象分别命名是为了让发布者和订阅者分开，
     * 如果订阅者和发布者都用一个MqttClient链接对象，则会出现两方都订阅了某个主题后，
     * 谁发送了消息，都会自己接收到自己发的消息，所以分开写，里面主要就是回调类的设置setCallback
     * */
    private MqttClient publishClient;

    /* 订阅者客户端对象 */
    private MqttClient subsribeClient;


    @Autowired
    private MqttConnect mqttConnect;


    @Autowired
    private MqttConfig config;

    @Value("${spring.profiles.active}")
    private String active;

    public static final List<String> defaultTopicList = Arrays.asList("up", "open");


    // 发布者、订阅者分别连接MQTT服务器
    // 订阅者订阅主题

    /**
     * 获取客户端id
     */
    public String getClientId(String client){
        String osName = System.getProperty("os.name").toLowerCase();
        String clientId = client+"-" + active;
        if (osName.contains("win")) {
            clientId += "-win";
        }
        return clientId;
    }


    /**
     * 项目启动时，初始化参数到缓存
     *      * @param qos
     *      * • 支持三种消息发布服务质量（QoS）：
     *      * • QoS 0（最多一次）：消息发布完全依赖底层 TCP/IP 网络。会发生消息丢失或重复。这个级别可用于如下情况，环境传感器数据，丢失一次数据无所谓，因为不久后还会有第二次发送。
     *      * • QoS 1（至少一次）：确保消息到达，但消息重复可能会发生。
     *      * • QoS 2（只有一次）：确保消息到达一次。这个级别可用于如下情况，在计费系统中，消息重复或丢失会导致不正确的结果。
     *      *
     */
    @PostConstruct
    public void init() {
        //建立连接
        publishConnect(); // 发布者连接服务端
        subsribeConnect(); // 订阅者连接客户端

        // 订阅者以某个消息级别订阅某个主题
        try {
            for (String topic : defaultTopicList) {
                this.subsribeClient.subscribe(topic, 2);
                LOGGER.info("MQTT消息订阅Topic：{}", topic);
            }
        } catch (MqttException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    /**
     * 发布者客户端和服务端建立连接
     */
    private void publishConnect() {
        //防止重复创建MQTTClient实例
        try {
            //先让客户端和服务器建立连接，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            this.publishClient = new MqttClient(config.getUrl(), getClientId("publish"), new MemoryPersistence());
            //发布消息不需要回调连接
            //client.setCallback(new PushCallback());

            MqttConnectOptions options = this.mqttConnect.getOptions();
            //判断拦截状态，这里注意一下，如果没有这个判断，是非常坑的
            if (!this.publishClient.isConnected()) {
                this.publishClient.connect(options);
            }else {//这里的逻辑是如果连接成功就重新连接
                this.publishClient.disconnect();
                this.publishClient.connect(this.mqttConnect.getOptions(options));
            }
            LOGGER.info("---------------------发布者连接成功---------------------");
        } catch (MqttException e) {
            LOGGER.info(e.toString());
        }
    }

    /**
     * 订阅端的链接方法，关键是回调类的设置，要对订阅的主题消息进行处理
     * 断线重连方法，如果是持久订阅，重连时不需要再次订阅
     * 如果是非持久订阅，重连是需要重新订阅主题 取决于options.setCleanSession(true);
     * true为非持久订阅
     */
    private void subsribeConnect() {
        try {
            //clientId不能和其它的clientId一样，否则会出现频繁断开连接和重连的问题
            subsribeClient = new MqttClient(config.getUrl(), getClientId("subsribe"), new MemoryPersistence());// MemoryPersistence设置clientid的保存形式，默认为以内存保存
            //如果是订阅者则添加回调类，发布不需要，PushCallback类在后面，继续往下看
            subsribeClient.setCallback(new MqttCallbackImpl(MqttServer.this));

            MqttConnectOptions options = mqttConnect.getOptions();
            //判断拦截状态，这里注意一下，如果没有这个判断，是非常坑的
            if (!subsribeClient.isConnected()) {
                subsribeClient.connect(options);
            }else {//这里的逻辑是如果连接成功就重新连接
                subsribeClient.disconnect();
                subsribeClient.connect(mqttConnect.getOptions(options));
            }
            LOGGER.info("------------------ 订阅连接成功 ------------------");
        } catch (MqttException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    /**
     * 把组装好的消息发出去
     * @param topic 主题
     * @param message 消息体
     * @return 消息发布成功与否
     */
    private boolean publish(MqttTopic topic , MqttMessage message) {
        try {
            //把消息发送给对应的主题
            MqttDeliveryToken token = topic.publish(message);
            token.waitForCompletion();
            //检查发送是否成功
            boolean flag = token.isComplete();

            StringBuilder sbf = new StringBuilder(200);
            sbf.append("给主题为'").append(topic.getName());
            sbf.append("'发布消息：");
            if (flag) {
                sbf.append("成功！消息内容是：").append(new String(message.getPayload()));
            } else {
                sbf.append("失败！");
            }
            LOGGER.info(sbf.toString());
            return token.isComplete();
        } catch (MqttException e) {
            LOGGER.info(e.toString());
        }
        return false;
    }

    /**
     * MQTT发送指令：主要是组装消息体
     * @param topic 主题
     * @param data 消息内容
     * @param qos 消息级别
     */
    public boolean sendMQTTMessage(String topic, String data, int qos) {
        try {
            MqttTopic mqttTopic = this.publishClient.getTopic(topic);
            MqttMessage mqttMessage = new MqttMessage();
            //消息等级
            //level 0：消息最多传递一次，不再关心它有没有发送到对方，也不设置任何重发机制
            //level 1：包含了简单的重发机制，发送消息之后等待接收者的回复，如果没收到回复则重新发送消息。这种模式能保证消息至少能到达一次，但无法保证消息重复
            //level 2： 有了重发和重复消息发现机制，保证消息到达对方并且严格只到达一次
            mqttMessage.setQos(qos);
            //如果重复消费，则把值改为true,然后发送一条空的消息，之前的消息就会覆盖，然后在改为false
            mqttMessage.setRetained(false);

            mqttMessage.setPayload(data.getBytes());

            //将组装好的消息发出去
            return publish(mqttTopic, mqttMessage);
        } catch (Exception e) {
            LOGGER.info(e.toString());
            e.printStackTrace();
        }
        return false;
    }
    public boolean sendMQTTMessage(String topic, String data) {
        return sendMQTTMessage(topic, data, 2);
    }

    /**
     * 订阅端取消订阅消息
     * @param topic 要订阅的主题
     */
    public void unionInit(String topic) {
        //取消订阅某个主题
        try {
            //MQTT 协议中订阅关系是持久化的，因此如果不需要订阅某些 Topic，需要调用 unsubscribe 方法取消订阅关系。
            subsribeClient.unsubscribe(topic);
        } catch (MqttException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }


    public void receiveMsgCallback(String topic, String result) {

        // 业务逻辑处理
        System.out.println("业务逻辑处理：topic = " + topic + ", result = " + result);

        if ("up".equals(topic)) {
            // up主题逻辑

            // 给open主题发一条消息
            sendMQTTMessage("open", "接收到你的消息了，好开心！！！" + System.currentTimeMillis());

        } else if ("open".equals(topic)) {
            // open主题逻辑




        }

    }

}
