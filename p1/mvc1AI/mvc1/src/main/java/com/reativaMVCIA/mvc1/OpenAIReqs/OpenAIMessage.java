package com.reativaMVCIA.mvc1.OpenAIReqs;

import lombok.Data;

@Data
public class OpenAIMessage {
    private String role;
    private String content;

    public OpenAIMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
