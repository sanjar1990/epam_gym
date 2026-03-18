
package com.epam.gym.exceptions.handler;

import com.epam.gym.dto.ApiResponse;
import com.epam.gym.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @Nullable HttpHeaders headers,
            @Nullable HttpStatusCode status,
            @Nullable WebRequest request
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({APIException.class})
    public ResponseEntity<ApiResponse<String>> apiException(APIException exception) {
        log.error(exception.getMessage());
        // TODO:
        //  Is it 200 OK with a 400 inside? You API clients will go crazy :)
        return ResponseEntity.ok(ApiResponse.bad(exception.getMessage()));
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handler(UserNotFoundException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.ok(ApiResponse.bad(exception.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handler(RuntimeException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.ok(ApiResponse.bad(exception.getMessage()));
    }
}
