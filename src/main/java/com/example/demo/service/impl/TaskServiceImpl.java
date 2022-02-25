package com.example.demo.service.impl;

import com.example.demo.dto.SubtaskDTO;
import com.example.demo.dto.TaskDTO;
import com.example.demo.enums.AscDescEnum;
import com.example.demo.repository.ITaskRepository;
import com.example.demo.service.ITaskService;
import com.example.demo.task.SubtaskEntity;
import com.example.demo.task.TaskEntity;
import com.example.demo.task.TaskPriority;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class TaskServiceImpl implements ITaskService {

    EntityManager entityManager;
    ITaskRepository taskRepository;

    public TaskServiceImpl(EntityManager entityManager, ITaskRepository taskRepository) {

        this.entityManager = entityManager;
        this.taskRepository = taskRepository;
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
    @Override
    public ResponseEntity<List<TaskDTO>> getAllTasks(String orderBy, AscDescEnum order, TaskPriority priority, Boolean completed) {

        List<TaskEntity> taskEntityList;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskEntity> criteriaQuery = criteriaBuilder.createQuery(TaskEntity.class);
        Root<TaskEntity> from = criteriaQuery.from(TaskEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(priority)) {

            predicates.add(criteriaBuilder.equal(from.get("priority"), priority));
        }
        if(Objects.nonNull(completed)) {

            predicates.add(criteriaBuilder.equal(from.get("completed") , completed));
        }

        criteriaQuery.select(from).where(predicates.toArray(new Predicate[0]));

        if(order.equals(AscDescEnum.DESC)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(from.get(orderBy)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(from.get(orderBy)));
        }

        taskEntityList = entityManager.createQuery(criteriaQuery).getResultList();

        List<TaskDTO> taskDTOList = new ArrayList<>();
        for(TaskEntity taskEntity : taskEntityList) {

            taskDTOList.add(TaskDTO.mapEntityToDTO(taskEntity));
        }

        return new ResponseEntity<>(taskDTOList, HttpStatus.OK);
    }

    /**
     * It searches a single task by ID.
     * @param id ID of the task to search.
     * @return A ResponseEntity with the task that have been found
     */
    @Override
    public ResponseEntity<TaskDTO> getTaskById(Long id) {

        Optional optional = taskRepository.findById(id);

        TaskDTO taskDTO;
        if(optional.isPresent()) {
            taskDTO = TaskDTO.mapEntityToDTO((TaskEntity) optional.get());
        } else {
            throw new NoSuchElementException();
        }

        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    /**
     * Creates a task
     * @param taskDTO Params of the task that has to be created. The task can not be {@code null}.
     * @return A ResponseEntity that notifies if the task has been created.
     */
    @Override
    public ResponseEntity createTask(TaskDTO taskDTO) {

        taskRepository.save(TaskDTO.mapDTOToEntity(taskDTO));

        return new ResponseEntity<>("Task created.", HttpStatus.CREATED);
    }

    /**
     * Updates a task.
     * @param id The ID of the task that is attempted to update.
     * @param taskDTO The params of the task that is attempted to update.
     * @return A ResponseEntity that notifies if the task has been updated.
     */
    @Override
    public ResponseEntity updateTask(Long id, TaskDTO taskDTO) {

        Optional optional = taskRepository.findById(id);

        TaskEntity taskEntity;
        if(optional.isPresent()) {
            taskEntity  = (TaskEntity) optional.get();
        } else {
            throw new NoSuchElementException();
        }

        taskEntity.setDescription(taskDTO.getDescription());
        taskEntity.setCompleted(taskDTO.isCompleted());
        taskEntity.setPriority(taskDTO.getPriority());

        // Creates a copy of the Subtasks of the entity.
        List<SubtaskEntity> aux = new ArrayList<>(taskEntity.getSubtasks());

        /*
            Iterates through the copy of subtasks. If the list of subtasksDTO does not
            contain the copy of the subtask, it is removed from the list of subtasks
            of taskEntity. On the contrary, if it contains it, it is updated with the
            data from subtaskDTO.
        */
        for(SubtaskEntity subtask : aux) {

            SubtaskDTO checkSubtask = SubtaskDTO.mapEntityToDTO(subtask);
            if(!taskDTO.getSubtasks().contains(checkSubtask)) {

                taskEntity.removeSubtask(subtask);
            } else {

                SubtaskEntity subtaskEntity = taskEntity.getSubtasks().get(taskEntity.getSubtasks().indexOf(subtask));
                SubtaskDTO auxSubtaskDTO = taskDTO.getSubtasks().get(taskDTO.getSubtasks().indexOf(checkSubtask));
                subtaskEntity.setDescription(auxSubtaskDTO.getDescription());
                subtaskEntity.setCompleted(auxSubtaskDTO.isCompleted());
                subtaskEntity.setPriority(auxSubtaskDTO.getPriority());
            }
        }

        /*
            Iterates through the subtasksDTO and, if the subtaskDTO has no ID,
            it adds it to the list of subtasks of the taskEntity.
         */
        for(SubtaskDTO subtaskDTO : taskDTO.getSubtasks()) {

            if(Objects.isNull(subtaskDTO.getId())) {
                taskEntity.addSubtask(SubtaskDTO.mapDTOToEntity(subtaskDTO));
            }
        }

        taskRepository.save(taskEntity);

        return new ResponseEntity<>("Task updated.", HttpStatus.OK);
    }

    /**
     * Deletes a task.
     * @param id A number that tells to the method which task has to be deleted.
     * @return A ResponseEntity with a message that notifies if the task has
     * been deleted successfully.
     */
    @Override
    public ResponseEntity deleteTask(Long id) {

        Optional optional = taskRepository.findById(id);

        if(optional.isPresent()) {
            taskRepository.delete((TaskEntity) optional.get());
        } else {
            throw new NoSuchElementException();
        }

        return new ResponseEntity<>("Task with id " + id + " deleted.", HttpStatus.OK);

    }
}
