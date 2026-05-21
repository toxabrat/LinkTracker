package com.linktracker.dto;

import java.time.OffsetDateTime;

public record Link(
        long id,
        String url,
        OffsetDateTime lastCheckedAt,
        OffsetDateTime updatedAt
) {}
