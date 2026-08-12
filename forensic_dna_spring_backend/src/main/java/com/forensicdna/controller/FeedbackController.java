package com.forensicdna.controller;

import com.forensicdna.dto.FeedbackStatusUpdate;
import com.forensicdna.entity.Feedback;
import com.forensicdna.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<?> submitFeedback(
            Authentication auth,
            @RequestParam("module") String module,
            @RequestParam("message") String message,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {
        Feedback saved = feedbackService.submitFeedback(auth.getName(), module, message, file);

        return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "status", "submitted"
        ));
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/me")
    public List<Feedback> getMyFeedback(Authentication auth) {
        return feedbackService.getMyFeedback(auth.getName());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Feedback updateStatus(
            @PathVariable UUID id,
            @RequestBody FeedbackStatusUpdate body
    ) {
        return feedbackService.updateStatus(id, body);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> deleteFeedback(@PathVariable UUID id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(Map.of("status", "deleted"));
    }
}