package nl.wilron.auth.jwt.utils;

import nl.wilron.auth.jwt.business.StubUserDetailsService;
import nl.wilron.auth.jwt.domain.Credentials;

/**
 * Test Util class.
 *
 * @author Ronald Moetwil
 */
public class TestUtil {

    public static final String NPA_PREFIX = "AUTH_API_NPA_";
    public static final String CREDENTIALS_PREFIX = "USER_";

    public static Credentials createAdminCredentials() {
        Credentials credentials = new Credentials();
        credentials.setUsername(StubUserDetailsService.ADMIN_USER);
        credentials.setPassword(StubUserDetailsService.ADMIN_USER);
        return credentials;
    }

    public static Credentials createUserCredentials() {
        Credentials credentials = new Credentials();
        credentials.setUsername(StubUserDetailsService.REGULAR_USER);
        credentials.setPassword(StubUserDetailsService.REGULAR_USER);
        return credentials;
    }

    public static Credentials createUnknwownUserCredentials() {
        Credentials credentials = new Credentials();
        credentials.setUsername("unknown");
        credentials.setPassword("unknown");
        return credentials;
    }

    public static Credentials createInvalidCredentials() {
        Credentials credentials = new Credentials();
        credentials.setUsername(StubUserDetailsService.ADMIN_USER);
        credentials.setPassword("wrong");
        return credentials;
    }
}
