package com.linktracker.bot.command;

import com.linktracker.bot.LinkTrackerBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    String getName();
    void handle(Update update);
}
