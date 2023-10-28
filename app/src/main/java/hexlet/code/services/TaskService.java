package hexlet.code.services;

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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskService {
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    @Autowired
    private static TaskMapper taskMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public TaskShowDTO getTask(long id) {
        Task t = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id + " not found")
        );
        return taskMapper.INSTANCE.showTask(t);
    }

    public List<TaskShowDTO> getTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            return tasks
                    .stream()
                    .map(t -> taskMapper.INSTANCE.showTask(t))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            throw new NullPointerException("There are no any user");
        }
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
            System.out.println("------------Create 1------------");
            System.out.println(newTask.getLabelIds().toString());
            task.setLabels(newTask.getLabelIds()
                            .stream()
                            .map(labelId -> labelRepository.findById(labelId.longValue()).orElseThrow())
                            .collect(Collectors.toSet()));
            System.out.println("------------Create 2------------");
            System.out.println(task.getLabels().toString());
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
            System.out.println("------------U1------------");
            System.out.println(newTask.getLabelIds().toString());
            System.out.println("------------U2------------");
            Set<Label> newLabels = newTask.getLabelIds()
                    .stream()
                    .map(l -> labelRepository.findById(l.longValue()).orElseThrow())
                    .collect(Collectors.toSet());
            System.out.println("------------U3------------");
            System.out.println(newTask.getLabelIds().toString());
            System.out.println("------------U4------------");
            task.setLabels(newLabels);
            taskRepository.save(task);
            return taskMapper.INSTANCE.showTask(task);

        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteTask(long id) {
        try {
            Optional<Task> task = taskRepository.findById(id);
            taskRepository.deleteById(task.get().getId());
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }
}
