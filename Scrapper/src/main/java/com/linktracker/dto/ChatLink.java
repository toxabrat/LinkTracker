package com.linktracker.dto;

import java.util.List;

public record ChatLink(
        Long chatId,
        Long linkId,
        List<String> tags,
        List<String> filters
        ) {}
