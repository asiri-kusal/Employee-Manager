/**
 * The AuthController Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.controllers;

import static lk.dialog.ms.models.ERole.ROLE_ADMIN;
import static lk.dialog.ms.models.ERole.ROLE_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lk.dialog.ms.BaseTestUtil;
import lk.dialog.ms.models.Role;
import lk.dialog.ms.models.User;
import lk.dialog.ms.payload.request.LoginRequest;
import lk.dialog.ms.payload.request.SignupRequest;
import lk.dialog.ms.payload.response.JwtResponse;
import lk.dialog.ms.payload.response.ResponseMessage;
import lk.dialog.ms.repository.RoleRepository;
import lk.dialog.ms.repository.UserRepository;
import lk.dialog.ms.security.JwtUtils;

@PrepareForTest(SecurityContextHolder.class)
@PowerMockIgnore({
                     "com.sun.crypto.*",
                     "javax.crypto.*",
                     "javax.management.*",
                     "javax.security.*"})
@RunWith(PowerMockRunner.class)
public class AuthControllerTest extends BaseTestUtil {

    private final static String DATABASE_ERROR = "Cannot connect to Database";

    private String username = "test user";
    private String password = "password";
    private String email = "test@dialog.lk";
    private Set<String> role;
    private AuthController authController;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        authController = new AuthController();
        mockHttpServletRequest(authController, true);
        role = new HashSet<>();
        role.add("admin");
    }

    @Test
    public void testAuthenticateUser() throws NoSuchFieldException, IllegalAccessException {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        Authentication authentication = getMockAuthentication(true);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test user");
        loginRequest.setPassword("password");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        setValueToPrivateField(authController, "authenticationManager", authenticationManager);
        JwtUtils jwtUtils = getMockJwtUtils();
        setValueToPrivateField(authController, "jwtUtils", jwtUtils);
        ResponseEntity responseEntity = authController.authenticateUser(loginRequest);
        assertEquals("Status code should be 200", HttpStatus.OK, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        JwtResponse jwtResponse = (JwtResponse) responseMessage.getMessage();
        assertTrue("Response entity should be instance of JwtResponse", jwtResponse instanceof JwtResponse);
    }

    @Test
    public void testRegisterUser() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, null, password, null, false);
        ResponseEntity responseEntity = authController.registerUser(getSignUpRequest(username, password, email, role));
        assertEquals("Status code should be 200", HttpStatus.CREATED, responseEntity.getStatusCode());
        String successMessage = "User registered successfully!";
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be", successMessage,
                     responseMessage.getMessage());
    }

    @Test
    public void testRegisterUserWithoutRole() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, null, password, null, false);
        ResponseEntity responseEntity = authController.registerUser(getSignUpRequest(username, password, email, null));
        assertEquals("Status code should be 200", HttpStatus.CREATED, responseEntity.getStatusCode());
        String successMessage = "User registered successfully!";
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be", successMessage,
                     responseMessage.getMessage());
    }

    @Test
    public void testRegisterUserWithExistEmail() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, null, password, email, false);
        ResponseEntity responseEntity = authController.registerUser(getSignUpRequest(username, password, email, null));
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        String successMessage = "Error: Email is already in use!";
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be", successMessage,
                     responseMessage.getMessage());
    }

    @Test
    public void testRegisterUserWithExistUser() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, null, false);
        ResponseEntity responseEntity = authController.registerUser(getSignUpRequest(username, password, email, null));
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        String successMessage = "Error: Username is already taken!";
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be", successMessage,
                     responseMessage.getMessage());
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterUserException() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, null, password, null, true);
        ResponseEntity responseEntity = authController.registerUser(getSignUpRequest(username, password, email, null));
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be dataBase error", DATABASE_ERROR,
                     responseMessage.getMessage());
    }

    @Test
    public void testDeleteUser() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, false);
        ResponseEntity responseEntity = authController.deleteUser(username);
        assertEquals("Status code should be 204", HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        String successMessage = "User delete successfully!";
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be", successMessage,
                     responseMessage.getMessage());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteUserNotFound() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, null, password, email, false);
        ResponseEntity responseEntity = authController.deleteUser(username);
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        StringBuilder builder = new StringBuilder("User not found :: ");
        builder.append(username);
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be", builder.toString(),
                     responseMessage.getMessage());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteUserException() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, true);
        ResponseEntity responseEntity = authController.deleteUser(username);
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be dataBase error", DATABASE_ERROR,
                     responseMessage.getMessage());
    }

    @Test
    public void testSearchUser() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, false);
        ResponseEntity responseEntity = authController.searchUser(username);
        assertEquals("Status code should be 200", HttpStatus.OK, responseEntity.getStatusCode());
        StringBuilder builder = new StringBuilder("User name should be equal to");
        builder.append(username);
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        User user = (User) responseMessage.getMessage();
        assertEquals(builder.toString(), user.getUsername(), username);
    }

    @Test(expected = RuntimeException.class)
    public void testSearchUserNotFound() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, null, password, email, false);
        ResponseEntity responseEntity = authController.searchUser(username);
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        StringBuilder builder = new StringBuilder("User not found :: ");
        builder.append(username);
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be", builder.toString(),
                     responseMessage.getMessage());
    }

    @Test(expected = RuntimeException.class)
    public void testSearchUserException() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, true);
        ResponseEntity responseEntity = authController.searchUser(username);
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be dataBase error", DATABASE_ERROR,
                     responseMessage.getMessage());
    }

    @Test
    public void testGetAllUsers() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, false);
        ResponseEntity responseEntity = authController.getAllUsers();
        assertEquals("Status code should be 200", HttpStatus.OK, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        List<User> users = (List<User>) responseMessage.getMessage();
        assertTrue("User List should not be empty", CollectionUtils.isNotEmpty(users));
    }

    @Test(expected = RuntimeException.class)
    public void testGetAllUsersException() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, true);
        ResponseEntity responseEntity = authController.getAllUsers();
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be dataBase error", DATABASE_ERROR,
                     responseMessage.getMessage());
    }


    @Test
    public void testGetAllRoles() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, false);
        ResponseEntity responseEntity = authController.getAllRoles();
        assertEquals("Status code should be 200", HttpStatus.OK, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        List<Role> users = (List<Role>) responseMessage.getMessage();
        assertTrue("Role List should not be empty", CollectionUtils.isNotEmpty(users));
    }

    @Test(expected = RuntimeException.class)
    public void testGetAllRolesException() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, true);
        ResponseEntity responseEntity = authController.getAllRoles();
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be dataBase error", DATABASE_ERROR,
                     responseMessage.getMessage());
    }

    @Test
    public void testUpdateUser() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, false);
        String updatedUser = "updated user";
        ResponseEntity responseEntity = authController
            .updateUser(username, getSignUpRequest(updatedUser, password, email, role));
        assertEquals("Status code should be 200", HttpStatus.OK, responseEntity.getStatusCode());
        StringBuilder builder = new StringBuilder("User name should be equal to");
        builder.append(username);
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        User user = (User) responseMessage.getMessage();
        assertEquals(builder.toString(), user.getUsername(), updatedUser);
    }

    @Test
    public void testUpdateUserWithoutRole() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, false);
        ResponseEntity responseEntity = authController
            .updateUser(username, getSignUpRequest(username, password, email, null));
        assertEquals("Status code should be 200", HttpStatus.OK, responseEntity.getStatusCode());
        StringBuilder builder = new StringBuilder("User name should be equal to");
        builder.append(username);
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        User user = (User) responseMessage.getMessage();
        assertEquals(builder.toString(), user.getUsername(), username);
        assertTrue("user roles should not be empty", CollectionUtils.isNotEmpty(user.getRoles()));
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateUserException() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, true);
        ResponseEntity responseEntity = authController
            .updateUser(username, getSignUpRequest(username, password, email, null));
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be dataBase error", DATABASE_ERROR,
                     responseMessage.getMessage());
    }

    @Test
    public void testUpdateRole() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, false);
        String roleName = ROLE_ADMIN.name();
        ResponseEntity responseEntity = authController.updateRole(roleName);
        assertEquals("Status code should be 200", HttpStatus.OK, responseEntity.getStatusCode());
        StringBuilder builder = new StringBuilder("RoleName name should be equal to");
        builder.append(roleName);
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        Role role = (Role) responseMessage.getMessage();
        assertEquals(builder.toString(), ROLE_ADMIN, role.getName());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateRoleException() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, true);
        ResponseEntity responseEntity = authController.updateRole(ROLE_ADMIN.name());
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be dataBase error", DATABASE_ERROR,
                     responseMessage.getMessage());
    }

    @Test
    public void testAddUserRole() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, false);
        String roleName = ROLE_ADMIN.name();
        ResponseEntity responseEntity = authController.addUserRole(roleName);
        assertEquals("Status code should be 200", HttpStatus.CREATED, responseEntity.getStatusCode());
        StringBuilder builder = new StringBuilder("Success message should be");
        String expected = "Role registered successfully!";
        builder.append(expected);
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals(builder.toString(), expected, responseMessage.getMessage());
    }

    @Test(expected = RuntimeException.class)
    public void testAddUserRoleException() throws NoSuchFieldException, IllegalAccessException {
        mockUserAndRoleRepositories(authController, username, password, email, true);
        ResponseEntity responseEntity = authController.addUserRole(ROLE_ADMIN.name());
        assertEquals("Status code should be 400", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseMessage responseMessage = (ResponseMessage) responseEntity.getBody();
        assertEquals("Response entity message should be dataBase error", DATABASE_ERROR,
                     responseMessage.getMessage());
    }

    private SignupRequest getSignUpRequest(String username, String password, String email, Set<String> role) {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword(password);
        signupRequest.setEmail(email);
        signupRequest.setRole(role);
        return signupRequest;
    }

    private void mockUserAndRoleRepositories(Object object, String username, String password, String email,
                                             boolean isThrowException)
        throws NoSuchFieldException, IllegalAccessException {
        User user = getUser(username, email, password);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userRepository.findByUsername(eq(username))).thenReturn(java.util.Optional.of(user));
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.existsByUsername(username)).thenReturn(true);

        RoleRepository roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByName(ROLE_ADMIN)).thenReturn(java.util.Optional.of(getAdminRole(true)));
        when(roleRepository.findByName(ROLE_USER)).thenReturn(java.util.Optional.of(getAdminRole(false)));

        if (isThrowException) {
            setUserRepositoryException(userRepository);
            setRoleRepositoryException(roleRepository);
        } else {
            when(userRepository.save(user)).thenReturn(user);
            doNothing().when(userRepository).delete(user);
            when(roleRepository.findAll()).thenReturn(Arrays.asList(getAdminRole(true)));
            doNothing().when(roleRepository).delete(getAdminRole(true));
        }

        setValueToPrivateField(object, "userRepository", userRepository);
        setValueToPrivateField(object, "roleRepository", roleRepository);

        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode(anyString())).thenReturn("asfaf123131312jmyuiy");
        setValueToPrivateField(object, "encoder", encoder);
    }

    private void setUserRepositoryException(UserRepository userRepository) {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException(DATABASE_ERROR));
        doThrow(new RuntimeException(DATABASE_ERROR)).when(userRepository).delete(any(User.class));
        when(userRepository.findByUsername(eq(username))).thenThrow(new RuntimeException(DATABASE_ERROR));
        when(userRepository.findAll()).thenThrow(new RuntimeException(DATABASE_ERROR));
    }

    private void setRoleRepositoryException(RoleRepository roleRepository) {
        when(roleRepository.findAll()).thenThrow(new RuntimeException(DATABASE_ERROR));
        doThrow(new RuntimeException(DATABASE_ERROR)).when(roleRepository).delete(any(Role.class));
        when(roleRepository.save(any(Role.class))).thenThrow(new RuntimeException(DATABASE_ERROR));
    }

}