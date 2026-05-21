package com.linktracker.updater;

import com.linktracker.dto.Link;

import java.util.Optional;

public interface LinkUpdater {
    String getUrlDefault();
    Optional<String> check(Link link);
}
