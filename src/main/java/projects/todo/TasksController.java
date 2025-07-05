package projects.todo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projects.todo.api.request.PaginationApiRequest;
import projects.todo.api.request.TaskFilter;
import projects.todo.api.response.PaginationApiResponse;
import projects.todo.api.response.TaskSummaryApiResponse;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TasksController {

    private final TaskService taskService;

    public PaginationApiResponse<TaskSummaryApiResponse> getAllTasks(
            PaginationApiRequest pagination,
            TaskFilter taskFilter
    ) {
        return taskService.getTasksSummaries(pagination, taskFilter); // TODO map here to the api response
    }
}
