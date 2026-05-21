package com.linktracker.client.external.api;

import com.linktracker.dto.external.api.StackOverflowResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class StackOverflowClient {
    private WebClient webClient;

    public StackOverflowClient() {
        webClient = WebClient.builder()
                .baseUrl("https://api.stackexchange.com/2.3")
                .build();
    }

    public List<StackOverflowResponse> getAnswer(
            String questionId,
            OffsetDateTime time
    ) {
        long since = time.toEpochSecond();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/questions/{id}/answers")
                        .queryParam("site", "stackoverflow")
                        .queryParam("filter", "withbody")
                        .queryParam("fromdate", since)
                        .build(questionId))
                .retrieve()
                .bodyToFlux(StackOverflowResponse.class)
                .collectList()
                .block();
    }
}
