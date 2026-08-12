package com.forensicdna.controller;

import com.forensicdna.entity.StrLocus;
import com.forensicdna.repository.StrLocusRepository;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/loci")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")


public class LociController {

    private final StrLocusRepository repo;

    public LociController(StrLocusRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<StrLocus> listLoci() {
        return repo.findAll();
    }
}