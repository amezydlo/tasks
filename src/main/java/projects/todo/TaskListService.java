package projects.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projects.todo.api.TaskListCreateApiRequest;
import projects.todo.api.TaskListCreateApiResponse;
import projects.todo.api.TaskListSummary;
import projects.todo.converter.TaskListConverter;
import projects.todo.persistance.Task;
import projects.todo.persistance.TaskList;
import projects.todo.persistance.TaskListRepository;
import projects.todo.persistance.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskListService {
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final TaskListConverter taskListConverter;

    public List<TaskListSummary> getTaskListSummaries() {
        return taskListRepository.findAll().stream().map(taskListConverter::toTaskListSummary).toList();
    }

    public TaskListCreateApiResponse createList(TaskListCreateApiRequest request) {
        var createdList = new TaskList(
                request.name()
        );
        return taskListConverter.toTaskListCreateApiResponse(taskListRepository.save(createdList));
    }

    public void removeList(Long listId) {
        var listToRemove = taskListRepository.findById(listId).orElseThrow(); // TODO think about it
        var affectedTasks = taskRepository.findAllByListsContaining(listToRemove);

        // TODO maybe do sth like that:
        //  throw exception if task will be removed
        //  if api has a query param of force delete then skip this exception and delete anyway

        for (Task task : affectedTasks) {
            task.getLists().remove(listToRemove);

            if (task.getLists().isEmpty()) {
                taskRepository.delete(task);
            } else {
                taskRepository.save(task);
            }
        }
        taskListRepository.delete(listToRemove);
    }
}
