package projects.todo.api;

import java.util.Optional;

public record TaskCreateApiRequest (
        Optional<Long> id,
        String title,
        String description
){
}
