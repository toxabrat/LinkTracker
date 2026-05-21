package com.linktracker.service;

import com.linktracker.client.UpdateByLinksClient;
import com.linktracker.dto.Chat;
import com.linktracker.dto.Link;
import com.linktracker.dto.request.LinkUpdateRequest;
import com.linktracker.repository.ChatLinksRepository;
import com.linktracker.repository.LinkRepository;
import com.linktracker.updater.DefaultUpdater;
import com.linktracker.updater.LinkUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkUpdaterService {
    private final LinkRepository linkRepository;
    private final List<LinkUpdater> linkUpdaters;
    private final DefaultUpdater defaultUpdater;
    private final UpdateByLinksClient updateByLinksClient;
    private final ChatLinksRepository chatLinksRepository;

    private void sendUpdate(Link link, String message) {
        List<Long> chatIds = chatLinksRepository
                .getChatIdByLink(link.url())
                .stream()
                .map(Chat::id)
                .toList();

        updateByLinksClient.postUpdates(new LinkUpdateRequest(
                link.id(),
                link.url(),
                message,
                chatIds
        ));
    }

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void checkUpdates() {
        for (Link link : linkRepository.getLinks()) {
            boolean isCheck = false;
            for (LinkUpdater linkUpdater : linkUpdaters) {
                if (link.url().contains(linkUpdater.getUrlDefault())) {
                    Optional<String> result = linkUpdater.check(link);
                    if (result.isPresent()) {
                        sendUpdate(link, result.get());
                        isCheck = true;
                        break;
                    }
                }
            }
            if (!isCheck) {
                Optional<String> result = defaultUpdater.check(link);
                result.ifPresent(string -> sendUpdate(link, string));
            }
        }
    }
}
