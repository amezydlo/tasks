package projects.todo.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Task details. Includes description and additional data.")
public record TaskApiResponse(Long id, String title, String description, Boolean completed) {
}
