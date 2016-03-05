package nl.wilron.auth.jwt.business;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import nl.wilron.auth.jwt.exceptions.InvalidTokenException;
import nl.wilron.auth.jwt.exceptions.TokenExpiredException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * JWTTokenGenerator.
 *
 * @author Ronald Moetwil
 */
public class JWTTokenGenerator implements TokenGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTTokenGenerator.class);

    private JWSSigner signer;

    // Expiration time in minutes.
    private long expirationTime;

    public JWTTokenGenerator(String thePrivateKey, long theExpirationTime) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Create RSA-signer with the private key
        signer = new RSASSASigner((RSAPrivateKey) getPrivateKey(thePrivateKey));
        expirationTime = theExpirationTime;
    }


    @Override
    public String generateToken(String subject, List<String> roles) {
        LOGGER.info("Generating Token for {}", subject);
        Date issueTime = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .issueTime(issueTime)
                .expirationTime(new Date(issueTime.getTime() + expirationTime * 1000 * 60))
                .claim("roles", roles)
                .build();

        LOGGER.debug(claimsSet.toString());

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256),
                claimsSet);

        // Compute the RSA signature
        try {
            signedJWT.sign(signer);
            String token = signedJWT.serialize();
            LOGGER.debug("Token: {}", token);
            return token;
        } catch (JOSEException e) {
            LOGGER.error("Failed to create Token.", e.getMessage());
        }

        return null;
    }

    @Override
    public String refreshToken(String token) {
        LOGGER.info("Refresh Token");

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            if (System.currentTimeMillis() < jwtClaimsSet.getExpirationTime().getTime()) {
                LOGGER.info("Refreshing Token for {}", jwtClaimsSet.getSubject());
                return this.generateToken(jwtClaimsSet.getSubject(), (List) jwtClaimsSet.getClaim("roles"));
            } else {
                throw new TokenExpiredException(HttpStatus.UNAUTHORIZED.value(), "Token expired");
            }
        } catch (ParseException e) {
            LOGGER.error("Failed to parse Token.", e.getMessage());
            throw new InvalidTokenException(HttpStatus.BAD_REQUEST.value(), "Failed to parse token");
        }
    }

    public static PrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedKey = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedKey = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);
    }
}
