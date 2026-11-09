package com.evaluation.backend.controller;

import com.evaluation.backend.dto.AdminEvaluationResponse;
import com.evaluation.backend.service.AdminAuthService;
import com.evaluation.backend.service.ResultService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = {
    "http://localhost:3001",
    "http://127.0.0.1:3001",
    "http://localhost:8081",
    "http://127.0.0.1:8081"
})
public class ResultController {


    private final ResultService resultService;

    private final AdminAuthService adminAuthService;

    public ResultController(
        ResultService resultService,
        AdminAuthService adminAuthService
    ) {
        this.resultService =
            resultService;
        this.adminAuthService = adminAuthService;
    }

    @GetMapping
    public ResponseEntity<?> getResults(
        @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (!adminAuthService.isAuthenticated(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Administrator sign-in is required."));
        }
        List<AdminEvaluationResponse> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }
}