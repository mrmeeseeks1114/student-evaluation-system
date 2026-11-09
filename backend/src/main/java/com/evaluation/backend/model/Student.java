package com.evaluation.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        unique = true,
        nullable = false
    )
    private String studentNumber;

    private String name;

    private String course;

    private String yearLevel;


    public Student() {
    }


    public Student(
        String studentNumber,
        String name,
        String course,
        String yearLevel
    ) {

        this.studentNumber = studentNumber;
        this.name = name;
        this.course = course;
        this.yearLevel = yearLevel;
    }


    public Long getId() {
        return id;
    }


    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(
        String studentNumber
    ) {

        this.studentNumber = studentNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {

        this.course = course;
    }


    public String getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(
        String yearLevel
    ) {

        this.yearLevel = yearLevel;
    }
}