package com.linktracker.updater;

import com.linktracker.client.external.api.DefaultClient;
import com.linktracker.dto.Link;
import com.linktracker.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultUpdater implements LinkUpdater {
    private final DefaultClient client;
    private final LinkRepository linkRepository;

    @Override
    public String getUrlDefault() {
        return "default";
    }

    @Override
    public Optional<String> check(Link link) {
        OffsetDateTime checkTime = OffsetDateTime.now();

        Optional<OffsetDateTime> lastModified = client.getLastUpdate(link.url());

        linkRepository.updateLastCheck(link.url(), checkTime);

        return lastModified
                .filter(date -> link.updatedAt() == null || date.isAfter(link.updatedAt()))
                .map(date -> {
                    linkRepository.updateUpdatedAt(link.url(), date);
                    return "Обновление по ссылке: " + link.url() + "\n" +
                            "Дата изменения: " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                });
    }
}
