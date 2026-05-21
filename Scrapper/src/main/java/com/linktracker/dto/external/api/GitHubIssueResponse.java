package com.linktracker.dto.external.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GitHubIssueResponse(
        String title,
        GitHubUser user,
        @JsonProperty("created_at")
        OffsetDateTime createdAt,
        String body
) {
    public record GitHubUser(String login) {}
}