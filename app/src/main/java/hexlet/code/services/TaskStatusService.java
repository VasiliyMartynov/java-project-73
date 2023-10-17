package hexlet.code.services;

import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.models.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    public TaskStatus getTaskStatus(long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found"));
        return taskStatus;
    }

    public List<TaskStatus> getTaskStatuses() {
        try {
            return taskStatusRepository.findAll();
        } catch (NullPointerException e) {
            throw new NullPointerException("There are no any task status");
        }
    }

    public TaskStatus createTaskStatus(TaskStatus newTask) throws Exception {
        if (taskStatusRepository.findByName(newTask.getName()).isPresent()) {
            throw new Exception(
                    "There is an account with that email address:" + newTask.getName());
        }
        try {
            taskStatusRepository.save(newTask);
            taskStatusRepository.flush();
            return getTaskStatus(newTask.getId());
        } catch (Exception e) {
            throw new Exception("Something where wrong");
        }
    }

    public TaskStatus updateTaskStatus(long id, TaskStatus newTask) {
        try {
            TaskStatus taskStatus = taskStatusRepository.findById(id).orElseThrow();
            taskStatus.setName(newTask.getName());
            taskStatusRepository.save(taskStatus);
            return taskStatus;

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
