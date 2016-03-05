package nl.wilron.auth.jwt.business;


import nl.wilron.auth.jwt.domain.Credentials;

/**
 * Authentication and Authorisation Business interface.
 *
 * @author Ronald Moetwil
 */
public interface AuthBusiness {


    /**
     * Authenticate with the given credentials
     * @param credentials The credentials that contains username and password
     *
     * @return Credentials instance with a valid token.
     */
    Credentials authenticate(Credentials credentials);

    /**
     * Refresh a given token if it is not expired.
     * @param credentials
     * @return Credentials with new token.
     */
    Credentials refresh(Credentials credentials);
}
