package com.linktracker.repository;

import com.linktracker.dto.Chat;
import com.linktracker.dto.ChatLink;
import com.linktracker.dto.Link;
import com.linktracker.dto.response.LinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ChatLinksRepository {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatsRepository chatRepository;
    List<ChatLink> chatLinks = new ArrayList<>();

    public void saveChatLink(ChatLink chatLink) {
        chatLinks.add(chatLink);
    }

    public boolean saveChatLinksByParams(long chatId,
                                      String url,
                                      List<String> tags,
                                      List<String> filters) {
        Link linkid = linkRepository.getLinkByUrl(url);
        if (linkid == null) return false;
        for (ChatLink chatLink : chatLinks) {
            if(chatLink.linkId() == linkid.id() && chatLink.chatId() == chatId) {
                return false;
            }
        }
        ChatLink chatLink = new ChatLink(chatId, linkid.id(), tags, filters);
        chatLinks.add(chatLink);
        return true;
    }

    public List<Chat> getChatIdByLink(String url) {
        Link link = linkRepository.getLinkByUrl(url);
        List<Chat> ans = new ArrayList<>();
        if (link == null) return ans;
        for (ChatLink chatLink : chatLinks) {
            if (chatLink.linkId() == link.id()) {
                ans.add(new Chat(chatLink.chatId()));
            }
        }
        return ans;
    }

    public List<LinkResponse> getLinksByChatId(long chatId) {
        List<LinkResponse> ans = new ArrayList<>();
        for (ChatLink chatLink : chatLinks) {
            if (chatLink.chatId() == chatId) {
                Link link = linkRepository.getLinkById(chatLink.linkId());
                if (link != null) {
                    ans.add(new LinkResponse(link.id(), link.url(), chatLink.tags(), chatLink.filters()));
                }
            }
        }
        return ans;
    }

    public boolean deleteLinkByParams(long chatId, String url) {
        Link link = linkRepository.getLinkByUrl(url);
        if (link == null) return false;
        long linkId = link.id();
        return chatLinks.removeIf(chatLink -> chatLink.chatId() == chatId &&
                chatLink.linkId() == linkId);
    }
}
