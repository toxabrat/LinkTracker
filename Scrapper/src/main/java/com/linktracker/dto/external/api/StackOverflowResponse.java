package com.linktracker.dto.external.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record StackOverflowResponse(
        String title,
        StackOverflowOwner owner,
        @JsonProperty("creation_date")
        Long creationDate,
        String body
) {
    public record StackOverflowOwner(
            @JsonProperty("display_name")
            String displayName
    ) {}
}

