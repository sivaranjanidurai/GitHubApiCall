package com.github.repo.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name ="TB_GITHUB_REPO_RES")
@NoArgsConstructor
public class GitHubRepoInfoEntity implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Column(columnDefinition = "JSON")
    private String response;


    @Column(unique=true)
    private Integer repoId;

    @Column(unique=true)
    private String fullName;
    private String description;
    private String cloneUrl;
    private Integer stars;
    private String createdAt;

}
