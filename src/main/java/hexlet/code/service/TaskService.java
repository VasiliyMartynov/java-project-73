package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskShowDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.dto.Mappers.TaskMapper;
import hexlet.code.models.Label;
import hexlet.code.models.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@AllArgsConstructor
public class TaskService {
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;
    private static TaskMapper taskMapper;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private LabelRepository labelRepository;
    private UserService userService;
    private TaskStatusRepository taskStatusRepository;

    public TaskShowDTO getTask(long id) {
        Task t = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found")
        );
        return taskMapper.INSTANCE.showTask(t);
    }

    public List<TaskShowDTO> getTasks(Predicate predicate) {
        return StreamSupport
                .stream(taskRepository.findAll(predicate).spliterator(), false)
                .map(t -> taskMapper.INSTANCE.showTask(t))
                .collect(Collectors.toList());
    }

    public TaskShowDTO createTask(TaskCreateDTO newTask) throws Exception {
        if (taskRepository.findByName(newTask.getName()).isPresent()) {
            throw new Exception(
                    "There is task already:" + newTask.getName());
        }
        try {
            Task task = new Task();
            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
            task.setAuthor(userService.getCurrentUser());
            task.setExecutor(userRepository.findById((long) newTask.getExecutorId()).orElseThrow());
            task.setTaskStatus(taskStatusRepository.findById((long) newTask.getTaskStatusId()).orElseThrow());
            task.setLabels(newTask.getLabelIds()
                            .stream()
                            .map(labelId -> labelRepository.findById(labelId.longValue()).orElseThrow())
                            .collect(Collectors.toSet()));
            taskRepository.save(task);

            taskRepository.flush();
            return taskMapper.INSTANCE.showTask(task);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Can't create task");
        }
    }

    public TaskShowDTO updateTask(long id, TaskUpdateDTO newTask) {
        try {
            Task task = taskRepository.findById(id).orElseThrow();
            task.setAuthor(userRepository.findById((long) newTask.getExecutorId()).orElseThrow());
            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
            task.setExecutor(userService.getCurrentUser());
            task.setTaskStatus(taskStatusRepository.findById((long) newTask.getTaskStatusId()).orElseThrow());
            Set<Label> newLabels = newTask.getLabelIds()
                    .stream()
                    .map(l -> labelRepository.findById(l.longValue()).orElseThrow())
                    .collect(Collectors.toSet());
            task.setLabels(newLabels);
            taskRepository.save(task);
            return taskMapper.INSTANCE.showTask(task);

        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    public void deleteTask(long serviceTaskId) {
        Task task = taskRepository.findById(serviceTaskId).orElseThrow(
                () -> new RuntimeException("task with " + serviceTaskId + " not found"));
        taskRepository.delete(task);
    }
}
