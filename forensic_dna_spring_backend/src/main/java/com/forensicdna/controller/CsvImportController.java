package com.forensicdna.controller;

import com.forensicdna.service.CsvImportService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/import")
public class CsvImportController {

    private final CsvImportService csvImportService;

    public CsvImportController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping("/profiles")
    public String importProfiles() {
        try {
            return csvImportService.importCombinedProfiles("/app/data/combined_profiles.csv");
        } catch (Exception e) {
            e.printStackTrace();
            return "CSV import failed: " + e.getMessage();
        }
    }
}