package com.linktracker.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
public class TGChatClient {
    @Autowired
    private WebClient webClient;

    public void postRegisterChatById(long chatId) {
        webClient.post()
                .uri("/tg-chat/{chatId}", chatId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public boolean deleteChatById(long chatId) {
        try {
            ResponseEntity<Void> response = webClient.delete()
                    .uri("/tg-chat/{chatId}", chatId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return response.getStatusCode().is2xxSuccessful();
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (WebClientResponseException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
