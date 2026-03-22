package io.github.noagsa.taskapi.mapper;

import io.github.noagsa.taskapi.dto.TaskRequestDTO;
import io.github.noagsa.taskapi.dto.TaskResponseDTO;
import io.github.noagsa.taskapi.model.Task;

public class TaskMapper {
    public static Task toEntity(TaskRequestDTO taskRequestDTO, long id) {
        return new Task(id,
                taskRequestDTO.title(),
                taskRequestDTO.description(),
                taskRequestDTO.isDone(),
                taskRequestDTO.dueDate());
    }

    public static TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isDone(),
                task.getDueDate(),
                task.getCreatedAt());
    }
}
