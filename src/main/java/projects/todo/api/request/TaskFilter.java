package projects.todo.api.request;

public record TaskFilter (
        String name,
        Boolean completed
) {
}
