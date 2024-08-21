package com.github.repo.presentation.controller;

import com.github.repo.application.dto.GitHubRepoInfoDto;
import com.github.repo.application.port.input.GitHubRepoUseCase;
import lombok.extern.java.Log;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Log
@RestController
@RequestMapping("/repositories")
@EnableCaching
public class GitHubRepoController
{

    private final GitHubRepoUseCase gitHubRepoUseCase;

    public GitHubRepoController(GitHubRepoUseCase gitHubRepoUseCase) {
        this.gitHubRepoUseCase = gitHubRepoUseCase;
    }


    @GetMapping("/{owner}/{repositoryName}")
    ResponseEntity<GitHubRepoInfoDto> getRepository(@PathVariable("owner") String owner, @PathVariable("repositoryName") String repositoryName)
    {
        log.info("Owner : "+ owner );
        log.info("RepositoryName : "+ repositoryName );

        GitHubRepoInfoDto gitHubRepoInfoDto = gitHubRepoUseCase.
                    getRepoInfoThroughAPIorCache(owner, repositoryName);

        if(null!=gitHubRepoInfoDto)
            return new ResponseEntity<>(gitHubRepoInfoDto, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
