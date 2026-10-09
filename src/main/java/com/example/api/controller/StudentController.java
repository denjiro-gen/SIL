package com.example.api.controller;

import com.example.api.dto.StudentCreateDto;
import com.example.api.dto.StudentResponseDto;
import com.example.api.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and @studentSecurity.isSameStudent(authentication, #id))")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createStudent(@Valid @RequestBody StudentCreateDto dto) {
        studentService.createStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{studentId}/course/{courseId}")
    public ResponseEntity<Void> assignCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        studentService.assignCourse(studentId, courseId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{studentId}/subject/{subjectId}")
    public ResponseEntity<Void> enrollSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        studentService.enrollSubject(studentId, subjectId);
        return ResponseEntity.ok().build();
    }
}
