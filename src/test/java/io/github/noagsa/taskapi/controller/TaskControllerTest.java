package io.github.noagsa.taskapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.noagsa.taskapi.dto.TaskRequestDTO;
import io.github.noagsa.taskapi.dto.TaskResponseDTO;
import io.github.noagsa.taskapi.exception.TaskNotFoundException;
import io.github.noagsa.taskapi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @MockitoBean
    TaskService taskService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void should_create_task_successfully() throws Exception {
        TaskRequestDTO request = new TaskRequestDTO(
                "Task 1",
                "Description 1",
                false,
                LocalDate.of(2026, 3, 23)
        );
        TaskResponseDTO response = new TaskResponseDTO(
                1L,
                "Task 1",
                "Description 1",
                false,
                LocalDate.of(2026, 3, 23),
                LocalDateTime.of(2026, 3, 20, 10, 30)
        );

        when(taskService.save(any(TaskRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.isDone").value(false));

        verify(taskService).save(any(TaskRequestDTO.class));
    }

    @Test
    void should_get_all_tasks_successfully() throws Exception {
        List<TaskResponseDTO> tasks = List.of(
                new TaskResponseDTO(1L, "Task 1", "Description 1", false,
                        LocalDate.of(2026, 3, 23), LocalDateTime.of(2026, 3, 20, 10, 30)),
                new TaskResponseDTO(2L, "Task 2", "Description 2", true,
                        LocalDate.of(2026, 3, 24), LocalDateTime.of(2026, 3, 20, 10, 40))
        );

        when(taskService.getAll()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].isDone").value(true));

        verify(taskService).getAll();
    }

    @Test
    void should_get_task_by_id_successfully() throws Exception {
        TaskResponseDTO response = new TaskResponseDTO(
                1L,
                "Task 1",
                "Description 1",
                false,
                LocalDate.of(2026, 3, 23),
                LocalDateTime.of(2026, 3, 20, 10, 30)
        );

        when(taskService.get(1L)).thenReturn(response);

        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Task 1"));

        verify(taskService).get(1L);
    }

    @Test
    void should_update_task_successfully() throws Exception {
        TaskRequestDTO request = new TaskRequestDTO(
                "Task 1 updated",
                "Description updated",
                true,
                LocalDate.of(2026, 3, 25)
        );
        TaskResponseDTO response = new TaskResponseDTO(
                1L,
                "Task 1 updated",
                "Description updated",
                true,
                LocalDate.of(2026, 3, 25),
                LocalDateTime.of(2026, 3, 20, 10, 30)
        );

        when(taskService.update(eq(1L), any(TaskRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Task 1 updated"))
                .andExpect(jsonPath("$.isDone").value(true));

        verify(taskService).update(eq(1L), any(TaskRequestDTO.class));
    }

    @Test
    void should_delete_task_successfully() throws Exception {
        doNothing().when(taskService).delete(1L);

        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(taskService).delete(1L);
    }

    @Test
    void should_return_not_found_when_task_does_not_exist() throws Exception {
        when(taskService.get(999L)).thenThrow(new TaskNotFoundException("Task not found with id: 999"));

        mockMvc.perform(get("/api/tasks/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Task not found with id: 999"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/api/tasks/999"));

        verify(taskService).get(999L);
    }

    @Test
    void should_return_not_found_when_deleting_non_existing_task() throws Exception {
        doThrow(new TaskNotFoundException("Task not found with id: 999")).when(taskService).delete(999L);

        mockMvc.perform(delete("/api/tasks/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Task not found with id: 999"));

        verify(taskService).delete(999L);
    }

    @Test
    void should_return_bad_request_when_json_is_malformed() throws Exception {
        String malformedJson = "{\"title\":\"Task\",\"description\":\"desc\",\"isDone\":false,\"dueDate\": }";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.path").value("/api/tasks"));
    }
}
