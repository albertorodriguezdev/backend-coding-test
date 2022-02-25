package com.example.demo.dto;

import com.example.demo.task.SubtaskEntity;
import com.example.demo.task.TaskPriority;

import java.util.Objects;

public class SubtaskDTO {

    private Long id;

    private String description;
    private boolean completed;
    private TaskPriority priority = TaskPriority.LOW;

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

    public static SubtaskDTO mapEntityToDTO(SubtaskEntity subtaskEntity) {

        SubtaskDTO subtaskDTO = new SubtaskDTO();

        subtaskDTO.setId(subtaskEntity.getId());
        subtaskDTO.setDescription(subtaskEntity.getDescription());
        subtaskDTO.setCompleted(subtaskEntity.isCompleted());
        subtaskDTO.setPriority(subtaskEntity.getPriority());

        return subtaskDTO;
    }

    public static SubtaskEntity mapDTOToEntity(SubtaskDTO subtaskDTO) {

        SubtaskEntity subtaskEntity = new SubtaskEntity();

        if(Objects.nonNull(subtaskDTO.getDescription()) && !subtaskDTO.getDescription().trim().isEmpty()) {
            subtaskEntity.setDescription(subtaskDTO.getDescription());
        } else {
            subtaskEntity.setDescription("Default description.");
        }

        subtaskEntity.setCompleted(subtaskDTO.isCompleted());

        if(Objects.nonNull(subtaskDTO.getPriority())) {
            subtaskEntity.setPriority(subtaskDTO.getPriority());
        } else {
            subtaskEntity.setPriority(TaskPriority.LOW);
        }

        return subtaskEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubtaskDTO that = (SubtaskDTO) o;
        return Objects.equals(id, that.id);
    }
}
