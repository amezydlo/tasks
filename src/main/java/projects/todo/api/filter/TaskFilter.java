package projects.todo.api.filter;

public record TaskFilter (
        String title,
        Boolean completed
) {
}
