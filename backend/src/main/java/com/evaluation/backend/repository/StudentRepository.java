package com.evaluation.backend.repository;

import com.evaluation.backend.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository
    extends JpaRepository<Student, Long> {

    Optional<Student>
    findByStudentNumber(String studentNumber);
}