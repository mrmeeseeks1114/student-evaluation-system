package com.evaluation.backend.controller;

import com.evaluation.backend.model.Student;
import com.evaluation.backend.service.StudentService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {


    private final StudentService studentService;


    public StudentController(
        StudentService studentService
    ) {

        this.studentService =
            studentService;
    }


    @GetMapping("/login/{studentNumber}")
    public ResponseEntity<Student> login(
        @PathVariable String studentNumber
    ) {

        return studentService
            .findByStudentNumber(studentNumber)
            .map(ResponseEntity::ok)
            .orElse(
                ResponseEntity.notFound().build()
            );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Student>
    getStudent(
        @PathVariable Long id
    ) {

        return studentService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElse(
                ResponseEntity.notFound().build()
            );
    }
}