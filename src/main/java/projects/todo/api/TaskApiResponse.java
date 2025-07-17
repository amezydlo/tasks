package projects.todo.api;

public record TaskApiResponse(Long id, String title, String description, Boolean completed) {
}
