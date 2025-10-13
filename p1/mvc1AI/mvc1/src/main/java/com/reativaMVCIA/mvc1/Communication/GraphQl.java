package com.reativaMVCIA.mvc1.Communication;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.reativaMVCIA.mvc1.OpenAIReqs.ChatService;

@Controller
public class GraphQl {

    private final ChatService chatService;

    public GraphQl(ChatService chatService) {
        this.chatService = chatService;
    }

    @QueryMapping
    public String ChatWithGPT(String apiKey , String model){
        return chatService.sendChatWithPrompt(apiKey, model);
    }

}
