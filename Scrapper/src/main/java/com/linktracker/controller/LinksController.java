package com.linktracker.controller;

import com.linktracker.dto.request.AddLinkRequest;
import com.linktracker.dto.response.LinkResponse;
import com.linktracker.repository.ChatLinksRepository;
import com.linktracker.repository.LinkRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/links")
public class LinksController {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatLinksRepository chatLinksRepository;

    @Operation(summary = "добавить ссылку")
    @PostMapping()
    public ResponseEntity<Void> postLink(@Parameter(description = "ссылка", example = "https://link") @RequestBody AddLinkRequest addLinkRequest,
                         @RequestHeader("Tg-Chat-Id") Long chatId) {
        linkRepository.saveLinkByString(addLinkRequest.url());
        boolean isNew = chatLinksRepository.saveChatLinksByParams(
                chatId,
                addLinkRequest.url(),
                addLinkRequest.tags(),
                addLinkRequest.filters()
        );
        if (isNew) {
            return ResponseEntity.ok().build();
        } else  {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "получить все отслеживаемые ссылки")
    @GetMapping()
    public List<LinkResponse> getLinks(@RequestHeader("Tg-Chat-Id") Long chatId) {
        return chatLinksRepository.getLinksByChatId(chatId);
    }

    @Operation(summary = "удалить ссылку")
    @DeleteMapping
    public ResponseEntity<Void> deleteLink(@RequestHeader("Tg-Chat-Id") Long chatId,
                                     @Parameter(description = "ссылка") @RequestParam String url) {
        boolean resultDelete = chatLinksRepository.deleteLinkByParams(chatId, url);
        if (resultDelete) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
