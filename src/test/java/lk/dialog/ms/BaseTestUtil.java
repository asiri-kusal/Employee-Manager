/**
 * The BaseTestUtil class provide common functionality for test classes.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms;

import static lk.dialog.ms.models.ERole.ROLE_ADMIN;
import static lk.dialog.ms.models.ERole.ROLE_USER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import lk.dialog.ms.models.ERole;
import lk.dialog.ms.models.Role;
import lk.dialog.ms.models.User;
import lk.dialog.ms.security.JwtUtils;
import lk.dialog.ms.security.services.UserDetailsImpl;

public class BaseTestUtil {

    public void setValueToPrivateField(Object objectInstance, String fieldName, Object value)
        throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = objectInstance.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(objectInstance, value);
    }

    public Authentication getMockAuthentication(boolean isUserDetailsImpl) {
        PowerMockito.mockStatic(SecurityContextHolder.class);
        SecurityContext context = PowerMockito.mock(SecurityContext.class);
        Authentication authentication = PowerMockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        Role userRole = new Role();
        userRole.setName(ERole.valueOf("ROLE_ADMIN"));
        userRole.setId(1);
        Set<Role> authority = new HashSet<>();
        authority.add(userRole);
        List<GrantedAuthority> authorities = authority.stream()
                                                      .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                                      .collect(Collectors.toList());
        UserDetailsImpl user = new UserDetailsImpl(10l, "test user", "email", "password", authorities);
        Object object;
        if (isUserDetailsImpl) {
            object = user;
        } else {
            object = "Anonymous";
        }
        when(authentication.getPrincipal()).thenReturn(object);
        context.setAuthentication(authentication);
        when(context.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext()).thenReturn(context);
        return authentication;
    }

    public JwtUtils getMockJwtUtils() throws NoSuchFieldException, IllegalAccessException {
        JwtUtils jwtUtils = new JwtUtils();
        setValueToPrivateField(jwtUtils, "jwtSecret", "bezKoderSecretKey");
        setValueToPrivateField(jwtUtils, "jwtExpirationMs", 86400000);
        return jwtUtils;
    }

    public User getUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(StringUtils.isNotBlank(username) ? username : "John");
        user.setPassword(password);
        user.setEmail(StringUtils.isNotBlank(email) ? email : "John@dialog.lk");
        user.setId(10l);
        user.setRoles(getRoles());
        return user;
    }

    public Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(getAdminRole(true));
        roles.add(getAdminRole(false));
        return roles;
    }

    public Role getAdminRole(boolean isAdmin) {
        Role role = new Role();
        if (isAdmin) {
            role.setId(1);
            role.setName(ROLE_ADMIN);
        } else {
            role = new Role();
            role.setId(1);
            role.setName(ROLE_USER);
        }
        return role;
    }

    public Set<GrantedAuthority> getGrantedAuthorities(List<String> authorityList) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        authorityList.stream().forEach(value -> {
            GrantedAuthority authority = mock(GrantedAuthority.class);
            when(authority.getAuthority()).thenReturn(value);
            grantedAuthorities.add(authority);
        });
        return grantedAuthorities;
    }

    public HttpServletRequest mockHttpServletRequest(Object object, boolean isFieldInject)
        throws NoSuchFieldException, IllegalAccessException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("dialog-trace-id")).thenReturn("dtrace-1234");
        if (isFieldInject) {
            setValueToPrivateField(object, "request", request);
        }
        return request;
    }

}