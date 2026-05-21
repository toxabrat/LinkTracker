package com.linktracker.client;

import com.linktracker.dto.request.AddLinkRequest;
import com.linktracker.dto.response.LinkResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class LinkClient {
    @Autowired
    private WebClient webClient;

    public List<LinkResponse> getLinks(long chatId) {
        return webClient.get()
                .uri("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .retrieve()
                .bodyToFlux(LinkResponse.class)
                .collectList()
                .block();
    }

    public boolean postLink(long chatId, AddLinkRequest addLinkRequest) {
        try {
            ResponseEntity<Void> response = webClient.post()
                    .uri("/links")
                    .header("Tg-Chat-Id", String.valueOf(chatId))
                    .body(Mono.just(addLinkRequest), AddLinkRequest.class)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return response.getStatusCode().is2xxSuccessful();
        } catch (WebClientResponseException.BadRequest e) {
            return false;
        } catch (WebClientResponseException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean deleteLink(long chatId, String url) {
        try {
            ResponseEntity<Void> response = webClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/links")
                            .queryParam("url", url)
                            .build())
                    .header("Tg-Chat-Id", String.valueOf(chatId))
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
