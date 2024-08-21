package com.github.repo.infrastructure.adapter;

import com.github.repo.application.port.output.GitHubApiGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@Component
public class GitHubApiGatewayImpl implements GitHubApiGateway {

    @Value("${github.repo.url}")
    private String gitHubRepoUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<LinkedHashMap<String, String>> fetchGitHubApi(String owner) {

        List<LinkedHashMap<String, String>> repos = restTemplate.getForObject(
                gitHubRepoUrl + owner + "/repos", List.class);
        return repos;
    }
}
