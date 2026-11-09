package com.evaluation.backend.service;

import com.evaluation.backend.model.Student;
import com.evaluation.backend.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;


    public StudentService(
        StudentRepository studentRepository
    ) {

        this.studentRepository =
            studentRepository;
    }


    public Optional<Student>
    findByStudentNumber(
        String studentNumber
    ) {

        return studentRepository
            .findByStudentNumber(studentNumber);
    }


    public Optional<Student>
    findById(Long id) {

        return studentRepository.findById(id);
    }
}