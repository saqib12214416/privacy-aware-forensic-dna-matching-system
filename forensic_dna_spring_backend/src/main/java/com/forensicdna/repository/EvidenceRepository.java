package com.forensicdna.repository;

import com.forensicdna.entity.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EvidenceRepository extends JpaRepository<Evidence, UUID> {
}