package nl.wilron.auth.jwt.exceptions;

import org.springframework.web.client.HttpClientErrorException;

/**
 * Invalid Token Exception.
 *
 * @author Ronald Moetwil
 */
public class InvalidTokenException extends AbstractAPIException {

    public InvalidTokenException(HttpClientErrorException e) {
        super(e);
    }

    public InvalidTokenException(ExceptionMessage anExceptionMessage) {
        super(anExceptionMessage);
    }

    public InvalidTokenException(Integer statusCode, String statusText) {
        super(statusCode, statusText);
        this.getExceptionMessage().setId("TokenExpiredException");
    }
}
