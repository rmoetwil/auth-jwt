package nl.wilron.auth.jwt.business;

import nl.wilron.auth.jwt.domain.Credentials;

import java.util.List;

/**
 * User Details Service interface.
 *
 * @author Ronald Moetwil
 */
public interface UserDetailsService {

    /**
     * Get the roles for the given user.
     *
     * @param username The username
     * @return List of roles
     */
    List<String> getRoles(String username);

    /**
     * Authenticate a given user.
     * @param credentials The users credentials
     */
    void authenticate(Credentials credentials);
}
