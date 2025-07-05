package projects.todo;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import projects.todo.api.request.PaginationApiRequest;
import projects.todo.api.request.TaskFilter;
import projects.todo.persistance.Task;
import projects.todo.persistance.TaskMatcher;
import projects.todo.persistance.TaskRepository;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;


    public void getTasksSummaries(PaginationApiRequest paginationApiRequest, TaskFilter taskFilter) {


        Specification<Task> spec = new TaskMatcher.Builder()
                .withTitle(taskFilter.name())
                .withCompleted(taskFilter.completed())
                .build()
                .asSpecification();

        var taskPage = taskRepository.findAll(Specification.allOf(), paginationApiRequest.toPageable());
    }
}
