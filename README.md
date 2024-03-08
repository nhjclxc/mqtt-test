# mqtt-test

SpringBoot环境下实现mqtt物联网开发

# MQTT

https://www.emqx.io/zh

https://space.bilibili.com/522222081

https://www.emqx.com/zh/blog/the-easiest-guide-to-getting-started-with-mqtt

https://www.bilibili.com/video/BV1o5411E7V1

客户端MQTTX: https://mqttx.app/zh/downloads

## MQTT日常使用中最常见的几个概念：
1. Topic主题：MQTT消息的主要传播途径, 我们向主题发布消息, 订阅主题, 从主题中读取消息并进行.业务逻辑处理, 主题是消息的通道 
2. 生产者：MQTT消息的发送者, 他们向主题发送消息 
3. 消费者：MQTT消息的接收者, 他们订阅自己需要的主题, 并从中获取消息 
4. broker服务：消息转发器, 消息是通过它来承载的, EMQX就是我们的broker, 在使用中我们不用关心它的具体实现其实, MQTT的使用流程就是: 生产者给broker的某个topic发消息->broker通过topic进行消息的传递->订阅该主题的消费者拿到消息并进行相应的业务逻辑


## 支持三种消息发布服务质量（QoS）, Qos时客户端与服务器之间的消息保证：
- QoS 0（最多一次）：消息发布完全依赖底层 TCP/IP 网络。会发生消息丢失或重复。这个级别可用于如下情况，环境传感器数据，丢失一次数据无所谓，因为不久后还会有第二次发送。
- QoS 1（至少一次）：确保消息到达，但消息重复可能会发生。
- QoS 2（只有一次）：确保消息到达一次。这个级别可用于如下情况，在计费系统中，消息重复或丢失会导致不正确的结果。


## 主题
- 发布消息时,必须发布指定主题,而订阅消息时可以使用通配符来订阅多个主题
  '+':表示通配一个层级，例如a/+，匹配a/x, a/y
  '#':表示通配多个层级，例如a/#，匹配a/x, a/b/ c/d
  注:‘+’通配一个层级，'#’通配多个层级(必须在末尾)。



#  EMQX
## 

