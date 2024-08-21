package com.github.repo.domain.repository;

import com.github.repo.domain.entity.GitHubRepoInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface GitHubRepoInfoRepository extends JpaRepository<GitHubRepoInfoEntity, String>
{
    public GitHubRepoInfoEntity findByFullName(String repoFullName);
}
