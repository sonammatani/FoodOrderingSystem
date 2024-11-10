package com.project.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class GenericApiResponseDto {
    HttpStatus status;
    String message;
}
