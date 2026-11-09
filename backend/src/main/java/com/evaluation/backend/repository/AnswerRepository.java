package com.evaluation.backend.repository;

import com.evaluation.backend.model.Answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository
    extends JpaRepository<Answer, Long> {

    List<Answer>
    findByEvaluationId(Long evaluationId);

    List<Answer>
    findByEvaluationIdInOrderByQuestionNumberAsc(List<Long> evaluationIds);
}