CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_STUDENT',
    course_id BIGINT,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE student_subjects (
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, subject_id),
    CONSTRAINT fk_ss_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_ss_subject FOREIGN KEY (subject_id) REFERENCES subjects(id)
);
