package projects.todo.api.response;

public record TaskSummaryApiResponse(
        String name,
        Boolean isDone
) {
}
