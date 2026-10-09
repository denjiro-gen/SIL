package com.example.api.controller;

import com.example.api.dto.CourseDto;
import com.example.api.entity.Course;
import com.example.api.repository.CourseRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseRepository courseRepository;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> createCourse(@Valid @RequestBody CourseDto courseDto) {
        Course course = new Course();
        course.setName(courseDto.getName());
        courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
