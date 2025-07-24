package projects.todo.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Schema(description = "Task creation request. Created task's completion status is set to false by default.")
public record TaskCreateApiRequest(
        String title,
        String description,

        @NotEmpty
        Set<Long> lists
) {
}
