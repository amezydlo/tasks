package projects.todo.api.sorting;

import api.common.sorting.BaseSortConverter;
import org.springframework.stereotype.Component;

@Component
public class TaskSortConverter extends BaseSortConverter<TaskSortParams> {
    public TaskSortConverter() {
        super(TaskSortParams.class);
    }
}
