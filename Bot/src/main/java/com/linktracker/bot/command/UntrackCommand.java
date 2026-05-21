package com.linktracker.bot.command;

import com.linktracker.bot.LinkTrackerBot;
import com.linktracker.client.LinkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UntrackCommand implements Command {
    @Lazy
    @Autowired
    private LinkTrackerBot linkTrackerBot;

    @Autowired
    private LinkClient linkClient;

    @Override
    public String getName() {
        return "/untrack";
    }

    @Override
    public void handle(Update update) {
        String text = update.getMessage().getText().trim();
        long chatId = update.getMessage().getChatId();
        String[] parts = text.split(" ");
        if (parts.length < 2) {
            linkTrackerBot.sendMessage(chatId, "Вы не ввели ссылку. Использование: /untrack <ссылка>");
            return;
        }
        String url = parts[1];
        boolean result = linkClient.deleteLink(chatId, url);
        if (result) {
            linkTrackerBot.sendMessage(chatId, "ссылка успешно отвязана");
        } else {
            String textMessage = """
                    Не удалось отвязать
                    возможно вы указали не верную ссылку
                    """;
            linkTrackerBot.sendMessage(chatId, textMessage);
        }
    }
}

