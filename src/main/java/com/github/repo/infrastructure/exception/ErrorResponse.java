package com.github.repo.infrastructure.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorResponse
{
    private int errorCode;
    private String message;
    private String Controller;
    private String Service;
    private String path;
    private String timestamp;
}
