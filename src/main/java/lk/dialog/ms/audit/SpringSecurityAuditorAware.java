/**
 * The SpringSecurityAuditorAware program implements for capture user details from security context.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.audit;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lk.dialog.ms.models.User;
import lk.dialog.ms.security.services.UserDetailsImpl;

public class SpringSecurityAuditorAware implements AuditorAware<Object> {

    private static final Logger LOGGER = LogManager.getLogger(SpringSecurityAuditorAware.class);
    private static final String NOT_AUTHENTICATED = "User Not Authenticated";
    private static final String AUTHENTICATED = "User Authenticated";

    /**
     * @return Object or @{@link User}
     * {@inheritDoc AuditorAware}
     */
    @Override
    public Optional<Object> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            LOGGER.info(NOT_AUTHENTICATED);
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
            String name = userPrincipal.getUsername();
            LOGGER.info(AUTHENTICATED);
            return Optional.of(name);
        }
        return Optional.of(authentication.getPrincipal());
    }
}