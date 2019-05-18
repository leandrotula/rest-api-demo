package com.app.ws.microservice.exceptions;

import com.app.ws.microservice.exceptions.dto.ErrorMessages;
import com.app.ws.microservice.exceptions.dto.Field;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class AppExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserException.class})
    public ResponseEntity<?> processUserException(final UserException ex, final WebRequest webRequest) {

        final ErrorMessages errorMessages = new ErrorMessages(new Date(), ex.getMessage(), null);

        return handleExceptionInternal(ex, errorMessages,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);

    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> processGeneralException(final Exception ex, final WebRequest webRequest) {

        final ErrorMessages errorMessages = new ErrorMessages(new Date(), ex.getMessage(), null);

        return handleExceptionInternal(ex, errorMessages,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        ErrorMessages error = processFieldErrors(fieldErrors);
        return new ResponseEntity(error, headers, status);
    }

    private ErrorMessages processFieldErrors(List<FieldError> fieldErrors) {
        ErrorMessages error = new ErrorMessages();
        error.setMessage("validation errors");
        error.setTimestamp(new Date());

        for (FieldError fieldError: fieldErrors) {
            error.getErrors().add(new Field(fieldError.getField(),
                    Objects.requireNonNull(fieldError.getDefaultMessage())));
        }
        return error;
    }
}
