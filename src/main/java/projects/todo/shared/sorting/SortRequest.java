package projects.todo.shared.sorting;

import lombok.Data;
import projects.todo.api.sorting.TaskSortParams;

@Data
public class SortRequest<T extends Enum<T>> {
    private T sortBy;
    private Direction direction;
}
