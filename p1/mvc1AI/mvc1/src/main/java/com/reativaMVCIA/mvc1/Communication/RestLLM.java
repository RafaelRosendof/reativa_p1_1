package com.reativaMVCIA.mvc1.Communication;

import com.reativaMVCIA.mvc1.OpenAIReqs.ChatService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

// REVIEW THIS

@RestController
@RequestMapping("/api") 
public class RestLLM {
    
    private final ChatService chatService;

    @Value("${spring.ai.openai.model}")
    private String model;

    @Autowired
    public RestLLM(ChatService chatService) {
        this.chatService = chatService;
    }

    

    @GetMapping("/isAlive")
    public ResponseEntity<Map<String , String>> isAliveGet(){
        System.out.println("Received isAlive GET request");

        return ResponseEntity.ok(Map.of(
            "status", "alive",
            "message", "LLM Service is running!",
            "timestamp", java.time.Instant.now().toString()
        ));
    }

    @PostMapping("/isAlive")
    public ResponseEntity<String> isAlive() {
        System.out.println("Received isAlive request");
        return ResponseEntity.ok("LLM Service is alive!");
    }
}