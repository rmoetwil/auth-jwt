package nl.wilron.auth.jwt.controller;

import nl.wilron.auth.jwt.exceptions.AbstractAPIException;
import nl.wilron.auth.jwt.exceptions.ExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * GlobalControllerExceptionHandler.
 * <p/>
 * Controller for global/generic exception handling.
 *
 * @author Ronald Moetwil
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);


    /**
     * Handles AbstractAPIException and subclasses.
     *
     * @return HTTP Status based on the error code.
     */
    @ExceptionHandler(value = {AbstractAPIException.class})
    public ResponseEntity<ExceptionMessage> handleLdapAPIException(AbstractAPIException ex) {
        return new ResponseEntity<>(ex.getExceptionMessage(), HttpStatus.valueOf(ex.getExceptionMessage().getCode()));
    }

    /**
     * Handles AuthenticationException
     *
     * @return HTTP-401 - UNAUTHORIZED
     */
    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ExceptionMessage> handleAuthenticationException(Exception ex) {
        LOGGER.debug(ex.getMessage());
        ExceptionMessage message = new ExceptionMessage();
        message.setMessage("LDAP AuthenticationException");

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    // Depending on the amount of control we want to have over the generic
    // Spring MVC exception handling we can either
    // overwrite handleExceptionInternal or any of the calling methods like e.g.
    // handleNoHandlerFoundException

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        LOGGER.debug(ex.getMessage());

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
        }

        // Can body object be null?
        if (body != null) {
            LOGGER.debug("body not null {}", body);
        }

        return new ResponseEntity<Object>(this.createExceptionMessage(ex), headers, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.debug(ex.getMessage());

        // Transform MethodArgumentNotValidException into an ErrorInfo response object.
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setId(ex.getClass().getSimpleName());
        exceptionMessage.setMessage(ex.getMessage());

        BindingResult bindingResult = ex.getBindingResult();
        for (Object object : bindingResult.getAllErrors()) {
            LOGGER.debug(object.toString());
            StringBuffer message = new StringBuffer();
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;
                message.append("Field ").append(fieldError.getField()).append(", ").append(fieldError.getDefaultMessage());
            } else if (object instanceof ObjectError) {
                ObjectError objectError = (ObjectError) object;
                message.append("Object ").append(objectError.getObjectName()).append(", ").append(objectError.getDefaultMessage());
            }
            exceptionMessage.setMessage(message.toString());
        }

        return new ResponseEntity<Object>(exceptionMessage, headers, status);
    }

    private ExceptionMessage createExceptionMessage(Exception ex) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setId(ex.getClass().getSimpleName());
        exceptionMessage.setMessage(ex.getMessage());

        return exceptionMessage;
    }
}
