package com.linktracker.service;

import com.linktracker.bot.LinkTrackerBot;
import com.linktracker.bot.command.Command;
import com.linktracker.bot.command.UnknownCommand;
import com.linktracker.dto.request.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TelegramBotService {
    @Lazy
    @Autowired
    private LinkTrackerBot linkTrackerBot;

    private final Map<String, Command> commands;
    private final UnknownCommand unknownCommand;
    private final Set<Long> registeredChats = new HashSet<>();

    public TelegramBotService(List<Command> commands, UnknownCommand unknownCommand) {
        this.commands = commands.stream().collect(Collectors.toMap(Command::getName, command -> command));
        this.unknownCommand = unknownCommand;
    }

    public void registerChat(long chatId) {
        registeredChats.add(chatId);
    }

    public boolean isRegistered(long chatId) {
        return registeredChats.contains(chatId);
    }

    public void handleUpdate(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String text = update.getMessage().getText().trim();
        String firstWord = text.split("\\s+")[0];
        long chatId = update.getMessage().getChatId();

        if (!registeredChats.contains(chatId)) {
            if (firstWord.equals("/start")) {
                registeredChats.add(chatId);
                commands.get("/start").handle(update);
            } else {
                unknownCommand.handle(update);
            }
            return;
        }

        if (commands.containsKey(firstWord)) {
            commands.get(firstWord).handle(update);
        } else {
            unknownCommand.handle(update);
        }
    }

    public void sendNotification(LinkUpdateRequest linkUpdateRequest) {
        for (long tgChatId : linkUpdateRequest.tgChatIds()) {
            String notification = formatNotificationMessage(linkUpdateRequest);
            linkTrackerBot.sendMessage(tgChatId, notification);
        }
    }

    public String formatNotificationMessage(LinkUpdateRequest linkUpdateRequest) {
        return linkUpdateRequest.description();
    }
}
