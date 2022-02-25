package com.example.demo.task;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TaskPriority priority;

    @Column(name = "creationdate")
    private LocalDateTime creationDate;

    @OneToMany(
            mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubtaskEntity> subtasks = new ArrayList<>();

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

    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<SubtaskEntity> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(SubtaskEntity subtask) {
        subtasks.add(subtask);
        subtask.setTask(this);
    }

    public void removeSubtask(SubtaskEntity subtask) {
        subtasks.remove(subtask);
        subtask.setTask(null);
    }
}
