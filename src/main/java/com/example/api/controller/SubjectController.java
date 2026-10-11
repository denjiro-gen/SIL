package com.example.api.controller;

import com.example.api.dto.SubjectDto;
import com.example.api.entity.Subject;
import com.example.api.repository.SubjectRepository;
import com.example.api.exception.NotFoundException;
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

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject with ID " + id + " was not found."));
        return ResponseEntity.ok(subject);
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setTitle(subjectDto.getTitle());
        Subject saved = subjectRepository.save(subject);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
