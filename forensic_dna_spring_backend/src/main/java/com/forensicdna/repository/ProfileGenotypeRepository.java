package com.forensicdna.repository;

import com.forensicdna.entity.ProfileGenotype;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ProfileGenotypeRepository extends JpaRepository<ProfileGenotype, Integer> {
    List<ProfileGenotype> findByProfileId(UUID profileId);
}