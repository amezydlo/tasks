package projects.todo.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Basic info about list of tasks.")
public record TaskListSummary(
        Long id,
        String name
) {
}
