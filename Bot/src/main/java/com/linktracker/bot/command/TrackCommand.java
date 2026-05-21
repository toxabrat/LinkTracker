package com.linktracker.bot.command;

import com.linktracker.bot.LinkTrackerBot;
import com.linktracker.client.LinkClient;
import com.linktracker.dto.request.AddLinkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class TrackCommand implements Command {
    @Lazy
    @Autowired
    private LinkTrackerBot linkTrackerBot;

    @Autowired
    private LinkClient linkClient;

    @Override
    public String getName() {
        return "/track";
    }

    @Override
    public void handle(Update update) {
        String text = update.getMessage().getText().trim();
        long chatId = update.getMessage().getChatId();
        String[] parts = text.split(" ");
        if (parts.length < 2) {
            linkTrackerBot.sendMessage(chatId, "Вы не ввели ссылку. Использование: /track <ссылка>");
            return;
        }
        String url = parts[1];
        boolean result = linkClient.postLink(chatId, new AddLinkRequest(url, List.of(), List.of()));
        if (result) {
            linkTrackerBot.sendMessage(chatId, "ссылка привязана");
        } else {
            String textMessage = """
                    не удалось привязать ссылку
                    возможно вы уже привязали эту ссылку,
                    сначала отвижите ее
                    """;
            linkTrackerBot.sendMessage(chatId, textMessage);
        }
    }

}

