package projects.todo.api.model;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Summary of the task. Contains basic fields, such as title, completion status.")
public record TaskSummaryApiResponse(
        Long id,
        String title,
        Boolean completed
) {
}
