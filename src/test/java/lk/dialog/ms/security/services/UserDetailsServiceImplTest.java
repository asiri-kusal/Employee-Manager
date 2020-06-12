/**
 * The UserDetailsServiceImpl Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.security.services;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lk.dialog.ms.BaseTestUtil;
import lk.dialog.ms.repository.UserRepository;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsServiceImplTest extends BaseTestUtil {

    @Test
    public void TestLoadUserByUsername() throws NoSuchFieldException, IllegalAccessException {
        UserRepository userRepository = mock(UserRepository.class);
        String userName = "test User";
        String email = "test@dialog.lk";
        String password = "password";
        when(userRepository.findByUsername(eq(userName))).thenReturn(
            java.util.Optional.ofNullable(getUser(userName, email, password)));
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        setValueToPrivateField(userDetailsService, "userRepository", userRepository);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        assertNotNull("UserDetails object should not be null", userDetails);
    }
}