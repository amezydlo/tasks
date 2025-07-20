package projects.todo;

import api.common.pagination.PageRequest;
import api.common.sorting.SortRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import projects.todo.api.TaskApiResponse;
import projects.todo.api.TaskCreateApiRequest;
import projects.todo.api.TaskSummaryApiResponse;
import projects.todo.api.TaskUpdateApiRequest;
import projects.todo.api.filter.TaskFilter;
import projects.todo.api.sorting.TaskSortParams;
import projects.todo.converter.TaskConverter;
import projects.todo.exception.NotFoundException;
import projects.todo.persistance.Task;
import projects.todo.persistance.TaskRepository;
import projects.todo.persistance.TaskSpecification;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;

    public Page<TaskSummaryApiResponse> getTasksSummaries(
            PageRequest pageRequest,
            TaskFilter taskFilter,
            SortRequest<TaskSortParams> sort) {
        var specification = new TaskSpecification(taskFilter);
        var page = pageRequest.toPageable(sort);

        return taskRepository.findAll(specification, page).map(taskConverter::toTaskSummaryApiResponse);
    }

    public TaskApiResponse getTaskDetails(Long taskId) {
        return taskRepository.findById(taskId)
                .map(taskConverter::toTaskApiResponse)
                .orElseThrow(() -> new NotFoundException("Task with id: " + taskId + " not found"));
    }

    public TaskApiResponse createTask(TaskCreateApiRequest taskCreateRequest) {
        var createdTask = new Task(
                taskCreateRequest.title(),
                taskCreateRequest.description());
        return taskConverter.toTaskApiResponse(taskRepository.save(createdTask));
    }

    public TaskApiResponse updateTask(Long taskId, TaskUpdateApiRequest request) {
        var taskToUpdate = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id: " + taskId + " not found"));

        request.title().ifPresent(taskToUpdate::setTitle);
        request.description().ifPresent(taskToUpdate::setDescription);
        request.completed().ifPresent(taskToUpdate::setCompleted);

        return taskConverter.toTaskApiResponse(taskRepository.save(taskToUpdate));
    }

    public void removeTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
