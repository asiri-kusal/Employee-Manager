/**
 * The SwaggerConfig Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.config;

import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfigTest {

    @Test
    public void testSwaggerSpringfoxDocket(){
        SwaggerConfig swaggerConfig = new SwaggerConfig();
        Docket docket = swaggerConfig.swaggerSpringfoxDocket();
        assertNotNull("Docket object should not be null",docket);
    }
}