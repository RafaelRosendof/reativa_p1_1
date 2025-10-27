package com.ms1Service.ms1.Communication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    @PostMapping("/test")
    public String testEndpoint(@RequestBody String body) {
        System.out.println("Received body: " + body);
        return "MS1 received: " + body;
    }
}
