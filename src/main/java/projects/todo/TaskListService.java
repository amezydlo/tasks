package projects.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.todo.api.model.TaskListCreateApiRequest;
import projects.todo.api.model.TaskListCreateApiResponse;
import projects.todo.api.model.TaskListSummary;
import projects.todo.converter.TaskListConverter;
import projects.todo.exception.NotFoundException;
import projects.todo.exception.OrphanedTasksException;
import projects.todo.persistance.Task;
import projects.todo.persistance.TaskList;
import projects.todo.persistance.TaskListRepository;
import projects.todo.persistance.TaskRepository;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskListService {
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final TaskListConverter taskListConverter;

    @Transactional(readOnly = true)
    public List<TaskListSummary> getTaskListSummaries() {
        return taskListRepository.findAll().stream().map(taskListConverter::toTaskListSummary).toList();
    }

    public TaskListCreateApiResponse createList(TaskListCreateApiRequest request) {
        var createdList = new TaskList(
                request.name()
        );
        return taskListConverter.toTaskListCreateApiResponse(taskListRepository.save(createdList));
    }

    public TaskListCreateApiResponse updateTaskList(Long taskListId, TaskListCreateApiRequest request) {
        var listToUpdate = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new NotFoundException("Task list with id: " + taskListId + " not found"));

        listToUpdate.setName(request.name());
        return taskListConverter.toTaskListCreateApiResponse(taskListRepository.save(listToUpdate));
    }

    @Transactional
    public void deleteTaskList(Long listId, boolean force) {
        var orphanedTasks = taskRepository.findTasksToBeAbandoned(listId);

        if (!force && !orphanedTasks.isEmpty()) {
            throw new OrphanedTasksException("Trying to remove not empty list.",
                    List.of("List: " + listId + " is the only list for tasks with ids: " + orphanedTasks.stream().map(Task::getId).toList()));
        }

        // force remove orphans
        if (force && !orphanedTasks.isEmpty()) {
            taskRepository.deleteAllInBatch(orphanedTasks);
        }

        var listToDelete = taskListRepository.findByIdWithTasksAndLists(listId).orElseThrow(() -> new NotFoundException("Task list with id: " + listId + " not found"));

        for (Task task : new HashSet<>(listToDelete.getTasks())) {
            task.removeList(listToDelete);
        }

        taskListRepository.delete(listToDelete);
    }
}
