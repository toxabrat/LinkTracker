package com.linktracker.controller;

import com.linktracker.repository.ChatsRepository;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tg-chat")
public class TGChatController {
    @Autowired
    ChatsRepository chatsRepository;

    @GetMapping
    public List<Long> getAllChatIds() {
        return chatsRepository.getChats().stream().map(chat -> chat.id()).toList();
    }

    @PostMapping("/{id}")
    public void postRegisterChatById(@Parameter(description = "tg chat id", example = "123456789")
                                 @PathVariable("id") Long id) {
        chatsRepository.saveChatByLong(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatById(@Parameter(description = "tg chat id", example = "123456789")
                                @PathVariable("id") Long id) {
        boolean deleteResult = chatsRepository.deleteChatByLong(id);
        if (deleteResult) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
