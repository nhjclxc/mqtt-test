package com.example.mqtttest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuoXianchao
 * @since 2024/03/08 13:57
 */

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping
    public String test(){
        System.out.println("Hello Hello");

        return "Hello";
    }
}
