CREATE DATABASE IF NOT EXISTS student_evaluation;

USE student_evaluation;


CREATE TABLE IF NOT EXISTS students (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    student_number VARCHAR(50)
        NOT NULL UNIQUE,

    name VARCHAR(100)
        NOT NULL,

    course VARCHAR(100)
        NOT NULL,

    year_level VARCHAR(50)
        NOT NULL
);


CREATE TABLE IF NOT EXISTS evaluations (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    student_id BIGINT NOT NULL,

    instructor VARCHAR(100)
        NOT NULL,

    subject VARCHAR(100)
        NOT NULL,

    rating DOUBLE NOT NULL,

    comments TEXT,

    submitted_at DATETIME
);


CREATE TABLE IF NOT EXISTS answers (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    evaluation_id BIGINT NOT NULL,

    question_number INT NOT NULL,

    rating INT NOT NULL
);

INSERT INTO students
(
    student_number,
    name,
    course,
    year_level
)
VALUES
(
    '2026-0001',
    'Juan Dela Cruz',
    'BS Information Technology',
    '2nd Year'
),
(
    '2026-0002',
    'Maria Santos',
    'BS Computer Science',
    '2nd Year'
),
(
    '2026-0003',
    'Pedro Garcia',
    'BS Information Technology',
    '3rd Year'
);