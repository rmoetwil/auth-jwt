package nl.wilron.auth.jwt.business;

import java.util.List;

/**
 * TokenGenerator interface.
 *
 * @author Ronald Moetwil
 */
public interface TokenGenerator {

    /**
     * Generate a token with the given data
     *
     * @param subject The subject.
     * @param roles The roles the subject has.
     *
     * @return Token
     */
    String generateToken(String subject, List<String> roles);

    /**
     * Refresh a token
     * @param token The token to refresh
     * @return New token.
     */
    String refreshToken(String token);
}
