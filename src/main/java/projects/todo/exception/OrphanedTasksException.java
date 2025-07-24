package projects.todo.exception;

public class OrphanedTasksException extends RuntimeException {
    public OrphanedTasksException(String message) {
        super(message);
    }
}
