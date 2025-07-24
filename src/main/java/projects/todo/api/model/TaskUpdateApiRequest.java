package projects.todo.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Task update request.")
public record TaskUpdateApiRequest(
        String title,
        String description,
        Boolean completed,
        Set<Long> lists
) {
}
