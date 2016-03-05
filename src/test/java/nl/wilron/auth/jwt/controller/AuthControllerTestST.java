package nl.wilron.auth.jwt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import nl.wilron.auth.jwt.Application;
import nl.wilron.auth.jwt.domain.Credentials;
import nl.wilron.auth.jwt.utils.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.fail;

/**
 * Authentication controller System Test.
 *
 * @author Ronald Moetwil
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@IntegrationTest({"server.port:0"})
public class AuthControllerTestST {

    // TODO Add tests
    // - Refresh token happy flow
    // - Refresh expired token

    @Value("${local.server.port}")
    public int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testAuthenticateOK() {
        Credentials credentials = TestUtil.createAdminCredentials();

        this.testAuthenticate(credentials, HttpStatus.OK);
    }

    @Test
    public void testAuthenticateWithUnknownAccount() {
        Credentials credentials = TestUtil.createUnknwownUserCredentials();

        this.testAuthenticate(credentials, HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testAuthenticateWithWrongPassword() {
        Credentials credentials = TestUtil.createInvalidCredentials();

        this.testAuthenticate(credentials, HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testAuthenticateWithoutPassword() {
        Credentials credentials = TestUtil.createUserCredentials();
        credentials.setPassword(null);

        this.testAuthenticate(credentials, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testAuthenticateWithoutUsername() {
        Credentials credentials = TestUtil.createUserCredentials();
        credentials.setUsername(null);

        this.testAuthenticate(credentials, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRefreshMissingToken() {
        Credentials credentials = new Credentials();

        this.testTokenRefresh(credentials, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRefreshInvalidToken() {
        Credentials credentials = new Credentials();
        credentials.setToken("invalidToken");
        this.testTokenRefresh(credentials, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRefreshExpiredToken() {
        // Expired token created 11-02-2016 13:54
        String expiredToken = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb25hbGRvIiwidGVhbXMiOlsiT25ib2FyZGluZ19ERVYiXSwicm9sZXMiOlsiQ0RfT05CT0FSRElORyJdLCJleHAiOjE0NTUxOTU4OTEsImlhdCI6MTQ1NTE5NTI5MX0.ge7S6eTrOWGtPBpE1SjMjVWQdk7qDzBxUkaQlJdFQj3F49_gOXiuHXKhsYccDttNFg2qOtVjc5ZCcrjdcp0cV4I5rKqdqiOPWpbHHGVOriRWHcpX52K-MTP6CvXKrtin7koddXnEHJxGWrfFFW1uhUXyhA7oZEp4fY2pasBkHyRIDBCHPLP4RqZNfgzP0NJp421nNWkCa7gu-49S0ABqyeL40FlFasDEteMHn3U5kUVk1gdaGnkkXK8iompmXdIb0lUYUwog2Xcvg22UKy3Ey6Z_AoqWuhKK3-l1c-66TgvAIcNOo6hX2fl0vSdZ_yBdPz5fzdvMnm-ccgV5TB031g";
        Credentials credentials = new Credentials();
        credentials.setToken(expiredToken);
        this.testTokenRefresh(credentials, HttpStatus.UNAUTHORIZED);
    }

    private void testAuthenticate(Credentials credentials, HttpStatus httpStatus) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            given().
                    body(mapper.writeValueAsString(credentials)).
                    contentType("application/json").
                    expect().
                    statusCode(httpStatus.value()).
                    when().
                    post("/auth");
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    private void testTokenRefresh(Credentials credentials, HttpStatus httpStatus) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            given().
                    body(mapper.writeValueAsString(credentials)).
                    contentType("application/json").
                    expect().
                    statusCode(httpStatus.value()).
                    when().
                    post("/refresh");
        } catch (JsonProcessingException e) {
            fail();
        }
    }
}
