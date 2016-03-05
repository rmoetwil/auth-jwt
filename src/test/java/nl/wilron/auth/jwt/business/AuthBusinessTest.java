package nl.wilron.auth.jwt.business;

import nl.wilron.auth.jwt.domain.Credentials;
import nl.wilron.auth.jwt.utils.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Authentication and Authorisation Unit test class.
 *
 * @author Ronald Moetwil
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthBusinessTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenGenerator tokenGenerator;

    private AuthBusiness authBusiness;

    @Before
    public void setup() {
        authBusiness = new AuthBusinessImpl();
        ReflectionTestUtils.setField(authBusiness, "tokenGenerator", tokenGenerator);
        ReflectionTestUtils.setField(authBusiness, "userDetailsService", userDetailsService);
    }
    @Test
    public void testAuthenticate() {
        Credentials credentials = TestUtil.createAdminCredentials();
        List<String> roles = new ArrayList<>();
        roles.add("admin");

        when(userDetailsService.getRoles(credentials.getUsername())).thenReturn(roles);
        when(tokenGenerator.generateToken(credentials.getUsername(), roles)).thenReturn("aToken");
        credentials = authBusiness.authenticate(credentials);

        assertNotNull("Token", credentials.getToken());

        verify(userDetailsService).authenticate(credentials);
        verify(userDetailsService).getRoles(credentials.getUsername());
        verify(tokenGenerator).generateToken(anyString(), anyList());
        verifyNoMoreInteractions(userDetailsService, tokenGenerator);
    }

    @Test
    public void testRefreshToken() {
        Credentials credentials = TestUtil.createAdminCredentials();
        credentials.setToken("aToken");

        when(tokenGenerator.refreshToken("aToken")).thenReturn("refreshedToken");
        credentials = authBusiness.refresh(credentials);

        assertNotNull("Token", credentials.getToken());

        verify(tokenGenerator).refreshToken(anyString());
        verifyNoMoreInteractions(userDetailsService, tokenGenerator);
    }
}
