package projects.todo.api.filter;

import io.swagger.v3.oas.annotations.media.Schema;

public record TaskFilter(
        @Schema(description = "Title of the task.")
        String title,
        @Schema(description = "Completion status of the task. Can be true or false")
        boolean completed
) {
}
