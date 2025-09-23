package main.java.com.llm.llmReativa.openAIrequest;

import java.util.List;

public interface ChatService {
    String getPrompt(List<String> topics, List<Double> stocks);
    public String sendChatWithPrompt(String prompt , String apiKey, String model , ChatClient.Builder chatBuilder);
    String getChatResponse(String prompt);

}