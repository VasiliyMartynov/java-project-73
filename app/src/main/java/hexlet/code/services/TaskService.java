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
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TaskService {

    private static final Log logger = LogFactory.getLog(TaskService.class);
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
                logger.info("------------Create task ------------");
            task.setName(newTask.getName());
                logger.info("------------Set task name: " + newTask.getName().toString());

            task.setDescription(newTask.getDescription());
                logger.info("------------Set task desciprion: " + newTask.getDescription().toString());

            task.setAuthor(userService.getCurrentUser());
                logger.info("------------Set task author: " + userService.getCurrentUser().toString());

            task.setExecutor(userRepository.findById((long) newTask.getExecutorId()).orElseThrow());
                logger.info("------------Set task executor: "
                        + userRepository.findById((long) newTask.getExecutorId()).orElseThrow());

            task.setTaskStatus(taskStatusRepository.findById((long) newTask.getTaskStatusId()).orElseThrow());
                logger.info("------------Set task status: "
                    + userRepository.findById((long) newTask.getExecutorId()).orElseThrow());

            task.setLabels(newTask.getLabelIds()
                            .stream()
                            .map(labelId -> labelRepository.findById(labelId.longValue()).orElseThrow())
                            .collect(Collectors.toSet()));
                logger.info("------------Set task labels: " + newTask.getLabelIds().toString());
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
            logger.info("------------updateTask1------------");
            logger.info(newTask.getLabelIds().toString());
            logger.info("------------updateTask2------------");
            Set<Label> newLabels = newTask.getLabelIds()
                    .stream()
                    .map(l -> labelRepository.findById(l.longValue()).orElseThrow())
                    .collect(Collectors.toSet());
            logger.info("------------updateTask3------------");
            logger.info(newTask.getLabelIds().toString());
            logger.info("------------updateTask4------------");
            task.setLabels(newLabels);
            taskRepository.save(task);
            return taskMapper.INSTANCE.showTask(task);

        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(id + " not found");
        }
    }

    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteTask(long serviceTaskId) {
        System.out.println("deleteTask---------");
        logger.info(LogMessage.format("TASK SERVICE trying to delete task with ID %s", serviceTaskId));
        logger.info("trying to find task in repo");
        Task task = taskRepository.findById(serviceTaskId).orElseThrow(
                () -> new RuntimeException("task with " + serviceTaskId + " not found"));
        logger.info(LogMessage.format("-----------task is found %s, %s", task.getId(),task.getName()));
        logger.info("-----------trying to delete");
        taskRepository.deleteById(serviceTaskId);
        logger.info("-----------deleted");
    }
}
