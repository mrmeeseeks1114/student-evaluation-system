package com.evaluation.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AdminEvaluationResponse(
    Long id,
    Long studentId,
    String studentName,
    String studentNumber,
    String course,
    String yearLevel,
    String instructor,
    String subject,
    Double rating,
    String comments,
    LocalDateTime submittedAt,
    List<AnswerResponse> answers
) {

    public record AnswerResponse(int questionNumber, int rating) {
    }
}
