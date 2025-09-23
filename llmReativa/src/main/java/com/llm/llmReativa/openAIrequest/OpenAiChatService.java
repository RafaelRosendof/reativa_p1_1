package main.java.com.llm.llmReativa.openAIrequest;

import org.springframework.stereotype.Service;

// relevancy evaluator what is and we need to use? // better no, gonna be more expensive 
// what is @Primary ?

// @Value("classpath:prompt/promptSystem.st") we not gonna use the format exit

// need a better function to receive the response

@Service 
public class OpenAiChatService implements ChatService {

    private final ChatClient chatClient;
    private final PromptService promptService;

    // needs the @Autowired ? or @Bean
    public OpenAiChatService(ChatClient chatClient, PromptService promptService) {
        this.chatClient = chatClient;
        this.promptService = promptService;
    }

    @Override
    public String getPrompt(java.util.List<String> topics, java.util.List<Double> stocks) {
        return promptService.gerarPrompt(topics, stocks);
    }

    // need to have a funcition to pass api key and the prompt and model
    @Override
    public String sendChatWithPrompt(String prompt , String apiKey, String model , ChatClient.Builder chatBuilder) {

        ChatOptions options = ChatOptions.builder()
                .model(model)
                .build();

        this.chatClient = chatBuilder.defaultOptions(options)
                .apiKey(apiKey)
                .build();
    }

    @Override
    public String getChatResponse(String prompt) {
        return chatClient.prompt().user(prompt).call().content();
    }

}