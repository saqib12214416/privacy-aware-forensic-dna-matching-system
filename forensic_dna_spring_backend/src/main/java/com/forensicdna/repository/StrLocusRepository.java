
package com.forensicdna.repository;

import com.forensicdna.entity.StrLocus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StrLocusRepository extends JpaRepository<StrLocus, Integer> {
    Optional<StrLocus> findByLocus(String locus);
}