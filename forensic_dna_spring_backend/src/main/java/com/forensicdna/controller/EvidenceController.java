package com.forensicdna.controller;

import com.forensicdna.dto.EvidenceRequest;
import com.forensicdna.entity.Evidence;
import com.forensicdna.service.EvidenceService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;

@RestController
@RequestMapping("/evidence")
@CrossOrigin(origins = "http://localhost:5173")

public class EvidenceController {

    private final EvidenceService evidenceService;

    public EvidenceController(EvidenceService evidenceService) {
        this.evidenceService = evidenceService;
    }

    @PostMapping
    public Map<String, Object> submitEvidence(
            @RequestBody EvidenceRequest request,
            Authentication auth
    ) throws Exception {
        Evidence saved = evidenceService.submitEvidence(request, auth.getName());

        return Map.of(
                "evidence_id", saved.getId(),
                "status", "stored"
        );
    }

    @GetMapping
    public Map<String, Object> listEvidence() {
        List<Evidence> items = evidenceService.listEvidence();
        return Map.of(
                "count", items.size(),
                "items", items
        );
    }

    @GetMapping("/{id}")
    public Evidence getEvidence(@PathVariable UUID id) {
        return evidenceService.getEvidence(id);
    }
}