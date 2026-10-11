INSERT INTO courses (name) VALUES ('BS Computer Science'), ('BS Cybersecurity');
INSERT INTO subjects (title) VALUES ('Java Programming'), ('Database Management Systems'), ('Cloud Computing');

-- bcrypt hashed passwords for admin123 and password123
INSERT INTO students (full_name, email, username, password, role, course_id) VALUES 
('System Admin', 'admin@example.com', 'admin', '$2a$10$w8T0.g2c2iB1P95pI2fC1.qf8XgN.qJv9f7Vl1bK6D5PZc5vNfG.', 'ROLE_ADMIN', null),
('Alex Mercer', 'alex.mercer@example.com', 'alex_mercer', '$2a$10$w8T0.g2c2iB1P95pI2fC1.qf8XgN.qJv9f7Vl1bK6D5PZc5vNfG.', 'ROLE_STUDENT', 1);

INSERT INTO student_subjects (student_id, subject_id) VALUES (2, 1), (2, 2);
