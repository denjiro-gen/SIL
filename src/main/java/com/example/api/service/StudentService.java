package com.example.api.service;

import com.example.api.dto.StudentCreateDto;
import com.example.api.dto.StudentResponseDto;
import com.example.api.entity.Course;
import com.example.api.entity.Student;
import com.example.api.entity.Subject;
import com.example.api.exception.DuplicateException;
import com.example.api.exception.NotFoundException;
import com.example.api.repository.CourseRepository;
import com.example.api.repository.StudentRepository;
import com.example.api.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;

    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public StudentResponseDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student with ID " + id + " was not found."));
        return mapToDto(student);
    }

    @Transactional
    public void createStudent(StudentCreateDto dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateException("Email '" + dto.getEmail() + "' is already registered.");
        }
        if (studentRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateException("Username '" + dto.getUsername() + "' is already registered.");
        }
        Student student = new Student();
        student.setFullName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setUsername(dto.getUsername());
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setRole("ROLE_STUDENT");
        studentRepository.save(student);
    }

    @Transactional
    public void assignCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student with ID " + studentId + " was not found."));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course with ID " + courseId + " was not found."));
        student.setCourse(course);
        studentRepository.save(student);
    }

    @Transactional
    public void enrollSubject(Long studentId, Long subjectId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student with ID " + studentId + " was not found."));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new NotFoundException("Subject with ID " + subjectId + " was not found."));
        student.getSubjects().add(subject);
        studentRepository.save(student);
    }

    private StudentResponseDto mapToDto(Student student) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getEmail());
        if (student.getCourse() != null) {
            dto.setCourse(student.getCourse().getName());
        }
        if (student.getSubjects() != null) {
            dto.setSubjects(student.getSubjects().stream().map(Subject::getTitle).collect(Collectors.toList()));
        } else {
            dto.setSubjects(new ArrayList<>());
        }
        return dto;
    }
}
