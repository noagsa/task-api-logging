package io.github.noagsa.taskapi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    private long id;
    private String title;
    private String description;
    private boolean isDone;
    private LocalDate dueDate;
    private LocalDateTime createdAt;

    public Task(long id, String title, String description, boolean isDone, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
