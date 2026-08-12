package com.forensicdna.controller;

import com.forensicdna.service.MatchService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.*;

@RestController
@RequestMapping("/matches")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")

public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/partial/{evidenceId}")
    public List<Map<String, Object>> matchEvidence(@PathVariable UUID evidenceId) {
        return matchService.matchEvidence(evidenceId);
    }
}