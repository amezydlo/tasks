package projects.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import projects.todo.api.filter.TaskFilter;
import projects.todo.api.TaskSummaryApiResponse;
import projects.todo.converter.TaskConverter;
import projects.todo.persistance.TaskRepository;
import projects.todo.persistance.TaskSpecification;
import projects.todo.shared.pagination.PageApiRequest;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;

    public Page<TaskSummaryApiResponse> getTasksSummaries(PageApiRequest pageApiRequest, TaskFilter taskFilter) {
        var specification = new TaskSpecification(taskFilter);
        var page = pageApiRequest.toPageable();

        return taskRepository.findAll(specification, page).map(taskConverter::toTaskSummaryApiResponse);
    }
}
