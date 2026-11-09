package com.evaluation.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    private Long evaluationId;

    private int questionNumber;

    private int rating;


    public Answer() {
    }


    public Answer(
        Long evaluationId,
        int questionNumber,
        int rating
    ) {

        this.evaluationId = evaluationId;
        this.questionNumber = questionNumber;
        this.rating = rating;
    }


    public Long getId() {
        return id;
    }


    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(
        Long evaluationId
    ) {

        this.evaluationId = evaluationId;
    }


    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(
        int questionNumber
    ) {

        this.questionNumber = questionNumber;
    }


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {

        this.rating = rating;
    }
}