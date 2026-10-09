package com.example.api.config;

import com.example.api.entity.Student;
import com.example.api.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<Student> admin = studentRepository.findByUsername("admin");
        if (admin.isPresent()) {
            Student a = admin.get();
            a.setPassword(passwordEncoder.encode("admin123"));
            studentRepository.save(a);
        }

        Optional<Student> alex = studentRepository.findByUsername("alex_mercer");
        if (alex.isPresent()) {
            Student s = alex.get();
            s.setPassword(passwordEncoder.encode("password123"));
            studentRepository.save(s);
        }
    }
}
