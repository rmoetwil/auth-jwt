package nl.wilron.auth.jwt.config;

import nl.wilron.auth.jwt.business.JWTTokenGenerator;
import nl.wilron.auth.jwt.business.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Business Configuration class.
 *
 * @author Ronald Moetwil
 */
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"nl.wilron.auth.jwt.business"})
public class BusinessConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public TokenGenerator tokenGenerator() throws InvalidKeySpecException, NoSuchAlgorithmException {
        TokenGenerator tokenGenerator = new JWTTokenGenerator(env.getRequiredProperty("jwt.privateKey"), env.getProperty("jwt.tokenExpirationTime", Long.class));

        return tokenGenerator;
    }
}
