package projects.todo.exception;

import api.common.error.ApiError;
import api.common.error.ApiErrorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handle(NotFoundException ex) {
        var error = ApiErrorFactory.createNotFound(ex.getMessage(), List.of("Please double-check your request."));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrphanedTasksException.class)
    public ResponseEntity<ApiError> handle(OrphanedTasksException ex) {
        var error = ApiErrorFactory.createBadRequest(ex.getMessage(),
                ex.getViolations(),
                List.of("Please check your request.")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handle() {
        var error = ApiErrorFactory.createInternalServerError("Something went wrong.", List.of("Please try again later."));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
