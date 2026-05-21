package com.linktracker.client;

import com.linktracker.dto.request.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UpdateByLinksClient {
    private final WebClient webClient;

    public UpdateByLinksClient(@Value("${BOT_API_URL}") String botApiUrl) {
        webClient = WebClient.builder()
                .baseUrl(botApiUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public void postUpdates(LinkUpdateRequest linkUpdateRequest) {
        webClient.post()
                .uri("/updates")
                .bodyValue(linkUpdateRequest)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
