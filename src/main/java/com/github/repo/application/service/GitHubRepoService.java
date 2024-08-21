package com.github.repo.application.service;

import com.github.repo.application.dto.GitHubRepoInfoDto;
import com.github.repo.application.port.output.GitHubApiGateway;
import com.github.repo.application.port.input.GitHubRepoUseCase;
import com.github.repo.domain.entity.GitHubRepoInfoEntity;
import com.github.repo.domain.repository.GitHubRepoInfoRepository;
import com.github.repo.infrastructure.exception.GitRepoNotFoundException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.LinkedHashMap;
import java.util.List;


@Service
@RequiredArgsConstructor

@Slf4j
public class GitHubRepoService implements  GitHubRepoUseCase
{
    @Autowired
    private GitHubRepoInfoRepository orderRepository;

    private final GitHubApiGateway gitHubApiGateway;

    public GitHubRepoInfoDto getRepoInfoThroughAPIorCache(String owner, String repositoryName)
    {
        GitHubRepoInfoDto gitHubRepoInfoDto =null;
        try
        {
            //check GitHup Info already present in Redis cache or database
            GitHubRepoInfoEntity gitHubRepoInfo=
                    findGitHubInfo(owner+"/"+repositoryName);
            gitHubRepoInfoDto=mapToGitHubRepoInfo(gitHubRepoInfo);

            //If GitHup Info not present in cache trigger GitHub Api
            if( null== gitHubRepoInfoDto)
            {
                List<LinkedHashMap<String, String>> repos =gitHubApiGateway.fetchGitHubApi(owner);

                gitHubRepoInfoDto = repos.stream()
                        .filter(repo -> repo.get("name").equals(repositoryName))
                        .map(this::storeGitHubResponse)
                        .map(this::mapToGitHubRepoInfo)
                        .findFirst()
                        .get();
            }
            return gitHubRepoInfoDto;
        }
        catch(Exception e)
        {
            throw new GitRepoNotFoundException("Git Repo not found with: " + owner+"/"+repositoryName);
        }

    }

    private GitHubRepoInfoDto mapToGitHubRepoInfo(GitHubRepoInfoEntity gitHubRepoInfo)
    {
        GitHubRepoInfoDto gitHubRepoInfoDto=null;
        if(gitHubRepoInfo!=null)
        {
            gitHubRepoInfoDto=new GitHubRepoInfoDto();
            gitHubRepoInfoDto.setFullName(gitHubRepoInfo.getFullName());
            gitHubRepoInfoDto.setDescription(gitHubRepoInfo.getDescription());
            gitHubRepoInfoDto.setCloneUrl(gitHubRepoInfo.getCloneUrl());
            gitHubRepoInfoDto.setStars(gitHubRepoInfo.getStars());
            gitHubRepoInfoDto.setCreatedAt(gitHubRepoInfo.getCreatedAt());
        }
        return gitHubRepoInfoDto;
    }
    @CachePut(value = "GitRepoInfo", key = "#fullName")
    private GitHubRepoInfoEntity storeGitHubResponse(LinkedHashMap<String, String> linkedHashMap)
    {
        Gson gson = new Gson();
        String json = gson.toJson(linkedHashMap, LinkedHashMap.class);

        GitHubRepoInfoEntity gitHubRepoInfo=new GitHubRepoInfoEntity();
        gitHubRepoInfo.setRepoId(Integer.parseInt(String.valueOf(linkedHashMap.get("id"))));
        gitHubRepoInfo.setFullName(linkedHashMap.get("full_name"));
        gitHubRepoInfo.setDescription(linkedHashMap.get("description"));
        gitHubRepoInfo.setCloneUrl(linkedHashMap.get("clone_url"));
        gitHubRepoInfo.setStars(Integer.parseInt(String.valueOf(linkedHashMap.get("stargazers_count"))));
        gitHubRepoInfo.setCreatedAt(linkedHashMap.get("created_at"));
        gitHubRepoInfo.setResponse(json);

        orderRepository.save(gitHubRepoInfo);

        return  gitHubRepoInfo;
    }

   /* @Cacheable(value = "GitRepoInfo", key = "#fullName")*/
    private GitHubRepoInfoEntity findGitHubInfo(String RepoFullName)
    {
       return orderRepository.findByFullName(RepoFullName);
    }
}
