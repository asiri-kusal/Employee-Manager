/**
 * The UserDetailsImpl Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.security.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lk.dialog.ms.BaseTestUtil;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserDetailsImplTest extends BaseTestUtil {

    private String username = "test user";
    private String password = "password";
    private String email = "test@dialog.lk";

    @Test
    public void testUserDetailsImplConstructor() {
        String userName = "testUser";
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, userName, email, password,
                                                          getGrantedAuthorities(Arrays.asList("ADMIN", "USER")));
        assertEquals("User name should be equal", userName, userDetails.getUsername());
    }

    @Test
    public void testUserDetailsImplBuild() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(getUser(username, email, password));
        assertEquals("User name should be equal", username, userDetails.getUsername());
    }

    @Test
    public void testUserDetailsImplAttributesTest() {
        List<String> roles = Arrays.asList("ADMIN", "USER");
        Long id = 1L;
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password,
                                                          getGrantedAuthorities(roles));
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        authorities.stream().forEach(authority -> {
            assertTrue("Role name should be match", roles.contains(((GrantedAuthority) authority).getAuthority()));
        });
        assertTrue("Account should be non locked", userDetails.isAccountNonLocked());
        assertTrue("User should be enable", userDetails.isEnabled());
        assertTrue("Account should be non expired", userDetails.isAccountNonExpired());
        assertTrue("Credentials should be non expired", userDetails.isCredentialsNonExpired());
        assertEquals("User name should be equal", username, userDetails.getUsername());
        assertEquals("User email should be equal", email, userDetails.getEmail());
        assertEquals("User password should be equal", password, userDetails.getPassword());
        assertEquals("User id should be equal", id, userDetails.getId());
        UserDetailsImpl userDetailsForCompare = UserDetailsImpl.build(getUser(username, email, password));
        assertFalse("UserDetails should not be matched", userDetails.equals(userDetailsForCompare));
    }
}
