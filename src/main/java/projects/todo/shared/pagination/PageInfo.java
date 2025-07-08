package projects.todo.shared.pagination;

public record PageInfo(
        Integer page,
        Integer size,
        Integer totalPages,
        Long totalResults
) {
}
