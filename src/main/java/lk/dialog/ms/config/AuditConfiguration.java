/**
 * The AuditConfiguration class used for create auditorAware bean.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.config;

import lk.dialog.ms.audit.SpringSecurityAuditorAware;
import lk.dialog.ms.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfiguration {

    /**
     * This will create @Audit bean
     * @return @{@link Object} or @{@link User}
     */
    @Bean
    public AuditorAware<Object> auditorAware() {
        return new SpringSecurityAuditorAware();
    }
}