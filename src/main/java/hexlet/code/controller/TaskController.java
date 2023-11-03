package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskShowDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;
import hexlet.code.models.Task;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private static final String ONLY_AUTHOR_BY_ID = """
            @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";
    private TaskService taskService;

    @Operation(summary = "Get item by ID")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200", description = "Found item",
                content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskShowDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "not found", content = @Content),
        @ApiResponse(responseCode = "422", description = "Wrong data", content = @Content)
    })
    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    public TaskShowDTO getTask(
            @Parameter(description = "id of item to be find")
            @PathVariable long id) {
        return taskService.getTask(id);
    }

    @Operation(summary = "Get all items")
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "200", description = "List of  items",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskShowDTO.class)) }),
        @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TaskShowDTO> getTasks(
            @Parameter(description = "Predicate of items to be find")
            @QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskService.getTasks(predicate);
    }


    @Operation(summary = "Create item")
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskShowDTO.class)) }),
        @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
        @ApiResponse(
                    responseCode = "422", description = "Wrong data",
                    content = @Content)
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TaskShowDTO createTask(
            @Parameter(description = "DTO object to create")
            @Valid @RequestBody TaskCreateDTO taskDTO) throws Exception {
        return taskService.createTask(taskDTO);
    }

    @Operation(summary = "Update item by Id")
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "200", description = "updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskShowDTO.class)) }),
        @ApiResponse(
                    responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
        @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
        @ApiResponse(
                    responseCode = "404", description = "Not found",
                    content = @Content),

        @ApiResponse(
                    responseCode = "422", description = "Wrong data",
                    content = @Content)
    })
    @PutMapping(
            value = ID,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public TaskShowDTO updateTask(
            @Parameter(description = "id of item to be update")
            @PathVariable long id, @RequestBody TaskUpdateDTO task) {
        return taskService.updateTask(id, task);
    }

    @Operation(summary = "Delete item by Id")
    @ApiResponses(value = {
        @ApiResponse(
                    responseCode = "200", description = "Deleted",
                    content = @Content),
        @ApiResponse(
                    responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
        @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
        @ApiResponse(
                    responseCode = "404", description = "Not found",
                    content = @Content),
        @ApiResponse(
                    responseCode = "422", description = "Wrong data",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
