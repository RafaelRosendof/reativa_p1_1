package com.reativa.ms1.LlmPart;

import reactor.core.publisher.Mono;

public interface llmService {

    public Mono<String> getAnalisys();

    public Mono<String> getSummary();
}