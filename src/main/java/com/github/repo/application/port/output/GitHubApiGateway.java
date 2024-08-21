package com.github.repo.application.port.output;

import java.util.LinkedHashMap;
import java.util.List;


public interface GitHubApiGateway
{
    List<LinkedHashMap<String, String>> fetchGitHubApi(String owner);
}
