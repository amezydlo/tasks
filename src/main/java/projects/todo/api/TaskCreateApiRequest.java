package projects.todo.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

@Schema(description = "Task creation request. Created task's completion status is set to false by default.")
public record TaskCreateApiRequest(
        @Schema(hidden = true)
        Optional<Long> id,
        String title,
        String description
) {
}
