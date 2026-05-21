package com.linktracker.bot.command;

import com.linktracker.bot.LinkTrackerBot;
import com.linktracker.client.TGChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements Command {
    @Lazy
    @Autowired
    private LinkTrackerBot linkTrackerBot;

    @Autowired
    private TGChatClient tgChatClient;

    @Override
    public String getName() {
        return "/start";
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();
        tgChatClient.postRegisterChatById(chatId);
        linkTrackerBot.setAllCommands(chatId);
        linkTrackerBot.sendMessage(chatId, "Добро пожаловать! Используйте /help для списка команд.");
    }
}
