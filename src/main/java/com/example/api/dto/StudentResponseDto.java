package com.example.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class StudentResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String course;
    private List<String> subjects;
}
