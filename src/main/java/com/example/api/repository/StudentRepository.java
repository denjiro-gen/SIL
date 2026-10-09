package com.example.api.repository;

import com.example.api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
