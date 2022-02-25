package com.example.demo.service;

import com.example.demo.dto.TaskDTO;
import com.example.demo.enums.AscDescEnum;
import com.example.demo.task.TaskPriority;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITaskService {

    ResponseEntity<List<TaskDTO>> getAllTasks(String orderBy, AscDescEnum order, TaskPriority priority, Boolean completed);

    ResponseEntity<TaskDTO> getTaskById(Long id);

    ResponseEntity createTask(TaskDTO taskDTO);

    ResponseEntity updateTask(Long id, TaskDTO takDTO);

    ResponseEntity deleteTask(Long id);
}
