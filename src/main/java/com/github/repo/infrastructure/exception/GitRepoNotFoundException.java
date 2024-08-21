package com.github.repo.infrastructure.exception;

public class GitRepoNotFoundException  extends RuntimeException {
    public GitRepoNotFoundException(String message) {
        super(message);
    }
}
