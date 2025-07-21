package projects.todo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.todo.api.TaskListCreateApiRequest;
import projects.todo.api.TaskListCreateApiResponse;
import projects.todo.api.TaskListSummary;

import java.net.URI;
import java.util.List;

import static projects.todo.TaskListController.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
@RequiredArgsConstructor
public class TaskListController {
    public static final String BASE_PATH = "/lists";
    private final TaskListService taskListService;


    @Operation(
            operationId = "get-all-task-lists",
            summary = "Retrieve a list of task lists.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of task lists.")
            }
    )
    @GetMapping
    public ResponseEntity<List<TaskListSummary>> getTaskLists() {
        return ResponseEntity.ok(taskListService.getTaskListSummaries());
    }

    @Operation(
            operationId = "create-task-list",
            summary = "Create a new list for tasks.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "List created successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid input data.")
            }
    )
    @PostMapping
    public ResponseEntity<TaskListCreateApiResponse> createTaskList(@RequestBody TaskListCreateApiRequest request) {
        var createdList = taskListService.createList(request);
        URI location = URI.create(BASE_PATH + "/" + createdList.id());
        return ResponseEntity.created(location).body(createdList);
    }
}
