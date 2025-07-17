package projects.todo.api;

import java.util.Optional;

public record TaskUpdateApiRequest(
        Optional<String> title,
        Optional<String> description,
        Optional<Boolean> completed
) {
}
