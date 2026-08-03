package com.forensicdna.service;

import com.forensicdna.dto.FeedbackStatusUpdate;
import com.forensicdna.entity.Feedback;
import com.forensicdna.entity.User;
import com.forensicdna.repository.FeedbackRepository;
import com.forensicdna.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    private final String uploadDir = "data/uploads/privacy";

    public FeedbackService(
            FeedbackRepository feedbackRepository,
            UserRepository userRepository
    ) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    public Feedback submitFeedback(
            String userId,
            String module,
            String message,
            MultipartFile file
    ) throws Exception {

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        String filePath = null;

        if (file != null && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String ext = "";

            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
            }

            if (!List.of(".pdf", ".jpg", ".jpeg", ".png").contains(ext)) {
                throw new RuntimeException("Invalid file type");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("File too large");
            }

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = UUID.randomUUID() + ext;
            Path savePath = Path.of(uploadDir, fileName);
            Files.write(savePath, file.getBytes());

            filePath = "/uploads/privacy/" + fileName;
        }

        Feedback feedback = new Feedback();
        feedback.setId(UUID.randomUUID());
        feedback.setUser(user);
        feedback.setModule(module);
        feedback.setMessage(message);
        feedback.setFilePath(filePath);
        feedback.setStatus("pending");
        feedback.setCreatedAt(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Feedback> getMyFeedback(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return feedbackRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Feedback updateStatus(UUID feedbackId, FeedbackStatusUpdate body) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        if (!body.getStatus().equals("approved") && !body.getStatus().equals("rejected")) {
            throw new RuntimeException("Invalid status");
        }

        feedback.setStatus(body.getStatus());
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(UUID feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        String filePath = feedback.getFilePath();

        feedbackRepository.delete(feedback);

        if (filePath != null) {
            try {
                String physicalPath = filePath.replace("/uploads", "data/uploads");
                Files.deleteIfExists(Path.of(physicalPath));
            } catch (Exception ignored) {}
        }
    }
}