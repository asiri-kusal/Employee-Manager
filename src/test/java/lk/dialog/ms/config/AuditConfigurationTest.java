/**
 * The AuditConfiguration Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import lk.dialog.ms.config.AuditConfiguration;

public class AuditConfigurationTest {

    @Test
    public void testAuditorAware() {
        AuditConfiguration configuration = new AuditConfiguration();
        assertNotNull("AuditorAware object should not be null", configuration.auditorAware());
    }
}