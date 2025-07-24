package projects.todo.converter;

import org.springframework.stereotype.Component;
import projects.todo.api.model.TaskListCreateApiResponse;
import projects.todo.api.model.TaskListSummary;
import projects.todo.persistance.TaskList;

@Component
public class TaskListConverter {
    public TaskListSummary toTaskListSummary(TaskList taskList) {
        return new TaskListSummary(taskList.getId(), taskList.getName());
    }

    public TaskListCreateApiResponse toTaskListCreateApiResponse(TaskList taskList) {
        return new TaskListCreateApiResponse(taskList.getId(), taskList.getName());
    }
}
