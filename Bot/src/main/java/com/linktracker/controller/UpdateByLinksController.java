package com.linktracker.controller;

import com.linktracker.dto.request.LinkUpdateRequest;
import com.linktracker.service.TelegramBotService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateByLinksController {
    @Autowired
    TelegramBotService telegramBotService;

    @PostMapping("/updates")
    public void postUpdates(@Parameter(description = "изменение ссылок")
                                                         @RequestBody LinkUpdateRequest linkUpdateRequest) {
        telegramBotService.sendNotification(linkUpdateRequest);
    }
}
