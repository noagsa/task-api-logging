package io.github.noagsa.taskapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.noagsa.taskapi.dto.TaskRequestDTO;
import io.github.noagsa.taskapi.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {
    @Autowired
    TaskService taskService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskService.clearTasks();
    }

    @Test
    void should_create_task_successfully() throws Exception {
        // Given
        TaskRequestDTO request = new TaskRequestDTO("Test task",
                "This is a test task",
                false,
                LocalDate.of(2026, 3, 26));

        // When / Then
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.description").value("This is a test task"))
                .andExpect(jsonPath("$.isDone").value(false));
    }

    @Test
    void should_get_all_tasks_successfully() throws Exception {
        TaskRequestDTO firstTask = new TaskRequestDTO("Test task 1", "This is the first test task", false,
                LocalDate.of(2026, 3, 23));
        TaskRequestDTO secondTask = new TaskRequestDTO("Test task 2", "This is the second test task", true,
                LocalDate.of(2026, 3, 24));

        taskService.save(firstTask);
        taskService.save(secondTask);

        // When / Then
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test task 1"))
                .andExpect(jsonPath("$[0].description").value("This is the first test task"))
                .andExpect(jsonPath("$[0].isDone").value(false))
                .andExpect(jsonPath("$[0].dueDate").value("2026-03-23"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Test task 2"))
                .andExpect(jsonPath("$[1].description").value("This is the second test task"))
                .andExpect(jsonPath("$[1].isDone").value(true))
                .andExpect(jsonPath("$[1].dueDate").value("2026-03-24"));
    }

    @Test
    void should_get_task_by_id_successfully() throws Exception {
        // Given
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Test task 1", "This is a test task", false,
                LocalDate.of(2026, 3, 23));
        taskService.save(taskRequestDTO);

        // When / Then
        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test task 1"))
                .andExpect(jsonPath("$.description").value("This is a test task"))
                .andExpect(jsonPath("$.isDone").value(false))
                .andExpect(jsonPath("$.dueDate").value("2026-03-23"));
    }

    @Test
    void should_update_task_successfully() throws Exception {
        // Given
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Test task 1", "This is the first test task", false,
                LocalDate.of(2026, 3, 23));
        taskService.save(taskRequestDTO);
        TaskRequestDTO taskRequestDTOtoUpdate = new TaskRequestDTO("Test task 1 update", "This is the updated task", false,
                LocalDate.of(2026, 3, 23));

        // When / Then
        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDTOtoUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test task 1 update"))
                .andExpect(jsonPath("$.description").value("This is the updated task"))
                .andExpect(jsonPath("$.isDone").value(false))
                .andExpect(jsonPath("$.dueDate").value("2026-03-23"));
    }

    @Test
    void should_delete_task_successfully() throws Exception {
        // Given
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Test task 1", "This is the first test task", false,
                LocalDate.of(2026, 3, 23));
        taskService.save(taskRequestDTO);

        // When / Then
        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_not_found_when_task_does_not_exist() throws Exception {

        mockMvc.perform(get("/api/tasks/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Task not found with id: 999"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/api/tasks/999"));
    }
}
