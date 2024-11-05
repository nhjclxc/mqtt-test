package com.example.mqtttest.producer;

import com.example.mqtttest.config.MqttProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pro")
public class ProducerController {
    @Autowired
    private MqttProviderConfig providerClient;

    /**
     *
     * @param qos
     * • 支持三种消息发布服务质量（QoS）：
     * • QoS 0（最多一次）：消息发布完全依赖底层 TCP/IP 网络。会发生消息丢失或重复。这个级别可用于如下情况，环境传感器数据，丢失一次数据无所谓，因为不久后还会有第二次发送。
     * • QoS 1（至少一次）：确保消息到达，但消息重复可能会发生。
     * • QoS 2（只有一次）：确保消息到达一次。这个级别可用于如下情况，在计费系统中，消息重复或丢失会导致不正确的结果。
     *
     */
    @RequestMapping("/sendMessage")
    @ResponseBody
    public String sendMessage(int qos, boolean retained, String topic, String message) {
        try {
            providerClient.publish(qos, retained, topic, message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送失败";
        }
    }
}
