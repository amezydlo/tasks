package projects.todo.converter;

import org.springframework.stereotype.Component;
import projects.todo.api.TaskSummaryApiResponse;
import projects.todo.persistance.Task;

@Component
public class TaskConverter {
    public TaskSummaryApiResponse toTaskSummaryApiResponse(Task task) {
        return new TaskSummaryApiResponse(
                task.getTitle(),
                task.getCompleted()
        );
    }

}
