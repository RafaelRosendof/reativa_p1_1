package com.reativaMVCIA.mvc1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.ai.openai.api-key=sk-test-key",
    "spring.ai.openai.model=gpt-4o-mini",
    "eureka.client.enabled=false",
    "spring.cloud.discovery.enabled=false"
})
class Mvc1IaApplicationTests {

    @Test
    void contextLoads() {
    }

}