package io.github.noagsa.taskapi.controller;

import io.github.noagsa.taskapi.dto.TaskRequestDTO;
import io.github.noagsa.taskapi.dto.TaskResponseDTO;
import io.github.noagsa.taskapi.service.TaskService;
import jakarta.validation.Valid;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> addTask(@RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO createdTask = taskService.save(taskRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok().body(taskService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable long id) {
        return ResponseEntity.ok().body(taskService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable long id, @RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.ok().body(taskService.update(id, taskRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
