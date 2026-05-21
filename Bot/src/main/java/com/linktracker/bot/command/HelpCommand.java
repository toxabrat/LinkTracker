package com.linktracker.bot.command;

import com.linktracker.bot.LinkTrackerBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpCommand implements Command {
    @Lazy
    @Autowired
    private LinkTrackerBot linkTrackerBot;

    private final String HELP_TEXT = """
                Доступные команды бота:
                /start - регистрация пользователя
                /help - вывод списка доступных команд
                /track ссылка - начать отслеживать ссылку
                /untrack ссылка - прекратить отслеживать ссылку
                /list - список отслеживаемых ссылок
                """;
    @Override
    public String getName() {
        return "/help";
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();
        linkTrackerBot.sendMessage(chatId, HELP_TEXT);
    }
}
