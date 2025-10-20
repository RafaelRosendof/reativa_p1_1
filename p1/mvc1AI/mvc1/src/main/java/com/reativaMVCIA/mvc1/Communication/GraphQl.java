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
        //return chatService.sendChatWithPrompt(apiKey, model);
        return "This method is deprecated. Use sendChatWithPrompt with prompt parameter instead.";
    }

    // create the method getAnalysis
    // now we gonna teste it
    @QueryMapping
    public String getAnalysis(){
        System.out.println("\n\nReceived request for LLM analysis.\n\n");
        return chatService.getAnalysis();
    }

    @QueryMapping
    public String isAlive() {
        return "MVC1IA is alive! viva figas \n\n\n\n";
    }


}
