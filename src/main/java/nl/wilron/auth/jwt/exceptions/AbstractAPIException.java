package nl.wilron.auth.jwt.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

/**
 * AbstractAPIException class.
 *
 * @author Ronald Moetwil
 */
public abstract class AbstractAPIException extends RuntimeException {

    private ExceptionMessage exceptionMessage;

    protected AbstractAPIException(HttpClientErrorException e) {
        ObjectMapper mapper = new ObjectMapper();
        ExceptionMessage exceptionMessage;
        try {
            exceptionMessage = mapper.readValue(e.getResponseBodyAsString(), ExceptionMessage.class);
        } catch (IOException e1) {
            exceptionMessage = new ExceptionMessage();
            exceptionMessage.setCode(e.getStatusCode().value());
            exceptionMessage.setMessage(e.getStatusText());
        }

        this.exceptionMessage = exceptionMessage;
    }

    protected AbstractAPIException(ExceptionMessage anExceptionMessage) {
        this.exceptionMessage = anExceptionMessage;
    }

    protected AbstractAPIException(Integer statusCode, String statusText) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setCode(statusCode);
        exceptionMessage.setMessage(statusText);
        this.exceptionMessage = exceptionMessage;
    }


    public ExceptionMessage getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(ExceptionMessage exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
