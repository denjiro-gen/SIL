package com.example.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubjectDto {
    @NotBlank(message = "Subject title is required")
    private String title;
}
