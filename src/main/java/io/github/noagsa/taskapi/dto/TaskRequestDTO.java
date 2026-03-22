package io.github.noagsa.taskapi.dto;

import java.time.LocalDate;

public record TaskRequestDTO(String title, String description, boolean isDone, LocalDate dueDate) {
}
