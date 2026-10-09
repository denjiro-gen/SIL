package com.example.api.service;

import com.example.api.dto.StudentCreateDto;
import com.example.api.dto.StudentResponseDto;
import com.example.api.entity.Course;
import com.example.api.entity.Student;
import com.example.api.entity.Subject;
import com.example.api.exception.DuplicateResourceException;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.CourseRepository;
import com.example.api.repository.StudentRepository;
import com.example.api.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
                .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + id + " was not found."));
        return mapToDto(student);
    }

    public void createStudent(StudentCreateDto dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email '" + dto.getEmail() + "' is already registered.");
        }
        if (studentRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username '" + dto.getUsername() + "' is already registered.");
        }
        Student student = new Student();
        student.setFullName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setUsername(dto.getUsername());
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setRole("ROLE_STUDENT");
        studentRepository.save(student);
    }

    public void assignCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " was not found."));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course with ID " + courseId + " was not found."));
        student.setCourse(course);
        studentRepository.save(student);
    }

    public void enrollSubject(Long studentId, Long subjectId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " was not found."));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject with ID " + subjectId + " was not found."));
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
        }
        return dto;
    }
}
