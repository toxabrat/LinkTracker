package com.linktracker.client.external.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Component
public class DefaultClient {
    WebClient webClient;

    public DefaultClient() {
        webClient = WebClient.builder().build();
    }

    public Optional<OffsetDateTime> getLastUpdate(String url) {
        try {
            ClientResponse clientResponse = webClient.get()
                    .uri(url)
                    .exchangeToMono(Mono::just)
                    .block();
            if (clientResponse == null) {
                return Optional.empty();
            }

            String lastModified = clientResponse.headers()
                    .asHttpHeaders()
                    .getFirst("Last-Modified");

            if (lastModified != null) {
                OffsetDateTime lastModifiedDate = OffsetDateTime.parse(
                        lastModified,
                        DateTimeFormatter.RFC_1123_DATE_TIME);
                return Optional.of(lastModifiedDate);
            }

            return Optional.empty();
        } catch (Exception e) {
            log.warn("Не удалось проверить ссылку {}: {}", url ,e.getMessage());
            return Optional.empty();
        }
    }
}
