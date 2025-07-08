package projects.todo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projects.todo.api.filter.TaskFilter;
import projects.todo.api.TaskSummaryApiResponse;
import projects.todo.shared.pagination.Page;
import projects.todo.shared.pagination.PageApiRequest;
import projects.todo.shared.pagination.PageInfo;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TasksController {

    private final TaskService taskService;

    @GetMapping
    public Page<TaskSummaryApiResponse> getAllTasks(
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
}
