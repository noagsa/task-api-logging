package io.github.noagsa.taskapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskRequestDTO(@NotBlank(message = "must not be blank") String title,
                             String description,
                             boolean isDone,
                             @NotNull(message = "must not be null") LocalDate dueDate) {
}
