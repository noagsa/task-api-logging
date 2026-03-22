package io.github.noagsa.taskapi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponseDTO(long id, String title, String description, boolean isDone, LocalDate dueDate, LocalDateTime createdAt) {
}
