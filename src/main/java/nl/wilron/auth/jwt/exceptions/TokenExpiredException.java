package nl.wilron.auth.jwt.exceptions;

import org.springframework.web.client.HttpClientErrorException;

/**
 * Token Expired Exception.
 *
 * @author Ronald Moetwil
 */
public class TokenExpiredException extends AbstractAPIException {

    public TokenExpiredException(HttpClientErrorException e) {
        super(e);
    }

    public TokenExpiredException(ExceptionMessage anExceptionMessage) {
        super(anExceptionMessage);
    }

    public TokenExpiredException(Integer statusCode, String statusText) {
        super(statusCode, statusText);
        this.getExceptionMessage().setId("TokenExpiredException");
    }
}
