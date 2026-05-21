package com.linktracker.bot.command;

import com.linktracker.bot.LinkTrackerBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UnknownCommand implements Command {
    @Lazy
    @Autowired
    private LinkTrackerBot linkTrackerBot;

    @Override
    public String getName() {
        return "/unknown";
    }

    @Override
    public void handle(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        linkTrackerBot.sendMessage(chatId, "вы ввели неправильную команду или не нажали /start");
    }
}
