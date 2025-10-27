package com.reativaMVCIA.mvc1.OpenAIReqs;

import java.util.List;
import lombok.Data;

@Data
public class OpenAIChatReq {
    private String model;
    private List<OpenAIMessage> messages;

    public OpenAIChatReq(String model, List<OpenAIMessage> messages) {
        this.model = model;
        this.messages = messages;
    }
}
