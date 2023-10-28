package hexlet.code.controllers;

import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskShowDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;
import hexlet.code.repository.TaskRepository;
import hexlet.code.services.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
@RequestMapping("${base-url}" + TaskController.TASK_CONTROLLER_PATH)
@AllArgsConstructor
public class TaskController {
    public static final String ID = "/{id}";
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    private TaskService taskService;
    private TaskRepository taskRepository;

    //GET Task BY ID
    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    TaskShowDTO getTask(@PathVariable long id) {
        return taskService.getTask(id);
    }

    //GET Tasks
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<TaskShowDTO> getTasks() {
        return taskService.getTasks();
    }

    //CREATE Task
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskShowDTO createTask(@Valid @RequestBody TaskCreateDTO taskDTO) throws Exception {
        return taskService.createTask(taskDTO);
    }

    //UPDATE TASK BY ID
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TaskShowDTO updateTask(@PathVariable long id, @RequestBody TaskUpdateDTO task) {
        return taskService.updateTask(id, task);
    }

    //DELETE TASK BY ID
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
