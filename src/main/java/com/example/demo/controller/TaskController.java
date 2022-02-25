package com.example.demo.controller;

import com.example.demo.dto.TaskDTO;
import com.example.demo.enums.AscDescEnum;
import com.example.demo.service.ITaskService;
import com.example.demo.task.TaskPriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final ITaskService taskService;

    @Autowired
    public TaskController(ITaskService taskService) {

        this.taskService = taskService;
    }

    /**
     * Returns all stored Tasks.
     * Can be sorted by priority or creation date and filtered by priority and/or completion.
     * @param orderBy Accepted values: "priority" or "creationDate". Indicates according to
     *                which parameter it has to order. The default value is "priority".
     * @param order Accepted values: "asc" or "desc". Indicates whether to sort
     *              the data ascending or descending. The default value is "desc".
     * @param priority Accepted values: "LOW", "MEDIUM" or "HIGH". Indicates according to which
     *                 priority it should filter.
     * @param completed Accepted values: true or false. Indicates if it should filter by
     *                  completed or not completed.
     * @return A ResponseEntity with a List of TaskDTO.
     */
    @GetMapping("/task")
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @RequestParam(defaultValue = "priority") String orderBy,
            @RequestParam(defaultValue = "DESC") AscDescEnum order,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) Boolean completed) {

        return taskService.getAllTasks(orderBy, order, priority, completed);
    }

    /**
     * Returns a single task searched by ID.
     * @param id The id of the task that has to be searched.
     * @return A ResponseEntity of type TaskDTO.
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {

        return taskService.getTaskById(id);
    }

    /**
     * Creates a task.
     * @param taskDTO A DTO with the params of the task that has to be created.
     * @return A ResponseEntity with a message that notifies if the task has
     * been created successfully.
     */
    @PostMapping("/task")
    public ResponseEntity createTask(@RequestBody TaskDTO taskDTO) {

        return taskService.createTask(taskDTO);
    }

    /**
     * Updates a task.
     * @param id A number that tells to the method which task has to be updated.
     * @param taskDTO A DTO with the params of the task that has to be updated.
     * @return A ResponseEntity with a message that notifies if the task has
     * been updated successfully.
     */
    @PutMapping("/task/{id}")
    public ResponseEntity updateTask(@PathVariable("id") Long id, @RequestBody TaskDTO taskDTO) {

        return taskService.updateTask(id, taskDTO);
    }

    /**
     * Deletes a task.
     * @param id A number that tells to the method which task has to be deleted.
     * @return A ResponseEntity with a message that notifies if the task has
     * been deleted successfully.
     */
    @DeleteMapping("/task/{id}")
    public ResponseEntity deleteTask(@PathVariable("id") Long id) {

        return taskService.deleteTask(id);
    }
}
