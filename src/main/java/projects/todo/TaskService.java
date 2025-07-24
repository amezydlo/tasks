package projects.todo;

import api.common.pagination.PageRequest;
import api.common.sorting.SortRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.todo.api.TaskApiResponse;
import projects.todo.api.TaskCreateApiRequest;
import projects.todo.api.TaskSummaryApiResponse;
import projects.todo.api.TaskUpdateApiRequest;
import projects.todo.api.filter.TaskFilter;
import projects.todo.api.sorting.TaskSortParams;
import projects.todo.converter.TaskConverter;
import projects.todo.exception.NotFoundException;
import projects.todo.exception.OrphanedTasksException;
import projects.todo.persistance.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;
    private final TaskListRepository taskListRepository;

    @Transactional(readOnly = true)
    public Page<TaskSummaryApiResponse> getTasksSummaries(
            PageRequest pageRequest,
            TaskFilter taskFilter,
            SortRequest<TaskSortParams> sort) {
        var specification = new TaskSpecification(taskFilter);
        var page = pageRequest.toPageable(sort);

        return taskRepository.findAll(specification, page).map(taskConverter::toTaskSummaryApiResponse);
    }

    @Transactional(readOnly = true)
    public TaskApiResponse getTaskDetails(Long taskId) {
        return taskRepository.findById(taskId)
                .map(taskConverter::toTaskApiResponse)
                .orElseThrow(() -> new NotFoundException("Task with id: " + taskId + " not found"));
    }

    @Transactional
    public TaskApiResponse createTask(TaskCreateApiRequest taskCreateRequest) {
        var task = new Task(
                taskCreateRequest.title(),
                taskCreateRequest.description());

        addTaskToLists(task, taskCreateRequest.lists());
        validateMinimumLists(task);

        return taskConverter.toTaskApiResponse(taskRepository.save(task));
    }

    @Transactional
    public TaskApiResponse updateTask(Long taskId, TaskUpdateApiRequest request) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id: " + taskId + " not found"));

        var title = Optional.ofNullable(request.title());
        var description = Optional.ofNullable(request.description());
        var completed = Optional.ofNullable(request.completed());
        var taskLists = request.lists();

        title.ifPresent(task::setTitle);
        description.ifPresent(task::setDescription);
        completed.ifPresent(task::setCompleted);

        updateTaskLists(task, taskLists);
        validateMinimumLists(task);

        return taskConverter.toTaskApiResponse(taskRepository.save(task));
    }

    @Transactional
    public void removeTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }


    private void addTaskToLists(Task task, Set<Long> listIds) {
        var newLists = taskListRepository.findAllByIdIn(listIds);
        validateInputList(listIds, newLists);

        newLists.forEach(task::addList);
    }

    private void updateTaskLists(Task task, Set<Long> listIds) {
        var newLists = taskListRepository.findAllByIdIn(listIds);
        validateInputList(listIds, newLists);

        var currentList = task.getLists(); // można zastąpić przez metodę z repozytorium - chodzi o to, że currentlist nie ma w sobie listy tasków a remove potrzebuje getTasks()

        var listsToRemove = new HashSet<>(currentList);
        var listsToAdd = new HashSet<>(newLists);

        listsToRemove.removeAll(newLists);
        listsToAdd.removeAll(currentList);

        listsToRemove.forEach(task::removeList);
        listsToAdd.forEach(task::addList);
    }

    private void validateInputList(Set<Long> listIds, Set<TaskList> taskLists) {
        var foundIds = taskLists.stream().map(TaskList::getId).collect(Collectors.toSet());
        var missingIds = new HashSet<>(listIds);

        missingIds.removeAll(foundIds);
        if (!missingIds.isEmpty()) {
            throw new NotFoundException("Lists with ids " + missingIds + " not found");
        }
    }

    private void validateMinimumLists(Task task) {
        if (task.getLists().isEmpty()) {
            throw new OrphanedTasksException("Task must belong to at least " + 1 + " lists");
        }
    }
}
