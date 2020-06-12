/**
 * The AuthController class contain all api related for spring security.
 * such as CRUD operation to user and token generation
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.dialog.ms.models.ERole;
import lk.dialog.ms.models.Role;
import lk.dialog.ms.models.User;
import lk.dialog.ms.payload.request.LoginRequest;
import lk.dialog.ms.payload.request.SignupRequest;
import lk.dialog.ms.payload.response.JwtResponse;
import lk.dialog.ms.payload.response.ResponseMessage;
import lk.dialog.ms.repository.RoleRepository;
import lk.dialog.ms.repository.UserRepository;
import lk.dialog.ms.security.JwtUtils;
import lk.dialog.ms.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);
    private static final String ROLE_NOT_FOUND_MESSAGE = "Error: Role is not found.";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found :: ";
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private HttpServletRequest request;

    /**
     * Signin(register) new @{@link User}
     *
     * @param @{@link LoginRequest}
     * @return @{@link ResponseEntity}
     */
    @PostMapping("/signin")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Token generate success"),
                           @ApiResponse(code = 400, message = "Exception occur when processing")})
    public ResponseEntity<ResponseMessage> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        long lStartTime = System.currentTimeMillis();
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                                        .map(item -> item.getAuthority())
                                        .collect(Collectors.toList());
        long lEndTime = System.currentTimeMillis();
        long output = lEndTime - lStartTime;
        LOGGER.info("elapsed time {}", output);
        return getResponseMessage(new JwtResponse(jwt,
                                                  userDetails.getId(),
                                                  userDetails.getUsername(),
                                                  userDetails.getEmail(),
                                                  roles), HttpStatus.OK);
    }

    /**
     * Generate Token for specific @{@link User}
     *
     * @param signUpRequest @{@link SignupRequest}
     * @return @{@link ResponseEntity}
     */
    @Caching(evict = {
        @CacheEvict(value = "getUser", allEntries = true),
        @CacheEvict(value = "getAllusers", allEntries = true)})
    @PostMapping("/signup")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "User registration success"),
                           @ApiResponse(code = 400, message = "Exception occur when processing")})
    public ResponseEntity<ResponseMessage> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        boolean isExistsByUsername = userRepository.existsByUsername(signUpRequest.getUsername());
        boolean isExistsByEmail = userRepository.existsByEmail(signUpRequest.getEmail());
        if (isExistsByUsername) {
            return getResponseMessage("Error: Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if (isExistsByEmail) {
            return getResponseMessage("Error: Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                             signUpRequest.getEmail(),
                             encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                          .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
            roles.add(userRole);
        } else {
            roles = generateRoles(strRoles);
        }
        user.setRoles(roles);
        userRepository.save(user);
        return getResponseMessage("User registered successfully!", HttpStatus.CREATED);
    }

    /**
     * Delete specific user by user name
     *
     * @param name name of the user
     * @return @{@link ResponseEntity}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/delete/{name}")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Delete user success"),
                           @ApiResponse(code = 400, message = "Exception occur when processing")})
    public ResponseEntity<ResponseMessage> deleteUser(
        @PathVariable(value = "name") String name) {
        User user = userRepository.findByUsername(name)
                                  .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE + name));
        userRepository.delete(user);
        return getResponseMessage("User delete successfully!", HttpStatus.NO_CONTENT);
    }

    /**
     * Search user by name
     *
     * @param name name of the user
     * @return @{@link User}
     */
    @Cacheable("getUser")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/search/{name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Search user success"),
                           @ApiResponse(code = 400, message = "Exception occur when processing")})
    public ResponseEntity<ResponseMessage> searchUser(
        @PathVariable(value = "name") String name) {
        return getResponseMessage(userRepository.findByUsername(name)
                                                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE + name)),
                                  HttpStatus.OK);
    }

    /**
     * Search all users
     *
     * @return @{@link List} of @{@link User}
     */
    @Cacheable("getAllusers")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/search/all")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Search all user success"),
                           @ApiResponse(code = 400, message = "Exception occur when processing")})
    public ResponseEntity<ResponseMessage> getAllUsers() {
        return getResponseMessage(userRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Search all user roles
     *
     * @return @{@link List} of @{@link Role}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/role/search-all")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Search all role success"),
                           @ApiResponse(code = 400, message = "Exception occur when processing")})
    public ResponseEntity<ResponseMessage> getAllRoles() {
        return getResponseMessage(roleRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Update user
     *
     * @param name          name of user
     * @param signUpRequest user information
     * @return @{@link User} updated user object
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/update/{name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "update user success"),
                           @ApiResponse(code = 400, message = "Exception occur when processing")})
    public ResponseEntity<ResponseMessage> updateUser(
        @PathVariable(value = "name") String name, @Valid @RequestBody SignupRequest signUpRequest) {
        User user = userRepository.findByUsername(name)
                                  .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE + name));
        Set<String> strRoles = signUpRequest.getRole();
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();
        String userName = signUpRequest.getUsername();
        if (CollectionUtils.isNotEmpty(strRoles)) {
            Set<Role> roles = generateRoles(strRoles);
            user.setRoles(roles);
        }
        user.setPassword(encoder.encode(StringUtils.isNotBlank(password) ? password : user.getPassword()));
        user.setEmail(StringUtils.isNotBlank(email) ? email : user.getEmail());
        user.setUsername(StringUtils.isNotBlank(userName) ? userName : user.getUsername());
        userRepository.save(user);
        return getResponseMessage(user, HttpStatus.OK);
    }

    /**
     * Update user roles
     *
     * @param name name of role
     * @return @{@link Role} updated role
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/update-role/{name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "update role success"),
                           @ApiResponse(code = 400, message = "Exception occur when processing")})
    public ResponseEntity<ResponseMessage> updateRole(
        @PathVariable(value = "name") String name) {
        Role role = roleRepository.findByName(ERole.valueOf(name))
                                  .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
        role.setName(ERole.valueOf(name));
        roleRepository.save(role);
        return getResponseMessage(role, HttpStatus.OK);
    }

    /**
     * Add new user role
     *
     * @param roleName role name for user
     * @return @{@link Role} newly created role
     */
    @PostMapping("/user/role/{roleName}")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "update role success"),
                           @ApiResponse(code = 400, message = "Exception occur when processing")})
    public ResponseEntity<ResponseMessage> addUserRole(
        @PathVariable(value = "roleName") String roleName) {
        Role role = new Role();
        role.setName(ERole.valueOf(roleName));
        roleRepository.save(role);
        return getResponseMessage("Role registered successfully!", HttpStatus.CREATED);
    }

    private Set<Role> generateRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                                   .orElseThrow(
                                                       () -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
                    roles.add(adminRole);

                    break;
                case "mod":
                    Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                                 .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
                    roles.add(modRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                                  .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
                    roles.add(userRole);
            }
        });
        return roles;
    }

    private ResponseEntity<ResponseMessage> getResponseMessage(Object value, HttpStatus status) {
        ResponseMessage message = new ResponseMessage.ResponseBuilder()
            .setCode(status.value())
            .setMessage(value)
            .setTimeStamp(new Date())
            .setTraceId(request.getHeader("dialog-trace-id"))
            .build();
        return new ResponseEntity<>(message, status);
    }
}