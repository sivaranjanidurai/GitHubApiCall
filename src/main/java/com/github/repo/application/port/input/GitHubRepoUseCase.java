package com.github.repo.application.port.input;

import com.github.repo.application.dto.GitHubRepoInfoDto;

public interface GitHubRepoUseCase
{
    public GitHubRepoInfoDto getRepoInfoThroughAPIorCache(String owner, String repositoryName);
}
