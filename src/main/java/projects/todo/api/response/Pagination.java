package projects.todo.api.response;

public record Pagination(
        Integer page,
        Integer size,
        Integer totalElements,
        Integer totalPages
) {
}
