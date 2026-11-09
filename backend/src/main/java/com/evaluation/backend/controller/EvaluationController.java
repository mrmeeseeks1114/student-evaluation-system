package com.evaluation.backend.controller;

import com.evaluation.backend.model.Answer;
import com.evaluation.backend.model.Evaluation;
import com.evaluation.backend.service.EvaluationService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evaluations")
@CrossOrigin(origins = "*")
public class EvaluationController {


    private final EvaluationService evaluationService;


    public EvaluationController(
        EvaluationService evaluationService
    ) {

        this.evaluationService =
            evaluationService;
    }


    @PostMapping
    public ResponseEntity<?> submitEvaluation(
        @RequestBody Map<String, Object> data
    ) {

        try {

            Evaluation evaluation =
                new Evaluation();


            evaluation.setStudentId(
                Long.parseLong(
                    data.get("studentId")
                        .toString()
                )
            );


            evaluation.setInstructor(
                data.get("instructor")
                    .toString()
            );


            evaluation.setSubject(
                data.get("subject")
                    .toString()
            );


            evaluation.setRating(
                Double.parseDouble(
                    data.get("rating")
                        .toString()
                )
            );


            evaluation.setComments(
                data.get("comments")
                    .toString()
            );


            List<Answer> answers =
                new ArrayList<>();


            List<Map<String, Object>>
                answerData =
                    (List<Map<String, Object>>)
                        data.get("answers");


            for (
                Map<String, Object> answer :
                answerData
            ) {

                int questionNumber =
                    Integer.parseInt(
                        answer
                            .get("questionNumber")
                            .toString()
                    );


                int rating =
                    Integer.parseInt(
                        answer
                            .get("rating")
                            .toString()
                    );


                answers.add(
                    new Answer(
                        null,
                        questionNumber,
                        rating
                    )
                );
            }


            Evaluation saved =
                evaluationService
                    .submitEvaluation(
                        evaluation,
                        answers
                    );


            return ResponseEntity.ok(saved);


        } catch (Exception e) {

            return ResponseEntity
                .badRequest()
                .body(
                    Map.of(
                        "message",
                        e.getMessage()
                    )
                );
        }
    }


    @GetMapping("/student/{studentId}")
    public List<Evaluation>
    getStudentEvaluations(
        @PathVariable Long studentId
    ) {

        return evaluationService
            .getStudentEvaluations(studentId);
    }
}