/**
 * The JwtUtilsTest class test all JWT utilities.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.security;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lk.dialog.ms.BaseTestUtil;
import lk.dialog.ms.security.services.UserDetailsImpl;

public class JwtUtilsTest extends BaseTestUtil {

    private static final String FAILURE_MESSAGE = "Token generation is fail";
    private static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9" +
                                        ".eyJzdWIiOiJhc2lyaSIsImlhdCI6MTU4OTI3OTI0NiwiZXhwIjoxNTg5MzY1NjQ2fQ" +
                                        ".NE29yHsT7U0MfPRFYQk41VIvuc8daVgB4_YLzaAK4Z3kkLlonYcLarm5Nr9E2PsQD6Ii3oK7zEHr5oxVniRGEw";
    private JwtUtils jwtUtils;
    private Authentication authentication;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        jwtUtils = getMockJwtUtils();
        authentication = mock(Authentication.class);
        mockAuthority(Arrays.asList("ADMIN", "USER"));
    }

    @Test
    public void testGenerateJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(FAILURE_MESSAGE, token);
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        assertNotNull(FAILURE_MESSAGE, userName);
    }

    @Test
    public void testValidateJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);
        boolean isValidateToken = jwtUtils.validateJwtToken(token);
        assertTrue(FAILURE_MESSAGE, isValidateToken);
    }

    @Test
    public void testExpiredJwtException() {
        boolean isValidateToken = jwtUtils.validateJwtToken(TOKEN);
        assertFalse(isValidateToken);
    }

    @Test
    public void testIllegalArgumentException() throws NoSuchFieldException, IllegalAccessException {
        setValueToPrivateField(jwtUtils, "jwtSecret", "abc");
        boolean isValidateToken = jwtUtils.validateJwtToken(TOKEN);
        assertFalse(isValidateToken);
    }

    @Test
    public void testValidateJwtTokenException() {
        boolean isValidateToken = jwtUtils.validateJwtToken(TOKEN);
        assertFalse(isValidateToken);
    }

    @Test
    public void testMalformedJwtException() {
        String token = "abc";
        boolean isValidateToken = jwtUtils.validateJwtToken(token);
        assertFalse(isValidateToken);
    }

    private void mockAuthority(List<String> authorityList) {
        mockAuthentication(getGrantedAuthorities(authorityList));
    }

    private void mockAuthentication(Set<GrantedAuthority> grantedAuthorities) {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "test@gmail.com", "testPassword",
                                                          grantedAuthorities);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

}