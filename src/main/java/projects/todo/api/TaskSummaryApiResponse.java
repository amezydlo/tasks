package projects.todo.api;

public record TaskSummaryApiResponse(
        String name,
        Boolean completed
) {
}
