package br.com.nineninetaxis.handlers;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Integer FORM_VALIDATION_ERROR = 1;
    private static final Integer INTEGRITY_VALIDATION_ERROR = 2;

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<VndErrors> integrity(DataIntegrityViolationException e) {
        return buildError(new VndErrors(INTEGRITY_VALIDATION_ERROR.toString(), interpolate("error.integrity")), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<VndErrors> integrity(MethodArgumentNotValidException e) {

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

        return buildError(new VndErrors(errors), HttpStatus.PRECONDITION_FAILED);
    }

    private ResponseEntity<VndErrors> buildError(VndErrors e, HttpStatus status) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/vnd.error+json"));
        return new ResponseEntity<>(e, httpHeaders, status);
    }

    private String interpolate(String message, Object... objects) {
        try {
            return messageSource.getMessage(message, objects, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return String.format(message, objects);
        }
    }
}
