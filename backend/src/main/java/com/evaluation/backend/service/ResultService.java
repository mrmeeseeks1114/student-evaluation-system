package com.evaluation.backend.service;

import com.evaluation.backend.dto.AdminEvaluationResponse;
import com.evaluation.backend.dto.AdminEvaluationResponse.AnswerResponse;
import com.evaluation.backend.model.Answer;
import com.evaluation.backend.model.Evaluation;
import com.evaluation.backend.model.Student;
import com.evaluation.backend.repository.AnswerRepository;
import com.evaluation.backend.repository.EvaluationRepository;
import com.evaluation.backend.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private final EvaluationRepository
        evaluationRepository;

    private final StudentRepository studentRepository;

    private final AnswerRepository answerRepository;

    public ResultService(
        EvaluationRepository evaluationRepository,
        StudentRepository studentRepository,
        AnswerRepository answerRepository
    ) {
        this.evaluationRepository =
            evaluationRepository;
        this.studentRepository = studentRepository;
        this.answerRepository = answerRepository;
    }

    public List<AdminEvaluationResponse>
    getAllResults() {
        List<Evaluation> evaluations =
            evaluationRepository.findAllByOrderBySubmittedAtDesc();
        if (evaluations.isEmpty()) {
            return List.of();
        }

        List<Long> studentIds = evaluations.stream()
            .map(Evaluation::getStudentId)
            .distinct()
            .toList();
        Map<Long, Student> students = studentRepository.findAllById(studentIds).stream()
            .collect(Collectors.toMap(Student::getId, Function.identity()));

        List<Long> evaluationIds = evaluations.stream()
            .map(Evaluation::getId)
            .toList();
        Map<Long, List<AnswerResponse>> answersByEvaluation =
            answerRepository.findByEvaluationIdInOrderByQuestionNumberAsc(evaluationIds).stream()
                .collect(Collectors.groupingBy(
                    Answer::getEvaluationId,
                    Collectors.mapping(
                        answer -> new AnswerResponse(
                            answer.getQuestionNumber(),
                            answer.getRating()
                        ),
                        Collectors.toList()
                    )
                ));

        return evaluations.stream()
            .map(evaluation -> {
                Student student = students.get(evaluation.getStudentId());
                return new AdminEvaluationResponse(
                    evaluation.getId(),
                    evaluation.getStudentId(),
                    student == null ? null : student.getName(),
                    student == null ? null : student.getStudentNumber(),
                    student == null ? null : student.getCourse(),
                    student == null ? null : student.getYearLevel(),
                    evaluation.getInstructor(),
                    evaluation.getSubject(),
                    evaluation.getRating(),
                    evaluation.getComments(),
                    evaluation.getSubmittedAt(),
                    answersByEvaluation.getOrDefault(evaluation.getId(), List.of())
                );
            })
            .toList();
    }
}