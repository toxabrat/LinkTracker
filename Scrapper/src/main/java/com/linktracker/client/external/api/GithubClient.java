package com.linktracker.client.external.api;

import com.linktracker.dto.external.api.GitHubIssueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class GithubClient {
    private WebClient webClient;

    public GithubClient() {
        webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .build();
    }

    public List<GitHubIssueResponse> getIssues(
            String owner,
            String repo,
            OffsetDateTime since
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repos/{owner}/{repo}/issues")
                        .queryParam("since", since)
                        .queryParam("state", "all")
                        .queryParam("per_page", "5")
                        .build(owner, repo))
                .retrieve()
                .bodyToFlux(GitHubIssueResponse.class)
                .collectList()
                .block();
    }
}
