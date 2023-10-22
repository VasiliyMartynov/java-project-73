package hexlet.code.services;

import hexlet.code.dto.TaskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.Mappers.TaskStatusMapper;
import hexlet.code.dto.TaskStatus.TaskStatusShowDTO;
import hexlet.code.dto.TaskStatus.TaskStatusUpdateDTO;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.models.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public TaskStatusShowDTO getTaskStatus(long id) {
        TaskStatus ts = taskStatusRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found")
        );
        return taskStatusMapper.INSTANCE.toTaskStatusShowDTO(ts);
    }

    public List<TaskStatusShowDTO> getTaskStatuses() {
        try {
            List<TaskStatus> taskStatuses = taskStatusRepository.findAll();
            return taskStatuses
                    .stream()
                    .map(ts -> taskStatusMapper.INSTANCE.toTaskStatusShowDTO(ts))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            throw new NullPointerException("There are no any task status");
        }
    }

    public TaskStatusShowDTO createTaskStatus(TaskStatusCreateDTO newTaskStatus) throws Exception {
        if (taskStatusRepository.findByName(newTaskStatus.getName()).isPresent()) {
            throw new Exception(
                    "There is an task status:" + newTaskStatus.getName());
        }
        try {
            var taskStatus = taskStatusMapper.INSTANCE.toTaskStatus(newTaskStatus);
            taskStatusRepository.save(taskStatus);
            taskStatusRepository.flush();
            return taskStatusMapper.INSTANCE.toTaskStatusShowDTO(taskStatus);
        } catch (Exception e) {
            throw new Exception("Something where wrong");
        }
    }

    public TaskStatusShowDTO updateTaskStatus(long id, TaskStatusUpdateDTO newTask) {
        try {
            TaskStatus taskStatus = taskStatusRepository.findById(id).orElseThrow();
            taskStatus.setName(newTask.getName());
            taskStatusRepository.save(taskStatus);
            return taskStatusMapper.INSTANCE.toTaskStatusShowDTO(taskStatus);

        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    public void deleteTaskStatus(long id) {
        try {
            Optional<TaskStatus> taskStatus = taskStatusRepository.findById(id);
            taskStatusRepository.deleteById(taskStatus.get().getId());
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }
}
