package com.linktracker.repository;

import com.linktracker.dto.Link;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LinkRepository {
    @Getter
    List<Link> links = new ArrayList<>();

    public void saveLink(Link link) {
        if (links.contains(link)) {
            return;
        }
        Link link_with_id = new Link((long)links.size(), link.url(), OffsetDateTime.now(), OffsetDateTime.now());
        links.add(link);
    }

    public void saveLinkByString(String url) {
        Link link_with_id = new Link((long)links.size(), url, OffsetDateTime.now(), OffsetDateTime.now());
        if (links.contains(link_with_id)) {
            return;
        }
        links.add(link_with_id);
    }

    public boolean deleteLinkByString(String url) {
        return links.removeIf(link -> link.url().equals(url));
    }

    public Link getLinkById(long id) {
        int index = (int) id;
        if (index < 0 || index >= links.size()) return null;
        return links.get(index);
    }

    public Link getLinkByUrl(String url) {
        return links.stream().filter(n -> n.url().equals(url)).findFirst().orElse(null);
    }

    public void updateLastCheck(String url, OffsetDateTime date) {
        long id = getLinkByUrl(url).id();
        Link link = links.get((int) id);
        links.set((int) id, new Link(link.id(), link.url(), date, link.updatedAt()));
    }

    public void updateUpdatedAt(String url, OffsetDateTime date) {
        long id = getLinkByUrl(url).id();
        Link link = links.get((int) id);
        links.set((int) id, new Link(link.id(), link.url(), link.lastCheckedAt(), date));
    }


}
