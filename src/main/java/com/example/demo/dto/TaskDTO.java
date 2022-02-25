package com.example.demo.dto;

import com.example.demo.task.SubtaskEntity;
import com.example.demo.task.TaskEntity;
import com.example.demo.task.TaskPriority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskDTO {

    private Long id;

    private String description;
    private boolean completed;
    private TaskPriority priority;
    private LocalDateTime creationDate;
    private List<SubtaskDTO> subtasks = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public List<SubtaskDTO> getSubtasks() {
        return subtasks;
    }

    public static TaskDTO mapEntityToDTO(TaskEntity taskEntity) {

        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setId(taskEntity.getId());
        taskDTO.setDescription(taskEntity.getDescription());
        taskDTO.setCompleted(taskEntity.isCompleted());
        taskDTO.setPriority(taskEntity.getPriority());
        taskDTO.setCreationDate(taskEntity.getCreationDate());
        for(SubtaskEntity subtaskEntity : taskEntity.getSubtasks()) {

            taskDTO.getSubtasks().add(SubtaskDTO.mapEntityToDTO(subtaskEntity));
        }

        return taskDTO;
    }

    public static TaskEntity mapDTOToEntity(TaskDTO taskDTO) {

        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setId(taskDTO.getId());

        if(Objects.nonNull(taskDTO.getDescription()) && !taskDTO.getDescription().trim().isEmpty()) {
            taskEntity.setDescription(taskDTO.getDescription());
        } else {
            taskEntity.setDescription("Default description.");
        }

        taskEntity.setCompleted(taskDTO.isCompleted());

        if(Objects.nonNull(taskDTO.getPriority())) {
            taskEntity.setPriority(taskDTO.getPriority());
        } else {
            taskEntity.setPriority(TaskPriority.LOW);
        }

        taskEntity.setCreationDate(LocalDateTime.now().withNano(0));

        if(Objects.nonNull(taskDTO.getSubtasks())) {
            for(SubtaskDTO subtaskDTO : taskDTO.getSubtasks()) {
                taskEntity.addSubtask(SubtaskDTO.mapDTOToEntity(subtaskDTO));
            }
        }

        return taskEntity;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}