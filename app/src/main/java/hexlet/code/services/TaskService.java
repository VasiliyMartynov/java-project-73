package hexlet.code.services;

import hexlet.code.dto.TaskCreateDTO;
//import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.models.Task;
import hexlet.code.models.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private static TaskMapper taskMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public TaskDTO getTask(long id) {
        Task t = taskRepository.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException(id + " not found")
        );
        return convertTaskToTaskDTO(t);
    }

    public List<TaskDTO> getTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            List<TaskDTO> tasksDTO = tasks
                    .stream()
                    .map(t -> taskMapper.INSTANCE.taskToTaskDTO(t))
                    .collect(Collectors.toList());
            return tasksDTO;
        } catch (NullPointerException e) {
            throw new NullPointerException("There are no any user");
        }
    }

    public TaskDTO createTask(TaskCreateDTO newTask) throws Exception {
        if (taskRepository.findByName(newTask.getName()).isPresent()) {
            throw new Exception(
                    "There is task:" + newTask.getName());
        }
        try {
            Task task = new Task();
            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
            task.setAuthor(userRepository.findById((long) newTask.getExecutorId()).orElseThrow());
            task.setExecutor(userRepository.findById((long) newTask.getExecutorId()).orElseThrow());
            task.setTaskStatus(taskStatusRepository.findById((long) newTask.getTaskStatusId()).orElseThrow());
            taskRepository.save(task);
            taskRepository.flush();
            return getTask(task.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Can't create task");
        }
    }

    public TaskDTO updateTask(long id, TaskCreateDTO newTask) {
        try {
            Task task = taskRepository.findById(id).orElseThrow();
            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
            task.setExecutor(userRepository.findById((long) newTask.getExecutorId()).orElseThrow());
            task.setTaskStatus(taskStatusRepository.findById((long) newTask.getTaskStatusId()).orElseThrow());
            taskRepository.save(task);
            return convertTaskToTaskDTO(task);

        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    public void deleteTask(long id) {
        try {
            Optional<Task> task = taskRepository.findById(id);
            taskRepository.deleteById(task.get().getId());
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    //CONVERT task to taskDTO
    public static TaskDTO convertTaskToTaskDTO(Task task) {
        return TaskMapper.INSTANCE.taskToTaskDTO(task);
    }
}