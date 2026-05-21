package com.linktracker.updater;

import com.linktracker.client.external.api.GithubClient;
import com.linktracker.dto.Link;
import com.linktracker.dto.external.api.GitHubIssueResponse;
import com.linktracker.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GitHubUpdater implements LinkUpdater {
    private final GithubClient githubClient;
    private final LinkRepository linkRepository;

    @Override
    public String getUrlDefault() {
        return "https://github.com";
    }

    @Override
    public Optional<String> check(Link link) {
        String owner = link.url().split("/")[3];
        String repo = link.url().split("/")[4];
        OffsetDateTime date = link.lastCheckedAt();
        OffsetDateTime checkTime = OffsetDateTime.now();
        List<GitHubIssueResponse> issues = githubClient.getIssues(owner, repo, date);
        linkRepository.updateLastCheck(link.url(), checkTime);
        StringBuilder answer = new StringBuilder();

        if (!issues.isEmpty()) {
            linkRepository.updateUpdatedAt(link.url(), checkTime);
            answer.append(link.url()).append("\n");
            answer.append("было получено ")
                    .append(issues.size())
                    .append(" новых сообщений\n");
            issues.forEach(issues_elem -> {
                String update = """
                        Новый PR/Issues: %s
                        Автор: %s
                        Дата: %s
                        %s
                        """.formatted(
                                issues_elem.title(),
                        issues_elem.user().login(),
                        issues_elem.createdAt(),
                        issues_elem.body() != null ?
                                issues_elem.body().substring(0, Math.min(issues_elem.body().length(), 200)) :
                                ""
                );
                answer.append(update);
                answer.append("\n\n");
            });
            return Optional.of(answer.toString());
        }
        return Optional.empty();
    }
}
