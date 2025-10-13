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

    @PostMapping("/chat")
    public ResponseEntity<Map<String,String>> chatWithLlm( @RequestBody String chatQuestion , @RequestHeader("Authorization") String apiKey ) {
        
        try{

            if(apiKey.startsWith("Bearer ")){
                apiKey = apiKey.substring(7);
            }

            //review this part 

            //String prompt = chatService.getPrompt(chatQuestion.getTopics(), chatQuestion.getStocks());
            String prompt = chatService.getPromptQuestion(chatQuestion);
            System.out.println("Using prompt: " + prompt);
            String response = chatService.sendChatWithPrompt(apiKey, model);
            return ResponseEntity.ok(Map.of("analysis", response));

        }catch(Exception e){
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to analyze stocks: " + e.getMessage()));
        }

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