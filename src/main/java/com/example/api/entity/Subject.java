package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.util.Set;
import java.util.HashSet;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@ToString(exclude = "students")
@EqualsAndHashCode(of = "id")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @ManyToMany(mappedBy = "subjects")
    @JsonIgnore
    private Set<Student> students = new HashSet<>();
}
