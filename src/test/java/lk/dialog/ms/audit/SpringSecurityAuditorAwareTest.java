/**
 * The SpringSecurityAuditorAware Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.audit;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

import lk.dialog.ms.BaseTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.context.SecurityContextHolder;


@PrepareForTest(SecurityContextHolder.class)
@PowerMockIgnore({
                     "com.sun.crypto.*",
                     "javax.crypto.*",
                     "javax.management.*",
                     "javax.security.*"})
@RunWith(PowerMockRunner.class)
public class SpringSecurityAuditorAwareTest extends BaseTestUtil {


    @Test
    public void testGetCurrentAuditor() {
        getMockAuthentication(true);
        SpringSecurityAuditorAware springSecurityAuditorAware = new SpringSecurityAuditorAware();
        Object object = springSecurityAuditorAware.getCurrentAuditor();
        assertNotNull("Current auditor object should not be empty", object);
    }

    @Test
    public void testGetCurrentAuditorAnonymous() {
        getMockAuthentication(false);
        SpringSecurityAuditorAware springSecurityAuditorAware = new SpringSecurityAuditorAware();
        Object object = springSecurityAuditorAware.getCurrentAuditor();
        assertNotNull("Current auditor object should not be empty", object);
    }

    @Test
    public void testGetCurrentAuditorAuthenticated() {
        SpringSecurityAuditorAware springSecurityAuditorAware = new SpringSecurityAuditorAware();
        Object object = springSecurityAuditorAware.getCurrentAuditor();
        assertNotNull("Current auditor object should be empty", object);
    }

}