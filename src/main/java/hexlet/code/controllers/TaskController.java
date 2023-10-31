package hexlet.code.controllers;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskShowDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;
import hexlet.code.models.Task;
import hexlet.code.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
//import org.springframework.security.access.prepost.PreAuthorize;
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

    private static final Log LOGGER = LogFactory.getLog(TaskController.class);
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
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
    TaskShowDTO getTask(
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
    List<TaskShowDTO> getTasks(
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
//            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    TaskShowDTO createTask(
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
    TaskShowDTO updateTask(
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
    @DeleteMapping(ID)
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize(ONLY_OWNER_BY_ID)
    void deleteTask(
            @Parameter(description = "id of item to be delete")
            @PathVariable Long id) {
        System.out.println("TASK CONTROLLER");
        LOGGER.info(LogMessage.format("TASK CONTROLLER trying to delete task with ID: %s", id));
        taskService.deleteTask(id);
    }

}
