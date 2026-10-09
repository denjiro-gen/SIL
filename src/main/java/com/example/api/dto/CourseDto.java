package com.example.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseDto {
    @NotBlank(message = "Course name is required")
    private String name;
}
