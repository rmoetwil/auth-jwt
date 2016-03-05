package nl.wilron.auth.jwt.business;

import nl.wilron.auth.jwt.domain.Credentials;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Stub User Details Service for demo purposes.
 *
 * @author Ronald Moetwil
 */
@Service
public class StubUserDetailsService implements UserDetailsService {

    public static final String ADMIN_USER = "admin";
    public static final String REGULAR_USER = "user";

    @Override
    public List<String> getRoles(String username) {
        List<String> roles = new ArrayList<>();

        if (username.equals(ADMIN_USER)) {
           roles.add("admin");
        } else if (username.equals(REGULAR_USER)) {
            roles.add("user");
        }

        return roles;
    }

    @Override
    public void authenticate(Credentials credentials) {
        if (credentials.getUsername().equals(ADMIN_USER) || credentials.getUsername().equals(REGULAR_USER)) {
            // password = username
            if (!credentials.getUsername().equals(credentials.getPassword())) {
                throw new BadCredentialsException("Unauthorized");
            }
        } else {
            throw new BadCredentialsException("Unauthorized");
        }
    }
}
