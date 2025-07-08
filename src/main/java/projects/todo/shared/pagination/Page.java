package projects.todo.shared.pagination;

import java.util.List;

/*
 *
 * Used in responses
 *
 * */

public record Page<T>(
        List<T> results,
        PageInfo pagination
) {
    public static <T> Page<T> of(List<T> results, PageInfo pagination) {
        return new Page<>(results, pagination);
    }
}
