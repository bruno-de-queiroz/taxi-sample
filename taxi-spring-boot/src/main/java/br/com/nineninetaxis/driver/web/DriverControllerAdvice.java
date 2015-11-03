package br.com.nineninetaxis.driver.web;

import br.com.nineninetaxis.driver.domain.Driver;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@ControllerAdvice
public class DriverControllerAdvice {

    public static final String CONTENT_TYPE = "application/vnd.error+json";
    public static final Integer FORM_VALIDATION_ERROR = 1;
    public static final Integer INTEGRITY_VALIDATION_ERROR = 2;

    @Autowired
    private MessageSource messageSource;

    private String interpolate(String message, Object... objects) {
        try {
            return messageSource.getMessage(message, objects, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return String.format(message, objects);
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(CONTENT_TYPE));
        return httpHeaders;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<VndErrors> validation(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<VndErrors.VndError> errors = bindingResult.getFieldErrors()
                .stream().map(f -> new VndErrors.VndError(
                        FORM_VALIDATION_ERROR.toString(),
                        String.format(f.getDefaultMessage(), f.getField())
                )).collect(Collectors.toList());

        errors.addAll(bindingResult.getGlobalErrors()
                .stream().map(f -> new VndErrors.VndError(
                        FORM_VALIDATION_ERROR.toString(),
                        f.getDefaultMessage()
                )).collect(Collectors.toList()));

        return new ResponseEntity<>(new VndErrors(errors), getHttpHeaders(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<VndErrors.VndError> validation(DataIntegrityViolationException e) {

        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e.getCause() instanceof ConstraintViolationException) {

            ConstraintViolationException cause = (ConstraintViolationException) e.getCause();

            if (cause.getConstraintName().toLowerCase().contains(Driver.CONSTRAINT_NAME)) {
                message = interpolate("error.duplicity");
                status = HttpStatus.CONFLICT;
            }
        }

        return new ResponseEntity<>(new VndErrors.VndError(INTEGRITY_VALIDATION_ERROR.toString(), message), getHttpHeaders(), status);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, HttpRequestMethodNotSupportedException.class, EntityNotFoundException.class})
    public void genericException(HttpServletResponse response, Exception e) throws IOException {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e instanceof EntityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }
        response.sendError(status.value());
    }
}
