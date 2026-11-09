package com.evaluation.backend.repository;

import com.evaluation.backend.model.Evaluation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository
    extends JpaRepository<Evaluation, Long> {

    List<Evaluation>
    findByStudentId(Long studentId);

    List<Evaluation>
    findAllByOrderBySubmittedAtDesc();


    boolean existsByStudentIdAndSubject(
        Long studentId,
        String subject
    );
}