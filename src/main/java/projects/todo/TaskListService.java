package projects.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projects.todo.api.TaskListCreateApiRequest;
import projects.todo.api.TaskListCreateApiResponse;
import projects.todo.api.TaskListSummary;
import projects.todo.converter.TaskListConverter;
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

}
