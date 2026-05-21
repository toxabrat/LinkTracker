package com.linktracker.updater;

import com.linktracker.client.external.api.StackOverflowClient;
import com.linktracker.dto.Link;
import com.linktracker.dto.external.api.StackOverflowResponse;
import com.linktracker.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StackOverflowUpdater implements LinkUpdater {
    private final StackOverflowClient client;
    private final LinkRepository linkRepository;

    @Override
    public String getUrlDefault() {
        return "https://stackoverflow.com";
    }

    private String convertDate(Long unixTimestamp) {
        return OffsetDateTime
                .ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    @Override
    public Optional<String> check(Link link) {
        String questionId = link.url().split("/")[4];
        OffsetDateTime date = link.lastCheckedAt();
        List<StackOverflowResponse> responses = new ArrayList<>();
        OffsetDateTime checkTime = OffsetDateTime.now();
        responses = client.getAnswer(questionId, date);
        linkRepository.updateLastCheck(link.url(), checkTime);
        StringBuilder answer = new StringBuilder();
        if (!responses.isEmpty()) {
            linkRepository.updateUpdatedAt(link.url(), checkTime);
            answer.append(link.url()).append("\n");
            answer.append("было получено ")
                    .append(responses.size())
                    .append(" новых сообщений\n");
            responses.forEach(answer_elem -> {
                String update = """
                    Новый ответ на вопрос: %s
                    Автор: %s
                    Дата: %s
                    %s
                    """.formatted(
                            answer_elem.title(),
                        answer_elem.owner().displayName(),
                        convertDate(answer_elem.creationDate()),
                        answer_elem.body().substring(0, Math.min(200, answer_elem.body().length()))
                );
                answer.append(update);
                answer.append("\n\n");
            });

            return Optional.of(answer.toString());
        }
        return Optional.empty();
    }
}
