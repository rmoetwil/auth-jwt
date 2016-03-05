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
 * @since 1.0
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
        String privateKey="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCWtlzkEIrUs1TXQHoDtgOkF6gl3KzwWjwt1C2s9TWiXhWjf8qI3Ecu9WEnBjIp4VvyxnDn/KMei+6cdv8/U37Mk9c7AyoWcwgoBA+tAz5yoV1XySLWRqNxY8xq800sDxaHefWQqd9draogAnrPMP7S5Dr8jo7JRX/nLQZO247qiGRV9nOVwDSAcQmFFnF2q46lnFNxkiKn4zNeCNEKUNdLRw553BuYaI2heGfi9pKTlPk37FoCFC/fRS2nLoRPHxCm/MS8UcyhzZumgiMCw57AKT+mQETu5NjDYOlN49ZPUqCD9AdSdyNKA15fszKi2cXT2PlSOdWqKZXHSv0XlpYxAgMBAAECggEAG6FdUFaUDUC/aiMg+3MwIbJQmnhloD76z3AZ256s8ZKMJwhzqjmwNzS3Vf81zSXKxKyK4I0Znq0K1taeHw8ArSLhj6kZ2znFogh0k0SH+14IDeMLJvuWVWB1hpoKaOxvNfEvCxGJqNQA35N0zm1uGl3LTqgYF2BNfaVshaKNBgQpEFCKs3OncRrq1tdcuV0ho4jiQC+GNTnsjidr2E5JMxI/oAao00bVbsfLF1NR0kc6TMjjkcoybfzDr0AoQG6honOYUDXL2Sh6F44XF8DNS844NK21SSMt58VeHc7NjM/088T/Q4gvjDFW1pyEee0M3Fq76CvN2fa61WuNEs9SAQKBgQDV4dM1QxCiRLs6uOMGuPaNGA5p2tW1Wj1Kj4NwkG822XmyR7SSdGDu3GCyTl646Z+z3YDbtAoq1jlkJo4guKJmfAjSMG52YWwif0B2BpqupTq6mbxtQD1Xp7OLW+Vpzj97P/f7SV23pGMhO2IbBKIbMV+AJ1SO5GEdf64OqOI10QKBgQC0ZAnIF5237g+YbZhFVRKrlmgj512KM1TdBb+brANjGE3z2eVhrp2lmKRbiSn9YetJfIckVV+liOdGQLspLHIuLq08Swcx4cWm62F+f0Mk4+gmFkvvOrxnwRNHgW6o8C2ta9RERiInkGK0VSTmea/Bcg2SE73LbtxU/h41vP+SYQKBgFIs/dVM+ysddUJOnwuKLVBwmbkaF1oiXcIz+bBaDiDo1dUlr79Fb6jWi2B2s/Yf7PhSdpvzRKTIwiw0lA8GYX820LzRVt+s9rsaYU33o57OZQ4k2zyuneL70AQPPsm0MLfOEB1ARv2z71JpWnw+/3mrMqIuelKcdKO81sdTI/bBAoGBAJZDvuIuyfKtG9CdenpGo4bLg3RlBkZY/Bgg8bt7Tlb9p/G5UJpOGoQW4tJdL90h9B0C3pAljccWg5DB2yYxH+vYdfYDibrDlyG0cUIuQIA6WOdhK1/cTZUgAC9QYh3X9DTB3dABm/HZFF+jjVlvhDhgCW3lg/QyJNen1wJsrXgBAoGBAKXXB9oqQpDfC7pQZon7h9DASySRlRhJOZ+oyc4Y5Tefy653i46ojahBLtBjaQ+ecXuBi/5kCoj9XGTpbOVaZdVXq71a8dHkSFzDwt1B3BKiyrVPom+wZlxIpnKM2bbo/nJYogmfwJExdR6ajWca7jlBm8XDdLYTCiCw6Hx8oxey";

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
