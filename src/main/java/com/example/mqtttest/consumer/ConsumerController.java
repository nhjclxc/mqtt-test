package com.example.mqtttest.consumer;

import com.example.mqtttest.config.MqttConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private MqttConsumerConfig client;

//    @Value("${spring.mqtt.client.id}")
//    private String clientId;

    @RequestMapping("/connect")
    @ResponseBody
    public String connect(){
        client.connect();
        return client.clientId + "连接到服务器";
    }

    @RequestMapping("/disConnect")
    @ResponseBody
    public String disConnect(){
        client.disConnect();
        return client.clientId + "与服务器断开连接";
    }
}