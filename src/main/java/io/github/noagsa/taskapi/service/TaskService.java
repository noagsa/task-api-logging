package io.github.noagsa.taskapi.service;

import io.github.noagsa.taskapi.dto.TaskRequestDTO;
import io.github.noagsa.taskapi.dto.TaskResponseDTO;
import io.github.noagsa.taskapi.exception.TaskNotFoundException;
import io.github.noagsa.taskapi.mapper.TaskMapper;
import io.github.noagsa.taskapi.model.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private Map<Long, Task> tasks;
    private long nextId;

    public TaskService() {
        this.tasks = new HashMap<>();
        this.nextId = 1;
    }

    private Task findTaskOrThrow(long id) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        return tasks.get(id);
    }

    public TaskResponseDTO save(TaskRequestDTO taskRequestDTO) {
        Task task = TaskMapper.toEntity(taskRequestDTO, nextId);
        tasks.put(nextId, task);
        nextId++;

        return TaskMapper.toDTO(task);
    }

    public TaskResponseDTO get(long id) {
        return TaskMapper.toDTO(findTaskOrThrow(id));
    }

    public List<TaskResponseDTO> getAll() {
        return tasks.values().stream()
                .map(TaskMapper::toDTO)
                .toList();
    }

    public TaskResponseDTO update(long id, TaskRequestDTO taskRequestDTO) {
        Task task = findTaskOrThrow(id);
        task.setTitle(taskRequestDTO.title());
        task.setDescription(taskRequestDTO.description());
        task.setDone(taskRequestDTO.isDone());
        task.setDueDate(taskRequestDTO.dueDate());
        tasks.put(id, task);
        return TaskMapper.toDTO(task);
    }

    public void delete(long id) {
        findTaskOrThrow(id);
        tasks.remove(id);
    }
}
