package com.forensicdna.repository;

import com.forensicdna.entity.Feedback;
import com.forensicdna.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByUserOrderByCreatedAtDesc(User user);
    List<Feedback> findAllByOrderByCreatedAtDesc();
}