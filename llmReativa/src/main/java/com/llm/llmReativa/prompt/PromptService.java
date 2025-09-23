package main.java.com.llm.llmReativa.prompt;

import java.util.List;

public class PromptService {


    public PromptService() {

    }

    public String gerarPrompt(List<String> topics , List<Double> stocks ) {
        StringBuilder prompt = new StringBuilder("""

        Here it's gonna be a english prompt for analyzing stock market data. based on the following topics and stock prices:
        Topics:
        The news about the last week of the top 3 most searchs stocks in our system.
        Stocks:
        the price of open and close of the last 7 days of the top 3 most searchs stocks in our system.

        """);
        for (int i = 0; i < topics.size(); i++) {
            prompt.append("Topic ").append(i + 1).append(": ").append(topics.get(i)).append("\n");
        }
        for (int i = 0; i < stocks.size(); i++) {
            prompt.append("Stock ").append(i + 1).append(": ").append(stocks.get(i)).append("\n");
        }
        return prompt.toString();
    }
}