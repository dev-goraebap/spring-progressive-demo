package xyz.goraebap.spring_progressive_demo.shared.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.time.LocalDateTime;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public Object handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        int statusCode = status != null ? Integer.parseInt(status.toString()) : 500;
        String errorMessage = message != null && !message.toString().isEmpty()
                ? message.toString()
                : HttpStatus.valueOf(statusCode).getReasonPhrase();
        String requestUri = uri != null ? uri.toString() : "/";

        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

        if (wantsJson(request)) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, errorMessage);
            problemDetail.setInstance(URI.create(requestUri));
            problemDetail.setProperty("timestamp", LocalDateTime.now());
            return ResponseEntity.status(httpStatus).body(problemDetail);
        }

        ModelAndView mav = new ModelAndView("pages/error/index");
        mav.addObject("status", statusCode);
        mav.addObject("error", httpStatus.getReasonPhrase());
        mav.addObject("message", errorMessage);
        mav.setStatus(httpStatus);
        return mav;
    }

    private boolean wantsJson(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE);
    }
}
