package com.linktracker.bot;

import com.linktracker.service.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.List;

@Slf4j
@Component
public class LinkTrackerBot extends TelegramLongPollingBot {
    @Autowired
    TelegramBotService telegramBotService;

    @Override
    public String getBotUsername() {
        return "MyLinkTracker0_bot";
    }

    public LinkTrackerBot(@Value("${TELEGRAM_BOT_TOKEN}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        telegramBotService.handleUpdate(update);
    }

    public void sendMessage(long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            execute(sendMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void setAllCommands(long chatId) {
        try {
            execute(new SetMyCommands(
                    List.of(
                            new BotCommand("/start", "Регистрация"),
                            new BotCommand("/help", "Список команд"),
                            new BotCommand("/track", "<ссылка> - Начать отслеживать ссылку"),
                            new BotCommand("/untrack", "<ссылка> - Прекратить отслеживание"),
                            new BotCommand("/list", "Список отслеживаемых ссылок")
                    ),
                    new BotCommandScopeChat(String.valueOf(chatId)),
                    null
            ));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onRegister() {
        try {
            execute(new SetMyCommands(
                    List.of(new BotCommand("/start", "Регистрация")),
                    new BotCommandScopeDefault(),
                    null
            ));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
