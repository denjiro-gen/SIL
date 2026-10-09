package com.example.api.controller;

import com.example.api.dto.SubjectDto;
import com.example.api.entity.Subject;
import com.example.api.repository.SubjectRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectRepository subjectRepository;

    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> createSubject(@Valid @RequestBody SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setTitle(subjectDto.getTitle());
        subjectRepository.save(subject);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
