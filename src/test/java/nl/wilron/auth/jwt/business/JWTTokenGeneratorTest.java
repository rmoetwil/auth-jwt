package nl.wilron.auth.jwt.business;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import nl.wilron.auth.jwt.exceptions.InvalidTokenException;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * JWTTokenGenerator unit test class.
 *
 * @author Ronald Moetwil
 */
public class JWTTokenGeneratorTest {

    private static final long EXPIRATION_TIME = 10;

    private RSAPublicKey publicKey;

    private PrivateKey privateKey;
    private TokenGenerator tokenGenerator;

    @Before
    public void setup() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair kp = this.generateKeyPair();
        publicKey = (RSAPublicKey) kp.getPublic();
        privateKey = kp.getPrivate();

        String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());

        System.out.println("jwt.privateKey=" + privateKeyString);
        System.out.println("jwt.publicKey=" + publicKeyString);

        tokenGenerator = new JWTTokenGenerator(Base64.encodeBase64String(privateKey.getEncoded()), EXPIRATION_TIME);
    }

    @Test
    public void testParsePublicKeyString() throws InvalidKeySpecException, NoSuchAlgorithmException {
        JWTTokenGenerator.getPublicKey(Base64.encodeBase64String(this.generateKeyPair().getPublic().getEncoded()));
    }

    @Test
    public void testParsePrivateKeyString() throws InvalidKeySpecException, NoSuchAlgorithmException {
        JWTTokenGenerator.getPrivateKey(Base64.encodeBase64String(this.generateKeyPair().getPrivate().getEncoded()));
    }

    /**
     * Code to generate a token with long expiration so we can use it in other API's.
     *
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testCreateTokenWithVeryLongExpiration() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String privateKey="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCjlE1qjeztNBx72Cd5uKtd49kkrUIzmW33nY2J0zvcZisUnrZKI3X/uRfodJWovPL2SiBuQPvcEnySooAA881GDpwrzVAsIhF8B123ukQBDO/s58gxLs0qLAejUlYLYr6GtvVdy9UX6sY9+9kivFXKn3p9m90d5506sfE8gEG8A0Skd/2pXewwCzFYqI5oeC5/c+aoVaiYnNQ6fmef4+SwTuwJ1SFIhL40UBXq3zZ8b1qXPMCPtJ31RN18gD3KGheKbYu5pot+QBZXHtRCgrsUZW2qUPPMYVNB53Op8eXBiy9lsuUxYOItMwlu7bUmILXDnNYrB1UIoy48x1mHJnRzAgMBAAECggEAfe9T9p/Tcv72muXb5lslG4CqERGOcRlDTyot6JkGcfT6X8KcxtEsLDc8z8rJ2YG2b6S7+QXwoQpee6m9cH5CYBgpZZOIkKGd4mXtYeB2MHTcVgBP4IWOkFQbgUKHWfaohiBfINoncWVfQCMflR130yoabI/NmRNSufh6p8B6WxrRPoiuJazZZ0cMmijj6DHVLIlB1+TOokZkCV/7MB38+WV+LPpXKw7yM/0U+JzgTdAd8AulQP2CDI54PSf+Z8CkD4lfL+SBm0cn6geKdsfI2kRyFKqATpE+hKOONEBAglp+iVQrd3mVFTROrC6iiBhlyViXHlmwO3TifGEeYWXVoQKBgQDaeF7UZsJ7thutCw0Lce+FB9jU4DiiY7Hx7J2mZGsrNFySKk8Djesl6o9jB4LQxBc6YnnndNFxN7KRcWQ99LUGPL+jC5aKSsw0dRRykQVG3uVxLIM5oiCma4QaW4ViVOIHPGpvEN35GE5iW88Ldg3hwm/+lZ7LWZpW27XqsWnMsQKBgQC/rgD/A4EGTP1Fxv3Beiupczrbi/mdzAvjPnqR9DGMZWvtXzwOy2tMyOgPzoNqXopZCtP7Gf2UKkP1SwC+tle9VfPaYB6zd0kvY8vcAafAUO7AU01ihkOCz8BKwrqbGyO51O6hXjLfgiVcGzyA4/gRwmuOruPdSh7d+pgQKOQMYwKBgQCPS6iXlJc34aihhcAWo7LBBPaaGj/Lc6zBy7tT6kXI3t1hGdMdt7Zm9kT6ZXDOUfASYYkn4Z2Gr0u2zjM72AfsgWgJH46sQodSWQECbFCTvZus8c9YvBjCT38kcqc4agifSmddVijiO6keJq3VDKfqTkh7KdQv+SOcmRvnRlro4QKBgAUxAkfl4WW5ShbMt+y4kVwqicqOdPlIIH5MH+e6FGl4Cb9JGAhSjBptSpO7BSYpX1fgCjO/1XVg26xWFrQTnja7kDo25j34GhxVeJ0B0nhL+Ntupibn+gYd+FhOA93Vk1ciJEBsm7YgAUEIX9Ah3IMmn+EibXt92W4rrlipstt/AoGACtzRVrjPQ0feMvkqEGlbo/0Qu/8YVTcQybx8Z08wMVzRMeNEwS0qwWj+dK931pjb7UnYD6tHbZk8HJc2k3+n/XHCwrEQW1zHDauBLDOqg+AtoTLOrZwJT3GbgGitsU9eepdfWVafekafr59N2VNsnBeDvJCaQQ7Elf+/MaJ6TqE=";

        TokenGenerator tokenGenerator = new JWTTokenGenerator(privateKey, 1000000);
        String subject = "ronaldo";
        List<String> roles = new ArrayList<>();
        roles.add("admin");

        tokenGenerator.generateToken(subject, roles);
        // Use logging to get the token
    }

    @Test
    public void testCreateAndParseToken() throws NoSuchAlgorithmException, JOSEException, ParseException {
        String subject = "ronaldo";
        List<String> roles = new ArrayList<>();
        roles.add("admin");

        SignedJWT signedJWT = SignedJWT.parse(tokenGenerator.generateToken(subject, roles));

        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        assertTrue(signedJWT.verify(verifier));

        // Retrieve / verify the JWT claims according to the app requirements
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

        assertEquals(subject, jwtClaimsSet.getSubject());
        assertNotNull("Issue Time", jwtClaimsSet.getIssueTime());
        assertNotNull("Expiration Time", jwtClaimsSet.getExpirationTime());
        assertEquals("Expiration Time", EXPIRATION_TIME * 60 * 1000, jwtClaimsSet.getExpirationTime().getTime() - jwtClaimsSet.getIssueTime().getTime());
        assertEquals("Roles", roles, jwtClaimsSet.getClaim("roles"));
    }

    @Test(expected = InvalidTokenException.class)
    public void refreshInvalidToken() {
        tokenGenerator.refreshToken("invalid token");
    }

    @Test
    public void refreshToken() throws ParseException, InterruptedException {
        String subject = "ronaldo";

        String originalToken = tokenGenerator.generateToken(subject, null);
        Thread.sleep(2000L);
        String refreshedToken = tokenGenerator.refreshToken(originalToken);

        assertNotEquals("Tokens", originalToken, refreshedToken);

        SignedJWT originalSignedJWT = SignedJWT.parse(originalToken);
        SignedJWT refreshedSignedJWT = SignedJWT.parse(refreshedToken);

        assertNotEquals("Issue Time", originalSignedJWT.getJWTClaimsSet().getIssueTime(), refreshedSignedJWT.getJWTClaimsSet().getIssueTime());
        assertNotEquals("Expiration Time", originalSignedJWT.getJWTClaimsSet().getExpirationTime(), refreshedSignedJWT.getJWTClaimsSet().getExpirationTime());
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // RSA signatures require a public and private RSA key pair, the public key
        // must be made known to the JWS recipient in order to verify the signatures
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(2048);

        return keyGenerator.genKeyPair();
    }
}
