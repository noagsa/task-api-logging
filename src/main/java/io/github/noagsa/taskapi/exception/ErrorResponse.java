package io.github.noagsa.taskapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorResponse(String message, HttpStatus httpStatus, Instant timestamp, String path) {
}
