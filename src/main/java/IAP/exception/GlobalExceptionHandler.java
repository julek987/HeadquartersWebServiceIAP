package IAP.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RichErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        RichErrorResponse errorResponse = new RichErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid input data",
                errorMessage,
                generateRequestId()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RichErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        RichErrorResponse errorResponse = new RichErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                ex.getMessage(),
                generateRequestId()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<RichErrorResponse> handleInvalidData(InvalidDataException ex) {
        RichErrorResponse errorResponse = new RichErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid data",
                ex.getMessage(),
                generateRequestId()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
