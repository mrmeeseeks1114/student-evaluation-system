package com.evaluation.backend.service;

import com.evaluation.backend.model.Answer;
import com.evaluation.backend.model.Evaluation;

import com.evaluation.backend.repository.AnswerRepository;
import com.evaluation.backend.repository.EvaluationRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EvaluationService {

    private final EvaluationRepository
        evaluationRepository;

    private final AnswerRepository
        answerRepository;


    public EvaluationService(
        EvaluationRepository evaluationRepository,
        AnswerRepository answerRepository
    ) {

        this.evaluationRepository =
            evaluationRepository;

        this.answerRepository =
            answerRepository;
    }


    public Evaluation submitEvaluation(
        Evaluation evaluation,
        List<Answer> answers
    ) {

        boolean alreadySubmitted =
            evaluationRepository
                .existsByStudentIdAndSubject(
                    evaluation.getStudentId(),
                    evaluation.getSubject()
                );


        if (alreadySubmitted) {

            throw new RuntimeException(
                "You have already evaluated this subject."
            );

        }


        evaluation.setSubmittedAt(
            LocalDateTime.now()
        );


        Evaluation saved =
            evaluationRepository.save(
                evaluation
            );


        for (Answer answer : answers) {

            answer.setEvaluationId(
                saved.getId()
            );

            answerRepository.save(answer);
        }


        return saved;
    }


    public List<Evaluation>
    getStudentEvaluations(
        Long studentId
    ) {

        return evaluationRepository
            .findByStudentId(studentId);
    }
}