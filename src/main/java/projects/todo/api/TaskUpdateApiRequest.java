package projects.todo.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

@Schema(description = "Task update request.")
public record TaskUpdateApiRequest(
        Optional<String> title,
        Optional<String> description,
        Optional<Boolean> completed
) {
}
