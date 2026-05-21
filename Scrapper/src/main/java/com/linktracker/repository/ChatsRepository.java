package com.linktracker.repository;

import com.linktracker.dto.Chat;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ChatsRepository {
    @Getter
    List<Chat> chats = new ArrayList<>();

    public void saveChat(Chat chat) {
        chats.add(chat);
    }

    public void saveChatByLong(long chatId) {
        Chat chat = new Chat(chatId);
        saveChat(chat);
    }

    public Chat getChatById(long chatId) {
        for (Chat chat : chats) {
            if (chat.id() == chatId) {
                return chat;
            }
        }
        return null;
    }

    public boolean deleteChatByLong(long chatId) {
        return chats.removeIf(chat -> chat.id() == chatId);
    }
}
