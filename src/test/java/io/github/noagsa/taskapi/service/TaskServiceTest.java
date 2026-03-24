package io.github.noagsa.taskapi.service;

import io.github.noagsa.taskapi.dto.TaskRequestDTO;
import io.github.noagsa.taskapi.dto.TaskResponseDTO;
import io.github.noagsa.taskapi.exception.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
     void setUp() {
        taskService = new TaskService();
    }

    @Test
    void should_create_task_successfully() {
        // Given
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Test Task", "This is a test task", false, LocalDate.of(2026, 03, 23));

        // When
        TaskResponseDTO taskResponseDTO = taskService.save(taskRequestDTO);

        // Then
        assertNotNull(taskResponseDTO);
        assertEquals(1L, taskResponseDTO.id());
        assertEquals("Test Task", taskResponseDTO.title());
        assertEquals("This is a test task", taskResponseDTO.description());
        assertEquals(LocalDate.of(2026, 03, 23), taskResponseDTO.dueDate());
        assertFalse(taskResponseDTO.isDone());
    }

    @Test
    void should_retrieve_task_successfully(){
        // Given
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Test Task", "This is a test task", false, LocalDate.of(2026, 03, 23));
        taskService.save(taskRequestDTO);
        long id = 1L;

        // When
        TaskResponseDTO taskResponseDTO = taskService.get(id);

        // Then
        assertNotNull(taskResponseDTO);
        assertEquals(1L, taskResponseDTO.id());
        assertEquals("Test Task", taskResponseDTO.title());
        assertEquals("This is a test task", taskResponseDTO.description());
        assertEquals(LocalDate.of(2026, 03, 23), taskResponseDTO.dueDate());
        assertFalse(taskResponseDTO.isDone());
    }

    @Test
    void should_throw_TaskNotFoundException_when_task_not_found() {
        // Given
        long id = 1L;

        // When/Then
        Exception exception = assertThrows(TaskNotFoundException.class, () -> taskService.get(id));
        assertEquals("Task not found with id: " + id, exception.getMessage());
    }

    @Test
    void should_return_all_tasks_successfully() {
        // Given
        TaskRequestDTO firstTask = new TaskRequestDTO(
                "Test task 1",
                "This is a test task",
                false,
                LocalDate.of(2026, 3, 25));
        TaskRequestDTO secondTask = new TaskRequestDTO(
                "Test task 2",
                "This is another test task",
                false,
                LocalDate.of(2026, 3, 26));

        taskService.save(firstTask);
        taskService.save(secondTask);

        // When
        List<TaskResponseDTO> savedTasks = taskService.getAll();

        // Then
        assertEquals(2, savedTasks.size());
        assertEquals(1L, savedTasks.get(0).id());
        assertEquals("Test task 1", savedTasks.get(0).title());
        assertEquals("This is a test task", savedTasks.get(0).description());
        assertEquals(LocalDate.of(2026, 3, 25), savedTasks.get(0).dueDate());
        assertFalse(savedTasks.get(0).isDone());
        assertEquals(2L, savedTasks.get(1).id());
        assertEquals("Test task 2", savedTasks.get(1).title());
        assertEquals("This is another test task", savedTasks.get(1).description());
        assertEquals(LocalDate.of(2026, 3, 26), savedTasks.get(1).dueDate());
        assertFalse(savedTasks.get(1).isDone());
    }

    @Test
    void should_update_task_successfully() {
        // Given
        long id = 1L;
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Test Task",
                "This is a test task",
                false,
                LocalDate.of(2026, 3, 23));
        TaskRequestDTO updatedTaskRequestDTO = new TaskRequestDTO("Test Task updated",
                "This is a test task updated",
                false,
                LocalDate.of(2026, 3, 23));

        taskService.save(taskRequestDTO);

        // When
        TaskResponseDTO taskResponseDTO = taskService.update(id, updatedTaskRequestDTO);

        // Then
        assertNotNull(taskResponseDTO);
        assertEquals(1L, taskResponseDTO.id());
        assertEquals("Test Task updated", taskResponseDTO.title());
        assertEquals("This is a test task updated", taskResponseDTO.description());
        assertEquals(LocalDate.of(2026, 3, 23), taskResponseDTO.dueDate());
        assertFalse(taskResponseDTO.isDone());
    }

    @Test
    void should_throw_TaskNotFoundException_when_updating_non_existing_task() {
        // Given
        long id = 1L;
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Test Task",
                "This is a test task",
                false,
                LocalDate.of(2026, 03, 23));

        // When / Then
        Exception exception = assertThrows(TaskNotFoundException.class, () -> taskService.update(id, taskRequestDTO));
        assertEquals("Task not found with id: " + id, exception.getMessage());
    }

    @Test
    void should_delete_task_successfully() {
        // Given
        long id = 1L;
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Test Task",
                "This is a test task",
                false,
                LocalDate.of(2026, 03, 23));
        taskService.save(taskRequestDTO);

        // When
        taskService.delete(id);

        // Then
        assertTrue(taskService.getAll().isEmpty());
    }

    @Test
    void should_throw_TaskNotFoundException_when_deleting_non_existing_task() {
        // Given
        long id = 1L;

        // When / Then
        Exception exception = assertThrows(TaskNotFoundException.class, () -> taskService.delete(id));
        assertEquals("Task not found with id: " + id, exception.getMessage());
    }
}
