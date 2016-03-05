package nl.wilron.auth.jwt.business;

import nl.wilron.auth.jwt.domain.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Authentication and Authorisation Business Implementation.
 *
 * @author Ronald Moetwil
 */
@Service
public class AuthBusinessImpl implements AuthBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthBusinessImpl.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Override
    public Credentials authenticate(Credentials credentials) {
        LOGGER.info("Authenticate user {} and create token", credentials.getUsername());
        // Authenticate
        userDetailsService.authenticate(credentials);
        // Generate token
        List<String> roles = userDetailsService.getRoles(credentials.getUsername());

        credentials.setToken(tokenGenerator.generateToken(credentials.getUsername(), roles));

        // Clear password
        credentials.setPassword(null);
        return credentials;
    }

    @Override
    public Credentials refresh(Credentials credentials) {
        LOGGER.info("Refresh token {}", credentials.getToken());
        credentials.setToken(tokenGenerator.refreshToken(credentials.getToken()));
        return credentials;
    }
}
