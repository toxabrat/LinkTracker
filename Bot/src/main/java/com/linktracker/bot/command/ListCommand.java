package com.linktracker.bot.command;

import com.linktracker.bot.LinkTrackerBot;
import com.linktracker.client.LinkClient;
import com.linktracker.dto.response.LinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class ListCommand implements Command {
    @Lazy
    @Autowired
    private LinkTrackerBot linkTrackerBot;
    @Autowired
    private LinkClient linkClient;

    @Override
    public String getName() {
        return "/list";
    }

    @Override
    public void handle(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        List<LinkResponse> links = linkClient.getLinks(chatId);

        StringBuilder message = new StringBuilder();

        if (links.isEmpty()) {
            message.append("У вас нет отслеживаемых ссылок\n");
        }

        for (LinkResponse link : links) {
            message.append(link.url()).append(" ").append('\n');
            if (!link.tags().isEmpty()) {
                message.append("тэги: ").append(String.join(", ",link.tags())).append('\n');
            }
            if (!link.filters().isEmpty()) {
                message.append("фильтры: ").append(String.join(", ", link.filters())).append('\n');
            }
        }
        message.append('\n');

        linkTrackerBot.sendMessage(chatId, message.toString());
    }
}
