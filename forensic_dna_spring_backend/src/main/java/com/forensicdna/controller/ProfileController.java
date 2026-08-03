package com.forensicdna.controller;

import com.forensicdna.entity.Profile;
import com.forensicdna.repository.ProfileRepository;
import com.forensicdna.repository.ProfileGenotypeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/profiles")
@CrossOrigin(origins = "http://localhost:5173")

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


    @GetMapping
    public List<Profile> listProfiles() {

        return profileRepo.findAll();

    }


    // GET PROFILE BY SAMPLE ID
    @GetMapping("/{sampleId}")
    public Map<String,Object> getProfile(
            @PathVariable String sampleId
    ) {


        Profile profile =
                profileRepo.findBySampleId(sampleId)
                .orElseThrow(
                    () -> new RuntimeException("Profile not found")
                );


        return Map.of(
                "profile", profile,
                "genotypes",
                genotypeRepo.findByProfileId(profile.getId())
        );

    }

}