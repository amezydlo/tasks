package projects.todo;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.todo.api.TaskApiResponse;
import projects.todo.api.TaskCreateApiRequest;
import projects.todo.api.TaskSummaryApiResponse;
import projects.todo.api.TaskUpdateApiRequest;
import projects.todo.api.filter.TaskFilter;
import projects.todo.shared.pagination.Page;
import projects.todo.shared.pagination.PageApiRequest;
import projects.todo.shared.pagination.PageInfo;

import java.net.URI;

@RestController
@RequestMapping(TasksController.BASE_URL)
@AllArgsConstructor
public class TasksController {

    static final String BASE_URL = "/tasks";

    private final TaskService taskService;

    @GetMapping
    public Page<TaskSummaryApiResponse> getTaskSummaries(
            PageApiRequest pagination,
            TaskFilter taskFilter
    ) {
        var tasksSummariesPage = taskService.getTasksSummaries(pagination, taskFilter);
        var pageInfo = new PageInfo(
                tasksSummariesPage.getNumber(),
                tasksSummariesPage.getSize(),
                tasksSummariesPage.getTotalPages(),
                tasksSummariesPage.getTotalElements()
        );

        return Page.of(tasksSummariesPage.getContent(), pageInfo);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskApiResponse> getTaskById(@PathVariable Long taskId) {
        var taskDetails = taskService.getTaskDetails(taskId);
        return ResponseEntity.ok(taskDetails);
    }

    @PostMapping
    public ResponseEntity<TaskApiResponse> createTask(@RequestBody TaskCreateApiRequest request) {
        var createdTask = taskService.createTask(request);
        URI location = URI.create(BASE_URL + "/" + createdTask.id());
        return ResponseEntity.created(location).body(createdTask);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskApiResponse> updateTask(@PathVariable Long taskId, @RequestBody TaskUpdateApiRequest request) {
        var updatedTask = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.removeTask(taskId);
        return ResponseEntity.noContent().build();
    }


}
