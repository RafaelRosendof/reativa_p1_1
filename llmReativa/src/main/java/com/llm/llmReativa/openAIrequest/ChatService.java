//package main.java.com.llm.llmReativa.openAIrequest;

package com.llm.llmReativa.openAIrequest;

import java.util.List;

public interface ChatService {
    String getPrompt(List<String> topics, List<Double> stocks);
    String getPromptQuestion(String chatQuestion);
    public String sendChatWithPrompt(String prompt , String apiKey, String model);
    //String getChatResponse(String prompt);

}