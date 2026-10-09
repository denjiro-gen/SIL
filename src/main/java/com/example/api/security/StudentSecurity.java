package com.example.api.security;

import com.example.api.entity.Student;
import com.example.api.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("studentSecurity")
@RequiredArgsConstructor
public class StudentSecurity {
    private final StudentRepository studentRepository;

    public boolean isSameStudent(Authentication authentication, Long studentId) {
        String currentUsername = authentication.getName();
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return false;
        }
        return student.getUsername().equals(currentUsername);
    }
}
