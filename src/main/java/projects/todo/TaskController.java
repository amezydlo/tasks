package projects.todo;

import api.common.pagination.Page;
import api.common.pagination.PageInfo;
import api.common.pagination.PageRequest;
import api.common.sorting.SortRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.todo.api.*;
import projects.todo.api.filter.TaskFilter;
import projects.todo.api.sorting.TaskSortParams;

import java.net.URI;

import static projects.todo.TaskController.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
@AllArgsConstructor
public class TaskController {

    public static final String BASE_PATH = "/tasks";

    private final TaskService taskService;

    @Operation(
            operationId = "get-all-tasks",
            summary = "Retrieve a list of tasks with support for filtering, pagination, and sorting.",
            description = """
                     Returns a paginated list of tasks. You can filter tasks using various criteria,
                     sort them by selected fields, and control the number of results per page.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Paginated list of task summaries returned successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskPageResponse.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<TaskSummaryApiResponse>> getTaskSummaries(
            @ParameterObject TaskFilter taskFilter,
            @ParameterObject PageRequest pagination,
            @RequestParam(required = false) @Parameter(
                    description = "Sort query parameter.",
                    example = "title,asc") SortRequest<TaskSortParams> sort
    ) {
        var tasksSummariesPage = taskService.getTasksSummaries(pagination, taskFilter, sort);
        var pageInfo = new PageInfo(
                tasksSummariesPage.getNumber(),
                tasksSummariesPage.getSize(),
                tasksSummariesPage.getTotalPages(),
                tasksSummariesPage.getTotalElements()
        );

        return ResponseEntity.ok(Page.of(tasksSummariesPage.getContent(), pageInfo));
    }

    @Operation(
            operationId = "get-task-by-id",
            summary = "Retrieve details of a specific task by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task details retrieved successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskApiResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Task not found.")
            }
    )
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskApiResponse> getTaskById(@PathVariable Long taskId) {
        var taskDetails = taskService.getTaskDetails(taskId);
        return ResponseEntity.ok(taskDetails);
    }

    @Operation(
            operationId = "create-task",
            summary = "Create a new task.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Task created successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskApiResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input data.")
            }
    )
    @PostMapping
    public ResponseEntity<TaskApiResponse> createTask(@RequestBody TaskCreateApiRequest request) {
        var createdTask = taskService.createTask(request);
        URI location = URI.create(BASE_PATH + "/" + createdTask.id());
        return ResponseEntity.created(location).body(createdTask);
    }

    @Operation(
            operationId = "update-task",
            summary = "Update an existing task by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task updated successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskApiResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input data."),
                    @ApiResponse(responseCode = "404", description = "Task not found.")
            }
    )
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskApiResponse> updateTask(@PathVariable Long taskId, @RequestBody TaskUpdateApiRequest request) {
        var updatedTask = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(
            operationId = "delete-task",
            summary = "Delete a task by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task deleted successfully."),
            }
    )
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.removeTask(taskId);
        return ResponseEntity.noContent().build();
    }


}
