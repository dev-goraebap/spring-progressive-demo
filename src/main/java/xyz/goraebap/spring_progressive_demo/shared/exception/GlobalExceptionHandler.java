package xyz.goraebap.spring_progressive_demo.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "xyz.goraebap.spring_progressive_demo.app")
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        // 필드 에러 메시지를 "필드명: 메시지" 형태로 조합
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed - path: {}, errors: {}",
                request.getDescription(false), errorMessage);

        return createProblemDetail(HttpStatus.BAD_REQUEST, errorMessage, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(
            NotFoundException ex,
            WebRequest request
    ) {
        log.warn("Resource not found - path: {}, message: {}",
                request.getDescription(false), ex.getMessage());

        return createProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequestException(
            BadRequestException ex,
            WebRequest request
    ) {
        log.warn("Bad request - path: {}, message: {}",
                request.getDescription(false), ex.getMessage());

        return createProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(
            Exception ex,
            WebRequest request
    ) {
        log.error("Unexpected error occurred - path: {}",
                request.getDescription(false), ex);

        return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String detail, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }
}
