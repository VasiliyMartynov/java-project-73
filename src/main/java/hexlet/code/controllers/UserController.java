package hexlet.code.controllers;

import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.dto.User.UserShowDTO;
import hexlet.code.dto.User.UserUpdateDTO;
import hexlet.code.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;
import static hexlet.code.controllers.UserController.USER_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {

    public static final String USER_CONTROLLER_PATH = "/users";
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;
    private final UserService userService;
    public static final String ID = "/{id}";

    @Operation(summary = "Get item by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Found item",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserShowDTO.class)) }),
            @ApiResponse(
                    responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404", description = "not found",
                    content = @Content),
            @ApiResponse(
                    responseCode = "422", description = "Wrong data",
                    content = @Content)
    })
    @GetMapping(ID)
    @ResponseStatus(HttpStatus.OK)
    UserShowDTO getUser(
            @Parameter(description = "id of item to be find")
            @PathVariable long id) {
        return userService.getUser(id);
    }

    @Operation(summary = "Get all items")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "List of  items",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserShowDTO.class)) }),
    })
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    List<UserShowDTO> getUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Create item")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserShowDTO.class)) }),
            @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(
                    responseCode = "422", description = "Wrong data",
                    content = @Content)
    })
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    UserShowDTO createUser(
            @Parameter(description = "DTO object to create")
            @Valid @RequestBody UserCreateDTO u) throws Exception {
        return userService.createUser(u);
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
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteUser(
            @Parameter(description = "id of item to be delete")
            @PathVariable long id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "Update item by Id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserShowDTO.class)) }),
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
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public UserShowDTO updateUser(
            @Parameter(description = "id of item to be update")
            @PathVariable long id, @RequestBody UserUpdateDTO user) {
        return userService.updateUser(id, user);
    }
}
