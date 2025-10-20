package com.reativaMVCIA.mvc1.OpenAIReqs;

public interface ChatService {
    // quem sabe com List<>
    String getPrompt();
    String getPromptQuestion(String chatQuestion);
    public String sendChatWithPrompt(String prompt , String apiKey, String model);

    public String sendGraphQLToDb();

    public String getAnalysis();
}