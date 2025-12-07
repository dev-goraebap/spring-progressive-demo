package xyz.goraebap.spring_progressive_demo.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(basePackages = "xyz.goraebap.spring_progressive_demo.app")
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed - path: {}, errors: {}",
                request.getDescription(false), errorMessage);

        return createProblemDetail(HttpStatus.BAD_REQUEST, errorMessage, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public Object handleNotFoundException(
            NotFoundException ex,
            HttpServletRequest httpRequest,
            WebRequest request
    ) {
        log.warn("Resource not found - path: {}, message: {}",
                request.getDescription(false), ex.getMessage());

        if (wantsJson(httpRequest)) {
            return createProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        }
        return errorView(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public Object handleBadRequestException(
            BadRequestException ex,
            HttpServletRequest httpRequest,
            WebRequest request
    ) {
        log.warn("Bad request - path: {}, message: {}",
                request.getDescription(false), ex.getMessage());

        if (wantsJson(httpRequest)) {
            return createProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        }
        return errorView(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Object handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest httpRequest,
            WebRequest request
    ) {
        log.warn("Response status exception - path: {}, status: {}, reason: {}",
                request.getDescription(false), ex.getStatusCode(), ex.getReason());

        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String message = ex.getReason() != null ? ex.getReason() : status.getReasonPhrase();

        if (wantsJson(httpRequest)) {
            return createProblemDetail(status, message, request);
        }
        return errorView(status, message);
    }

    @ExceptionHandler(Exception.class)
    public Object handleGenericException(
            Exception ex,
            HttpServletRequest httpRequest,
            WebRequest request
    ) {
        log.error("Unexpected error occurred - path: {}",
                request.getDescription(false), ex);

        if (wantsJson(httpRequest)) {
            return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
        }
        return errorView(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    private boolean wantsJson(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE);
    }

    private ModelAndView errorView(HttpStatus status, String message) {
        ModelAndView mav = new ModelAndView("pages/error/index");
        mav.addObject("status", status.value());
        mav.addObject("error", status.getReasonPhrase());
        mav.addObject("message", message);
        mav.setStatus(status);
        return mav;
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String detail, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }
}
