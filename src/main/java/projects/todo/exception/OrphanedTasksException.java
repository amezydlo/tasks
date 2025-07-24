package projects.todo.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class OrphanedTasksException extends RuntimeException {
    private final List<String> violations;

    public OrphanedTasksException(String message, List<String> violations) {
        super(message);
        this.violations = violations;
    }
}
