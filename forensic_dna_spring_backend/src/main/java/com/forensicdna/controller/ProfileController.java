package com.forensicdna.controller;

import com.forensicdna.entity.Profile;
import com.forensicdna.repository.ProfileGenotypeRepository;
import com.forensicdna.repository.ProfileRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profiles")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileRepository profileRepo;
    private final ProfileGenotypeRepository genotypeRepo;

    public ProfileController(
            ProfileRepository profileRepo,
            ProfileGenotypeRepository genotypeRepo
    ) {
        this.profileRepo = profileRepo;
        this.genotypeRepo = genotypeRepo;
    }

    // =====================================================
    // GET ALL PROFILES
    // =====================================================

    @GetMapping
    public List<Profile> listProfiles() {

        return profileRepo.findAll();
    }

    // =====================================================
    // GET PROFILE BY SAMPLE ID
    // =====================================================

    @GetMapping("/{sampleId}")
    public Map<String, Object> getProfile(
            @PathVariable String sampleId
    ) {

        Profile profile = profileRepo
                .findBySampleId(sampleId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Profile not found: " + sampleId
                        )
                );

        Map<String, Object> response = new HashMap<>();

        response.put("profile", profile);
        response.put(
                "genotypes",
                genotypeRepo.findByProfileId(profile.getId())
        );

        return response;
    }
}